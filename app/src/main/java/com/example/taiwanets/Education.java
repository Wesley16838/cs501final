package com.example.taiwanets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Education extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinnerYear, spinnerMonth, searchableSpinnerSchool;
    LinearLayout student, employee;
    Button back, next;
    TextView fn, em, status_date;
    EditText program, degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        student= findViewById(R.id.layoutStatus_student);
        employee = findViewById(R.id.layoutStatus_employee);
        program = findViewById(R.id.program);
        degree = findViewById(R.id.degree);
        status_date = findViewById(R.id.status_education_date);
        spinnerYear = findViewById(R.id.year);
        spinnerMonth = findViewById(R.id.month);
        back = findViewById(R.id.back);
        searchableSpinnerSchool = findViewById(R.id.searchableSpinner);
        final SharedPreferences sharedPreferences = getSharedPreferences("user" , MODE_PRIVATE);
        final String fullname = sharedPreferences.getString("fullname" , "");
        final String email = sharedPreferences.getString("email" , "");
        final String status = sharedPreferences.getString("status" , "");
        final String pg = sharedPreferences.getString("major" , "");
        final String dg = sharedPreferences.getString("degree" , "");
        program.setText(pg);
        degree.setText(dg);
        fn = findViewById(R.id.fullname);
        em = findViewById(R.id.email);
        fn.setText(fullname);
        em.setText(email);
        if(status.equals("STUDENT")){
            student.setVisibility(View.VISIBLE);
            employee.setVisibility(View.GONE);
            status_date.setText("Expected graduation date");
        }else if(status.equals("EMPLOYEE")){
            student.setVisibility(View.GONE);
            employee.setVisibility(View.VISIBLE);
            status_date.setText("Graduation date");
        }
        spinnerYear.setOnItemSelectedListener(this);
        spinnerMonth.setOnItemSelectedListener(this);
        populateSpinnerYear();
        populateSpinnerMonth();
        next = findViewById(R.id.next);
        searchableSpinnerSchool.setAdapter(new ArrayAdapter<>(Education.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.spinner_school)));
        searchableSpinnerSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Education.this, PersonalInformation.class));
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String school = searchableSpinnerSchool.getSelectedItem().toString();
                Log.i("TAG school", school);
                String year = spinnerYear.getSelectedItem().toString();
                Log.i("TAG year", year);
                String month = spinnerMonth.getSelectedItem().toString();
                Log.i("TAG month", month);
                String prog = program.getText().toString();
                String deg = degree.getText().toString();
                if(TextUtils.isEmpty(prog) || TextUtils.isEmpty(deg)){
                    Toast.makeText(Education.this, "Empty Fields!",Toast.LENGTH_SHORT).show();
                }else if(school.equals("Select School") || year.equals("Select year") || month.equals("Select month")){
                    Toast.makeText(Education.this, "Empty Fields!",Toast.LENGTH_SHORT).show();
                }else{
                    sharedPreferences.edit().putString("school", school).apply();
                    sharedPreferences.edit().putString("schoolyear" , year).apply();
                    sharedPreferences.edit().putString("schoolmonth" , month).apply();
                    sharedPreferences.edit().putString("major" , prog).apply();
                    sharedPreferences.edit().putString("degree" , deg).apply();
                    startActivity(new Intent(Education.this, WorkExperience.class));
                    finish();
                }

            }
        });
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

}
