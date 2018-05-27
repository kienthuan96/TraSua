package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thuan.thuctap.R;
import com.spark.submitbutton.SubmitButton;

public class LoginActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private SubmitButton btnSubmit;
    private TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getId();

        getEvent();
    }

    /**
     * lay id
     */
    private void getId(){
        edtAccount=findViewById(R.id.edtAccount_login);
        edtPassword=findViewById(R.id.edtPassword_login);
        btnSubmit=findViewById(R.id.btnSubmit_login);
        txtRegister=findViewById(R.id.txtRegister_login);
    }

    /**
     * bat su kien
     */
    private void getEvent(){
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transRegister();
            }
        });
    }

    /**
     * chuyen den activity Register
     */
    private void transRegister(){
        Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
