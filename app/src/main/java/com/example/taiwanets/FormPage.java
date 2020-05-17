package com.example.taiwanets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FormPage extends AppCompatActivity {
    Button form;
    private static final String TAG = "Formpage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_page);
        form = findViewById(R.id.goform);
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentNext = new Intent(FormPage.this,PersonalInformation.class);

                startActivity(intentNext);
                finish();
            }
        });
    }
}
