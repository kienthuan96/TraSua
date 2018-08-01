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
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MilkTeaActivity extends AppCompatActivity {
    private Button btnEdit, btnDelete;
    private String idStore, idMilkTea;
    private TextView txtNameMilkTea;
    private TextView txtPriceMilkTea;
    private TextView txtDescriptionMilkTea;
    private TextView txtDateMilkTea;

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
        txtNameMilkTea = findViewById(R.id.txtNameMilkTea_milkTea);
        txtPriceMilkTea = findViewById(R.id.txtPriceMilkTea_milkTea);
        txtDateMilkTea = findViewById(R.id.txtDate_milkTea);
        txtDescriptionMilkTea = findViewById(R.id.txtDescription_milkTea);
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
        myRefMilkTea.child(idMilkTea).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                txtNameMilkTea.setText(milkTea.getNameMilkTea());
                txtPriceMilkTea.setText(milkTea.getPrice());
                txtDescriptionMilkTea.setText(milkTea.getDescription());
                txtDateMilkTea.setText(milkTea.getDateUp());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
