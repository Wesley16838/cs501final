package com.example.taiwanets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    Button loginBtn, register;
    EditText fullname, email, password;
    RadioGroup radioStatus;
    RadioButton radioBtn;
    private FirebaseAuth auth;
    private static final String TAG = "RegisterPage";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        radioStatus = (RadioGroup) findViewById(R.id.radioGroup);
        loginBtn = findViewById(R.id.login);
        register = findViewById(R.id.btnSignup);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, LoginPage.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_fullname = fullname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                int selectedId = radioStatus.getCheckedRadioButtonId();
                radioBtn = (RadioButton) findViewById(selectedId);
                String txt_status = radioBtn.getText().toString();
                if(TextUtils.isEmpty(txt_fullname)||TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_status)){
                    Toast.makeText(RegisterPage.this, "Empty Credentials!",Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<5){
                    Toast.makeText(RegisterPage.this, "Password must longer than 5 digits!",Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(txt_email,txt_password,txt_fullname,txt_status);
                }
            }
        });
    }

    private void registerUser(final String email, final String password, final String fullname,final String status){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fullname).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
//                                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                Map<String, Object> user = new HashMap<>();
//                                user.put("status",status);
//                                db.collection("user").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(RegisterPage.this,FormPage.class);
                                        SharedPreferences sharedPreferences = getSharedPreferences("user" , MODE_PRIVATE);
                                        sharedPreferences.edit().putString("email", email).apply();
                                        sharedPreferences.edit().putString("status" , status).apply();
                                        sharedPreferences.edit().putString("fullname" , fullname).apply();
                                        startActivity(intent);
                                        finish();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(RegisterPage.this, "Error:"+e, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterPage.this, "Authentication failed:"+task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
