package com.example.taiwanets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReferralLetter extends AppCompatActivity {
    private static final String TAG = "Final Page";
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Button done, back, add;
    TextView fn, em;
    public int numberOfLines = 0;
    public Uri imageuri;
    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_letter);
        done = findViewById(R.id.done);
        add = findViewById(R.id.add);
        fn = findViewById(R.id.fullname);
        em = findViewById(R.id.email);

        final SharedPreferences sharedPreferences = getSharedPreferences("user" , MODE_PRIVATE);

        final String uri = sharedPreferences.getString("imageuri" , "");
        final String fullname = sharedPreferences.getString("fullname" , "");
        final String email = sharedPreferences.getString("email" , "");
        final String status = sharedPreferences.getString("status" , "");
        final String summary = sharedPreferences.getString("summary" , "");
        final String headline = sharedPreferences.getString("headline" , "");
        fn.setText(fullname);
        em.setText(email);
        //education
        final String school = sharedPreferences.getString("school","");
        final String year = sharedPreferences.getString("schoolyear","");
        final String month = sharedPreferences.getString("schoolmonth","");
        final String deg = sharedPreferences.getString("degree","");
        final String ma = sharedPreferences.getString("major","");

        //work
        final String des = sharedPreferences.getString("description","");
        final String com = sharedPreferences.getString("company","");
        final String jobtitle = sharedPreferences.getString("jobtitle","");
        final String location = sharedPreferences.getString("location","");
        final String jobyear = sharedPreferences.getString("jobyear","");
        final String jobmonth = sharedPreferences.getString("jobmonth","");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Images");
        imageuri = Uri.parse(uri);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"test"+uri);
                Add_Line();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fileuploader(school, year, month,deg,ma, des, jobtitle, com, location,jobyear,jobmonth,fullname,email, summary, headline,status);
            }
        });
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader(final String school, final String year, final String month, final String degree, final String major, final String description, final String jobtitle, final String company, final String location, final String jobyear, final String jobmonth, final String fullname, final String email, final String summary, final String headline, final String status){

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
                        //required list for referral
                        ArrayList arr = getContent();
                        user.put("School",school);
                        user.put("SchoolYear",year);
                        user.put("SchoolMonth",month);
                        user.put("Degree",degree);
                        user.put("Major",major);
                        user.put("Image",url);
                        user.put("Description",description);
                        user.put("Fullname",fullname);
                        user.put("Status",status);
                        user.put("Summary",summary);
                        user.put("Headline",headline);
                        user.put("JobTitle",jobtitle);
                        user.put("CompanyName",company);
                        user.put("Location",location);
                        user.put("JobStartYear",jobyear);
                        user.put("JobStartMonth",jobmonth);
                        user.put("ContentForReferral",arr);
                        db.collection("user").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Intent intent = new Intent(ReferralLetter.this,ProfilePage.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(ReferralLetter.this, "Error:"+e, Toast.LENGTH_SHORT).show();
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


    public void Add_Line() {
        LinearLayout ll = (LinearLayout)findViewById(R.id.list);
        String[] content = {"Ex.Resume", "Ex.Email", "Ex.Phone Number", "Ex.Brief Introduction", "Ex.Job ID"};
        // add edittext
        EditText et = new EditText(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,10,0,10);
        et.setLayoutParams(p);
        et.setImeOptions(5);
        et.setSingleLine(true);
        et.setPadding(50,0,50,0);
        et.setBackgroundResource(R.drawable.round_border);
        if(numberOfLines>4){
            et.setHint("");
        }else{
            et.setHint(content[numberOfLines]);
        }
        et.setHeight(100);
        et.setWidth(250);
        et.setId(numberOfLines + 1);
        ll.addView(et);
        numberOfLines++;
    }
    public ArrayList<String> getContent() {
        ArrayList arr = new ArrayList();
        for(int i=1; i<numberOfLines+1;i++){
            int resID = this.getResources().getIdentifier(String.valueOf(i), "id", ReferralLetter.this.getPackageName());
            EditText text = findViewById(resID);
            String and = text.getText().toString();
            arr.add(and);
        }
        return arr;
    }
}
