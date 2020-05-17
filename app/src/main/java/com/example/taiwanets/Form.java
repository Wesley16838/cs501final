package com.example.taiwanets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText description, job, company, degree, major;
    Spinner spinnerYear, spinnerMonth, searchableSpinnerSchool;

    Button submitBtn, upload, add;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ImageView profileImage;
    LottieAnimationView lottie;
    LinearLayout dateView;
    public Uri imageuri;
    public int numberOfLines = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        dateView = findViewById(R.id.date);
        spinnerYear = findViewById(R.id.year);
        spinnerMonth = findViewById(R.id.month);
        description = findViewById(R.id.textDescription);
        job = findViewById(R.id.jobtitle);
        degree = findViewById(R.id.degree);
        major = findViewById(R.id.major);
        company = findViewById(R.id.company);
        submitBtn = findViewById(R.id.submit);
        upload = findViewById(R.id.upload);
        add =findViewById(R.id.add);
        profileImage = findViewById(R.id.profileImage);
        lottie = findViewById(R.id.animation);
        lottie.bringToFront();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Images");
        searchableSpinnerSchool = findViewById(R.id.searchableSpinner);

        Intent intent=getIntent();
        final String fullname = intent.getStringExtra("fullname");
        final String email = intent.getStringExtra("email");
        final String status = intent.getStringExtra("status");
        if(status.equals("EMPLOYEE")){
            dateView.setVisibility(View.GONE);
        }else{
            populateSpinnerYear();
            populateSpinnerMonth();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Line();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }

        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String school = searchableSpinnerSchool.getSelectedItem().toString();
                String des = description.getText().toString();
                String jobtitle =job.getText().toString();
                String com= company.getText().toString();
                String deg = degree.getText().toString();
                String ma = major.getText().toString();
                if(status.equals("EMPLOYEE")){
                    String year = spinnerYear.getSelectedItem().toString();
                    String month = spinnerMonth.getSelectedItem().toString();
                    Fileuploader(school, year, month, des ,fullname,email,jobtitle, com, deg, ma);//school, year, month, description, fullname,  email
                }else{
                    Fileuploader(school, "", "", des ,fullname,email,jobtitle, com, deg, ma);//school, year, month, description, fullname,  email
                }

            }
        });

        spinnerYear.setOnItemSelectedListener(this);
        spinnerMonth.setOnItemSelectedListener(this);
        searchableSpinnerSchool.setAdapter(new ArrayAdapter<>(Form.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_school)));
        searchableSpinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position ==0){
//                    //display toast message
//
//                    //set empty value
//
//                }else{
//                    String school = parent.getItemAtPosition(position).toString();
//
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader(final String school, final String year, final String month, final String description, final String fullname, final String email, final String jobtitle, final String company, final String degree, final String major){
        lottie.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final StorageReference Ref =  storageReference.child(System.currentTimeMillis()+"."+getExtension(imageuri));
        Ref.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        final String url = uri.toString();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> user = new HashMap<>();
                        ArrayList arr = getContent();
                        user.put("School",school);
                        user.put("Year",year);
                        user.put("Month",month);
                        user.put("Image",url);
                        user.put("Description",description);
                        user.put("Fullname",fullname);
                        user.put("JobTitle",jobtitle);
                        user.put("CompanyName",company);
                        user.put("ContentForReferral",arr);
                        db.collection("user").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                lottie.setVisibility(View.GONE);
//                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                                Toast.makeText(Form.this,"ImageuploadSuccessfully "+url,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Form.this,ProfilePage.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                lottie.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(Form.this, "Error:"+e, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data.getData() != null){
            imageuri = data.getData();
            profileImage.setImageURI(imageuri);
        }
    }

    private void populateSpinnerYear() {
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year));
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }
    private void populateSpinnerMonth() {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.month));
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.year){
//            String selectedYear = parent.getSelectedItem().toString();

        }else if(parent.getId() == R.id.month){
//            String selectedMonth = parent.getSelectedItem().toString();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void Add_Line() {
        LinearLayout ll = (LinearLayout)findViewById(R.id.list);
        // add edittext
        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,5,0,5);
        et.setLayoutParams(p);
        et.setImeOptions(5);
        et.setSingleLine(true);
        et.setPadding(10,0,10,0);
        et.setBackground(Drawable.createFromPath("@drawable/shadow"));
        et.setHint("EX: Brief description in 3rd person");
        et.setId(numberOfLines + 1);
        ll.addView(et);
        numberOfLines++;
    }
    public ArrayList<String> getContent() {
        ArrayList arr = new ArrayList();
        for(int i=1; i<numberOfLines+1;i++){
            int resID = this.getResources().getIdentifier(String.valueOf(i), "id", Form.this.getPackageName());
            EditText text = findViewById(resID);
            String and = text.getText().toString();
            arr.add(and);
        }
        return arr;
    }
}
