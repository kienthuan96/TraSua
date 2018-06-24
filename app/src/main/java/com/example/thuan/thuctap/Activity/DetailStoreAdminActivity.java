package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailStoreAdminActivity extends AppCompatActivity {
    private TextView txtNameDetailStore;
    private TextView txtAddressDetailStore;
    private TextView txtNumberPhoneDetailStore;

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
    }

    private void getId(){
        txtNameDetailStore= findViewById(R.id.txtNameStore_detaiAdmin);
        txtAddressDetailStore=findViewById(R.id.txtAddressStore_detaiAdmin);
        txtNumberPhoneDetailStore=findViewById(R.id.txtNumberPhoneStore_detaiAdmin);
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
//                Toast.makeText(DetailStoreAdminActivity.this, "Store "+store.getNameStore(), Toast.LENGTH_SHORT).show();
                txtNameDetailStore.setText(store.getNameStore());
                txtAddressDetailStore.setText(store.getAddress());
                txtNumberPhoneDetailStore.setText(store.getNumberPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
