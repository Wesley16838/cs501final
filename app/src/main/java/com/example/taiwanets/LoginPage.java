package com.example.taiwanets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {
    EditText email, password;
    Button btnLogIn, registerBtn;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogIn = findViewById(R.id.btnLogin);
        registerBtn = (Button)findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    startActivity(new Intent(LoginPage.this, RegisterPage.class));
                    finish();

            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if(txt_email == null || txt_password == null){
                    Toast.makeText(getApplicationContext(),"Email or Password is empty",Toast.LENGTH_LONG).show();
                }else{
                    loginUser(txt_email, txt_password);
                }
            }
        });

    }
    private void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = auth.getCurrentUser();
                    startActivity(new Intent(LoginPage.this, ProfilePage.class));
                    finish();
                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(LoginPage.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
