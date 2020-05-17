package com.example.taiwanets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class WorkExperience extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinnerYear, spinnerMonth, spinnerEndYear, spinnerEndMonth;
    LinearLayout student, employee, enddate;
    TextView fullname, email, workenddate;
    EditText company, jobtitle, description, location;
    Button back, next;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    public Uri imageuri;
    private static final String TAG = "WORK";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        student= findViewById(R.id.layoutStatus_student);
        employee = findViewById(R.id.layoutStatus_employee);
        enddate = findViewById(R.id.end_date);

        company = findViewById(R.id.company);
        jobtitle = findViewById(R.id.title);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        workenddate = findViewById(R.id.status_work_ended_date);
        spinnerYear = findViewById(R.id.start_year);
        spinnerMonth = findViewById(R.id.start_month);
        spinnerYear.setOnItemSelectedListener(this);
        spinnerMonth.setOnItemSelectedListener(this);

        final SharedPreferences sharedPreferences = getSharedPreferences("user" , MODE_PRIVATE);
        final String fn = sharedPreferences.getString("fullname" , "");
        final String em = sharedPreferences.getString("email" , "");
        final String status = sharedPreferences.getString("status" , "");

        final String cm = sharedPreferences.getString("company" , "");
        final String jt = sharedPreferences.getString("jobtitle" , "");
        final String ds = sharedPreferences.getString("description" , "");
        final String lc = sharedPreferences.getString("location" , "");

        company.setText(cm);
        jobtitle.setText(jt);
        description.setText(ds);
        location.setText(lc);

        if(status.equals("STUDENT")){
            enddate.setVisibility(View.VISIBLE);
            workenddate.setVisibility(View.VISIBLE);
            student.setVisibility(View.VISIBLE);
            employee.setVisibility(View.GONE);
            spinnerEndYear = findViewById(R.id.end_year);
            spinnerEndMonth = findViewById(R.id.end_month);
            spinnerEndYear.setOnItemSelectedListener(this);
            spinnerEndMonth.setOnItemSelectedListener(this);
        }else if(status.equals("EMPLOYEE")){
            enddate.setVisibility(View.GONE);
            workenddate.setVisibility(View.GONE);
            student.setVisibility(View.GONE);
            employee.setVisibility(View.VISIBLE);

        }
        populateSpinnerYear(status);
        populateSpinnerMonth(status);
        fullname.setText(fn);
        email.setText(em);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        final String uri = sharedPreferences.getString("imageuri" , "");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Images");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkExperience.this, Education.class));
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String year = spinnerYear.getSelectedItem().toString();
                String month = spinnerMonth.getSelectedItem().toString();
                String com = company.getText().toString();
                String job = jobtitle.getText().toString();
                String des = description.getText().toString();
                String loc = location.getText().toString();
                if(TextUtils.isEmpty(com) || TextUtils.isEmpty(job)||TextUtils.isEmpty(des)||TextUtils.isEmpty(loc)){
                    Toast.makeText(WorkExperience.this, "Empty Fields!",Toast.LENGTH_SHORT).show();
                }else if(year.equals("Select year") || month.equals("Select month")){
                    Toast.makeText(WorkExperience.this, "Empty Fields!",Toast.LENGTH_SHORT).show();
                }else{
                    sharedPreferences.edit().putString("company", com ).apply();
                    sharedPreferences.edit().putString("jobtitle", job ).apply();
                    sharedPreferences.edit().putString("description", des ).apply();
                    sharedPreferences.edit().putString("location", loc ).apply();
                    sharedPreferences.edit().putString("jobyear", year ).apply();
                    sharedPreferences.edit().putString("jobmonth", month ).apply();
                    if(status.equals("EMPLOYEE")){
                        startActivity(new Intent(WorkExperience.this, ReferralLetter.class));
                        finish();
                    }else if(status.equals("STUDENT")){
                        String endyear = spinnerEndYear.getSelectedItem().toString();
                        String endmonth = spinnerEndMonth.getSelectedItem().toString();
                        if(Integer.parseInt(endyear)<Integer.parseInt(year)){
                            Toast.makeText(WorkExperience.this, "End year should not be earlier than begin year!",Toast.LENGTH_SHORT).show();
                        }else if(Integer.parseInt(endyear) == Integer.parseInt(year)){
                            if(Integer.parseInt(endmonth) < Integer.parseInt(month)){
                                Toast.makeText(WorkExperience.this, "End date should not be earlier than begin date!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            final String uri = sharedPreferences.getString("imageuri" , "");
                            final String fullname = sharedPreferences.getString("fullname" , "");
                            final String email = sharedPreferences.getString("email" , "");
                            final String status = sharedPreferences.getString("status" , "");
                            final String summary = sharedPreferences.getString("summary" , "");
                            final String headline = sharedPreferences.getString("headline" , "");

                            //education
                            final String school = sharedPreferences.getString("school","");
                            final String schoolyear = sharedPreferences.getString("schoolyear","");
                            final String schoolmonth = sharedPreferences.getString("schoolmonth","");
                            final String deg = sharedPreferences.getString("degree","");
                            final String ma = sharedPreferences.getString("major","");

                            //work
                            final String jobtitle = sharedPreferences.getString("jobtitle","");
                            final String location = sharedPreferences.getString("location","");
                            final String jobyear = sharedPreferences.getString("jobyear","");
                            final String jobmonth = sharedPreferences.getString("jobmonth","");

                            imageuri = Uri.parse(uri);
                            Fileuploader(school, schoolyear, schoolmonth, deg, ma, des, jobtitle, com, location, jobyear, jobmonth, endyear, endmonth, fullname,email, summary, headline,status);
                        }

                    }
                }
            }
        });
    }

    private void populateSpinnerYear(String status) {
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.year));
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
        Log.d(TAG,"Status ==== "+status);
        if(status.equals("STUDENT")){
            Log.d(TAG,"IN STUDENT");
            spinnerEndYear.setAdapter(yearAdapter);
        }
    }
    private void populateSpinnerMonth(String status) {
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.month));
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);
        Log.d(TAG,"Status ==== "+status);
        if(status.equals("STUDENT")){
            Log.d(TAG,"IN STUDENT");
            spinnerEndMonth.setAdapter(monthAdapter);
        }
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
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void Fileuploader(final String school, final String year, final String month, final String degree, final String major, final String description, final String jobtitle, final String company, final String location,final String startyear,final String startmonth, final String endyear, final String endmonth, final String fullname, final String email, final String summary, final String headline, final String status){

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

                        user.put("School",school);
                        user.put("SchoolYear",year);
                        user.put("SchoolMonth",month);
                        user.put("Degree",degree);
                        user.put("Major",major);

                        user.put("Image",url);
                        user.put("Fullname",fullname);
                        user.put("Status",status);
                        user.put("Summary",summary);
                        user.put("Headline",headline);

                        user.put("Description",description);
                        user.put("JobTitle",jobtitle);
                        user.put("CompanyName",company);
                        user.put("Location",location);
                        user.put("JobStartYear",startyear);
                        user.put("JobStartMonth",startmonth);
                        user.put("JobEndYear",endyear);
                        user.put("JobEndMonth",endmonth);
                        db.collection("user").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Intent intent = new Intent(WorkExperience.this,ProfilePage.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(WorkExperience.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
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


}
