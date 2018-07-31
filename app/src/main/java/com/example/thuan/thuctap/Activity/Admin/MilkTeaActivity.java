package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MilkTeaActivity extends AppCompatActivity {
    private Button btnEdit, btnDelete;
    private String idStore, idMilkTea;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefMilkTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_tea);
        getId();
        event();
    }



    private void getId(){
        btnEdit = findViewById(R.id.btnEditMilkTea_milkTea);
        btnDelete = findViewById(R.id.btnDeleteMilkTea_milkTea);
    }

    private void event(){
        getData();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRefMilkTea.child(idMilkTea).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MilkTeaActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MilkTeaActivity.this, AdminActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MilkTeaActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("admin");
        if (bundle != null) {
            idStore = bundle.getString("idStore");
            idMilkTea = bundle.getString("idMilkTea");
        }

        mDatabase = FirebaseDatabase.getInstance();
        myRefMilkTea = mDatabase.getReference("milkTea");

    }
}
