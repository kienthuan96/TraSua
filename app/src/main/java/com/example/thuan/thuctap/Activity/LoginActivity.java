package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.spark.submitbutton.SubmitButton;

public class LoginActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private SubmitButton btnSubmit;
    private TextView txtRegister;
    private CheckBox chkSaveInfo;

    private FirebaseAuth mAuth;
    private String account,password;
    private String prefName="my_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        getId();
//        checkLogin();

        getEvent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPreferences();
    }

    public void savingPreferences()
    {
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=getSharedPreferences
                (prefName, MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        boolean bchk=chkSaveInfo.isChecked();
        if(!bchk)
        {
            //xóa mọi lưu trữ trước đó
            editor.clear();
        }
        else
        {
            //lưu vào editor
            editor.putString("account", account);
            editor.putString("password", password);
            editor.putBoolean("checked", bchk);
        }
        //chấp nhận lưu xuống file
        editor.commit();
    }
    /**
     * hàm đọc trạng thái đã lưu trước đó
     */
    public void restoringPreferences()
    {
        SharedPreferences pre=getSharedPreferences
                (prefName,MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk=pre.getBoolean("checked", false);
        if(bchk)
        {
            //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
            String user=pre.getString("account", "");
            String pwd=pre.getString("password", "");
            edtAccount.setText(user);
            edtPassword.setText(pwd);
        }
        chkSaveInfo.setChecked(bchk);
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
        chkSaveInfo=findViewById(R.id.chkSaveInfo_login);
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
