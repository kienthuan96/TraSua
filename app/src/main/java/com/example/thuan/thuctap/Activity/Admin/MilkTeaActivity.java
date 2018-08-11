package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MilkTeaActivity extends AppCompatActivity {
    private Button btnEdit, btnDelete;
    private String idStore, idMilkTea;
    private String status;
    private TextView txtNameMilkTea;
    private TextView txtPriceMilkTea;
    private TextView txtDescriptionMilkTea;
    private TextView txtDateMilkTea;
    private ImageView imgMilkTea;
    private Spinner spnStatus;
    private ArrayList<String> arrStatus;

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
        imgMilkTea = findViewById(R.id.imgMilkTea_milkTea);
        spnStatus = findViewById(R.id.spnStatusMilkTea_milkTea);
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

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = arrStatus.get(i);
                myRefMilkTea.child(idMilkTea).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("status").setValue(status);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference pathReference = storageRef.child("IMG_CONTACT/"+milkTea.getImageMilkTea());
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        Glide.with(MilkTeaActivity.this).load(imageURL).into(imgMilkTea);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arrStatus = new ArrayList<>();
        arrStatus.add("Ready");
        arrStatus.add("Sell");
        arrStatus.add("End");
        ArrayAdapter<String> adapterStatus = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrStatus);
        spnStatus.setDropDownHorizontalOffset(android.R.layout.simple_list_item_single_choice);
        spnStatus.setAdapter(adapterStatus);
    }
}
