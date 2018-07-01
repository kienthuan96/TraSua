package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.PostMilkTeaActivity;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailStoreAdminActivity extends AppCompatActivity {
    private TextView txtNameDetailStore;
    private TextView txtAddressDetailStore;
    private TextView txtNumberPhoneDetailStore;
    private Button btnDeleteStore,btnEditStore, btnAddMilkTea;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private String nameUser;
    private String idUser;
    private String idStore;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store_admin);
        getId();
        setData();
        getEvent();
    }

    private void getId(){
        txtNameDetailStore= findViewById(R.id.txtNameStore_detaiAdmin);
        txtAddressDetailStore=findViewById(R.id.txtAddressStore_detaiAdmin);
        txtNumberPhoneDetailStore=findViewById(R.id.txtNumberPhoneStore_detaiAdmin);
        btnDeleteStore=findViewById(R.id.btnDeleteStore_detailAdmin);
        btnEditStore=findViewById(R.id.btnEditStore_detailAdmin);
        btnAddMilkTea=findViewById(R.id.btnAddMilkTea_detailAdmin);
    }

    private void setData(){
        getData();
        txtNameDetailStore.setText(store.getNameStore());
        txtAddressDetailStore.setText(store.getAddress());
        txtNumberPhoneDetailStore.setText(store.getNumberPhone());
    }

    private void getData(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        nameUser=mUser.getDisplayName();
        idUser=mUser.getUid();

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("admin");
        idStore=bundle.getString("idStore");
        readData();
    }

    private void readData(){
        store=new Store();
        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference("store");
        myRef.child(idStore).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                store=dataSnapshot.getValue(Store.class);
                txtNameDetailStore.setText(store.getNameStore());
                txtAddressDetailStore.setText(store.getAddress());
                txtNumberPhoneDetailStore.setText(store.getNumberPhone());
                Log.d("load","load");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailStoreAdminActivity.this, "Khong thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEvent(){
        btnDeleteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef=mDatabase.getReference("store");
                myRef.child(idStore).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailStoreAdminActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(DetailStoreAdminActivity.this,AdminActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailStoreAdminActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnEditStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailStoreAdminActivity.this, EditStoreActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("idStore",idStore);
                intent.putExtra("admin",bundle);
                startActivity(intent);
            }
        });

        btnAddMilkTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailStoreAdminActivity.this, PostMilkTeaActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("idStore",idStore);
                intent.putExtra("admin",bundle);
                startActivity(intent);
            }
        });
    }

}
