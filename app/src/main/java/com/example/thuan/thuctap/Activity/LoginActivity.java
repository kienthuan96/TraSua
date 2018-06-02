package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

public class LoginActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private SubmitButton btnSubmit;
    private TextView txtRegister;

    private FirebaseAuth mAuth;
    private String account,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        getId();
        checkLogin();

        getEvent();
    }

    /**
     * kiem tra da dang nhap user chua
     */
    private void checkLogin(){
        if (mAuth!=null){
            transAdmin();
        }
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
    private void getData(){
        account=edtAccount.getText().toString();
        password=edtPassword.getText().toString();
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                signIn(account,password);
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

    /**
     * chuyen den activity Admin
     */
    private void transAdmin(){
        Intent intent=new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            transAdmin();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
