package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostMilkTeaActivity extends AppCompatActivity {
    private EditText edtNameMilkTea;
    private EditText edtPriceMilkTea;
    private EditText edtDescribeMilkTea;
    private ImageView imgMilkTea;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private String idUser;
    private String idStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_milk_tea);
        getId();
        getData();
    }

    private void getId(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        idUser=mUser.getUid();

        edtNameMilkTea = findViewById(R.id.edtNameMilkTea_postMilkTea);
        edtPriceMilkTea = findViewById(R.id.edtPriceMilkTea_postMilkTea);
        edtDescribeMilkTea = findViewById(R.id.edtDescribeMilkTea_postMilkTea);
        imgMilkTea = findViewById(R.id.imgMilkTea_postMilkTea);
    }

    private void getData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("admin");
        idStore=bundle.getString("idStore");
    }
}
