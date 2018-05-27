package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.thuan.thuctap.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private EditText edtFullName;
    private EditText edtPhone;
    private ImageView imgAvatar;
    private Button btnRegister;
    private InputStream inputStream;
    private Uri uri;

    private int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        edtAccount=findViewById(R.id.edtAccount_register);
        edtPassword=findViewById(R.id.edtAccount_register);
        edtFullName=findViewById(R.id.edtFullName_register);
        edtPhone=findViewById(R.id.edtPhone_register);
        imgAvatar=findViewById(R.id.imgAvatar_register);
        btnRegister=findViewById(R.id.btnRegister);
    }

    /**
     * bat su kien
     */
    private void getEvent(){
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transGallery();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * chuyen sang thu vien cua may
     */
    private void transGallery(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }
}
