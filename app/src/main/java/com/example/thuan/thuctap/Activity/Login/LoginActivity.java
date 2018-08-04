package com.example.thuan.thuctap.Activity.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Activity.Admin.AdminActivity;
import com.example.thuan.thuctap.Activity.Shipper.ShipperActivity;
import com.example.thuan.thuctap.Activity.User.UserActivity;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.library.AnimateCheckBox;
import com.spark.submitbutton.SubmitButton;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LoginActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private SubmitButton btnSubmit;
    private TextView txtRegister;
    private AnimateCheckBox chkSaveInfo;
    private MDToast mdToast;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String account,password;
    private String prefName="my_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkInternet();
        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
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

    private void checkInternet() {
        if (isConnected() == false) {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Thông báo")
                    .setMessage("Bạn chưa kết nối mạng !!!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        } else {
            Toast.makeText(this, "Đã kết nối mạng", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting())  return true;
        return false;
    }
    /**
     * kiem tra da dang nhap user chua
     */
    private void checkLogin(){
        if (mAuth!=null){
//            mdToast = MDToast.makeText(LoginActivity.this, "Name "+mAuth.getCurrentUser().getEmail(), 5000, MDToast.TYPE_ERROR);
//            mdToast.show();
            transAdmin();
        }else{
            mdToast = MDToast.makeText(LoginActivity.this, "null ", 5000, MDToast.TYPE_WARNING);
            mdToast.show();
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
                if (checkError()){
//                    signIn(account,password);
                    new BackgroundLogin().execute();
                }
            }
        });
    }

    private boolean checkError(){
        if (account.length() == 0){
            mdToast = MDToast.makeText(LoginActivity.this, "Nhap thong tin account ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (password.length() == 0){
            mdToast = MDToast.makeText(LoginActivity.this, "Nhap thong tin password ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        return true;
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
        finish();
        startActivity(intent);
    }

    private void transUser(){
        Intent intent=new Intent(LoginActivity.this, UserActivity.class);
        finish();
        startActivity(intent);
    }

    private void transShipper(){
        Intent intent=new Intent(LoginActivity.this, ShipperActivity.class);
        finish();
        startActivity(intent);
    }

    private void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            myRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (user.getStatus().equals("User")) {
                                        transUser();
                                    }
                                    if (user.getStatus().equals("Admin")) {
                                        transAdmin();
                                    }
                                    if (user.getStatus().equals("Shipper")) {
                                        transShipper();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private class BackgroundLogin extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            signIn(account,password);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }
}

