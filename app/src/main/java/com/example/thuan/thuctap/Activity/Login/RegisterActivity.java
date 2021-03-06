package com.example.thuan.thuctap.Activity.Login;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private MDToast mdToast;
    private EditText edtAccount;
    private EditText edtPassword;
    private EditText edtRePassword;
    private EditText edtFullName;
    private EditText edtPhone;
    private ImageView imgAvatar;
    private Button btnRegister;
    private Spinner spnStatus;
    private InputStream inputStream;
    private Uri uri;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private int PICK_IMAGE = 1;
    private String account;
    private String password;
    private String rePassword;
    private String fullName;
    private String status;
    private String phone;
    private List<String> arrStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        getId();

        getEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                uri=data.getData();
                inputStream = RegisterActivity.this.getContentResolver().openInputStream(data.getData());
                imgAvatar.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * lay id
     */
    private void getId(){
        edtAccount = findViewById(R.id.edtAccount_register);
        edtPassword = findViewById(R.id.edtPassword_register);
        edtRePassword = findViewById(R.id.edtRePassword_register);
        edtFullName = findViewById(R.id.edtFullName_register);
        edtPhone = findViewById(R.id.edtPhone_register);
        imgAvatar = findViewById(R.id.imgAvatar_register);
        btnRegister = findViewById(R.id.btnRegister);
        spnStatus = findViewById(R.id.spnStatusUser_register);
    }

    /**
     * lay du lieu tu xml
     */
    private void getData(){
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("user");
        account = edtAccount.getText().toString();
        password = edtPassword.getText().toString();
        rePassword = edtRePassword.getText().toString();
        fullName = edtFullName.getText().toString();
        phone = edtPhone.getText().toString();

        arrStatus = new ArrayList<>();
        arrStatus.add("User");
        arrStatus.add("Admin");
        arrStatus.add("Shipper");
        ArrayAdapter<String> adapterStatus = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrStatus);
        spnStatus.setDropDownHorizontalOffset(android.R.layout.simple_list_item_single_choice);
        spnStatus.setAdapter(adapterStatus);
    }

    /**
     * bat su kien
     */
    private void getEvent(){
        getData();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transGallery();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(RegisterActivity.this, ""+edtPhone.getText().toString(), Toast.LENGTH_SHORT).show();
                if (checkError()){
                    createUser(edtAccount.getText().toString(),edtPassword.getText().toString(),edtFullName.getText().toString(),uri,status,edtPhone.getText().toString());
                }

            }
        });

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = arrStatus.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                status = arrStatus.get(0);
            }
        });
    }

    /**
     * chuyen sang thu vien cua may
     */
    private void transGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    private void transLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     *
     * @param accountU
     * @param passwordU
     * @param fullNameU
     * @param uriU
     * tao tai khoan tren firebase
     */
    private void createUser(String accountU, String passwordU, final String fullNameU, final Uri uriU, final String status, final String phone){
        mAuth.createUserWithEmailAndPassword(accountU, passwordU)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uriU).setDisplayName(fullNameU).build();
                            user.updateProfile(userProfileChangeRequest);
                            User mUser = new User(user.getUid(),fullNameU,0,status,phone);
                            myRef.child(mUser.getIdUser()).setValue(mUser);
                            Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_SHORT).show();
                            transLogin();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * tim loi truoc khi dang ky
     * @return true khi khong co loi, false khi co loi
     */
    private boolean checkError(){
        if (edtAccount.getText().toString().isEmpty())
        {
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy nhập tài khoản ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (edtPassword.getText().toString().isEmpty()){
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy nhập mật khẩu ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (edtRePassword.getText().toString().isEmpty()){
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy nhập mật khẩu ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (edtFullName.getText().toString().isEmpty()){
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy nhập tên ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (edtPhone.getText().toString().isEmpty()){
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy nhập số điện thoại ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (uri == null){
            mdToast = MDToast.makeText(RegisterActivity.this, "Hãy chọn ảnh đại diện ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
//        if (edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
//            Toast.makeText(RegisterActivity.this,"Password not right",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (edtPassword.getText().toString().length() < 6 ){
            mdToast = MDToast.makeText(RegisterActivity.this, "Mật khẩu không đủ 6 kí tự ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        if (!edtAccount.getText().toString().contains("@")){
            mdToast = MDToast.makeText(RegisterActivity.this, "Tên tài khoản thiếu @", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        return true;
    }



}
