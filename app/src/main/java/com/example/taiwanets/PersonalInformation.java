package com.example.taiwanets;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class PersonalInformation extends AppCompatActivity {
    LinearLayout student, employee;
    TextView fullname, email;
    EditText summary, headline;
    ImageButton upload;
    Button next;
    public Uri imageuri;
    public Uri resultUri=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        final SharedPreferences sharedPreferences = getSharedPreferences("user" , MODE_PRIVATE);
        String fn = sharedPreferences.getString("fullname" , "");
        String em = sharedPreferences.getString("email" , "");
        String status = sharedPreferences.getString("status" , "");
        String sm = sharedPreferences.getString("summary" , "");
        String hl = sharedPreferences.getString("headline" , "");
        String img = sharedPreferences.getString("imageuri" , "");
        student=findViewById(R.id.layoutStatusstudent);
        employee=findViewById(R.id.layoutStatusemployee);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        summary = findViewById(R.id.summary);
        summary.setText(sm);
        headline = findViewById(R.id.headline);
        headline.setText(hl);
        upload = findViewById(R.id.addImage);
        if(!img.equals("")){
            upload.setImageURI(Uri.parse(img));
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }

        });
        next = findViewById(R.id.next);
        if(status.equals("STUDENT")){
            student.setVisibility(View.VISIBLE);
            employee.setVisibility(View.GONE);
        }else if(status.equals("EMPLOYEE")){
            student.setVisibility(View.GONE);
            employee.setVisibility(View.VISIBLE);
        }
        next.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String sum = summary.getText().toString();
                String head = headline.getText().toString();
                Log.i("Image", String.valueOf(resultUri));
                if(TextUtils.isEmpty(sum) || TextUtils.isEmpty(head)){
                    Toast.makeText(PersonalInformation.this, "Empty Fields!",Toast.LENGTH_SHORT).show();
                }else if(resultUri == null){
                    Toast.makeText(PersonalInformation.this, "Empty image!",Toast.LENGTH_SHORT).show();
                }else {
                    sharedPreferences.edit().putString("summary", sum).apply();
                    sharedPreferences.edit().putString("headline", head).apply();
                    sharedPreferences.edit().putString("imageuri", resultUri.toString()).apply();
                    startActivity(new Intent(PersonalInformation.this, Education.class));
                    finish();
                }
            }
        });
        fullname.setText(fn);
        email.setText(em);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data.getData() != null){
            imageuri = data.getData();
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                resultUri = result.getUri();
                upload.setImageURI(resultUri);

            }
        }
    }

    private void uploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent,1);
    }
}
