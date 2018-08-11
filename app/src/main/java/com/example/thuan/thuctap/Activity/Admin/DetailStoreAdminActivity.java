package com.example.thuan.thuctap.Activity.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Activity.Login.LoginActivity;
import com.example.thuan.thuctap.Adapter.Admin.DetailStoreAdapter;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailStoreAdminActivity extends AppCompatActivity {
    private TextView txtNameDetailStore;
    private TextView txtAddressDetailStore;
    private TextView txtNumberPhoneDetailStore;
    private Button btnDeleteStore,btnEditStore, btnAddMilkTea;
    private ListView lstDetailStore;
    private ProgressDialog progressDialog;
    private DetailStoreAdapter adapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefMilkTea;
    private String nameUser;
    private String idUser;
    private String idStore;
    private Store store;
    private ArrayList<MilkTea> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_store_admin);
        getId();
        getData();
        setData();
        getEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detailstore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuPostMilkTea:
                Intent intent = new Intent(DetailStoreAdminActivity.this, PostMilkTeaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idStore",idStore);
                intent.putExtra("admin",bundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getId(){
        txtNameDetailStore = findViewById(R.id.txtNameStore_detaiAdmin);
        txtAddressDetailStore = findViewById(R.id.txtAddressStore_detaiAdmin);
        txtNumberPhoneDetailStore = findViewById(R.id.txtNumberPhoneStore_detaiAdmin);
        btnDeleteStore = findViewById(R.id.btnDeleteStore_detailAdmin);
        btnEditStore = findViewById(R.id.btnEditStore_detailAdmin);
        lstDetailStore = findViewById(R.id.lstDetailStore_detailAdmin);
    }

    private void setData(){
        txtNameDetailStore.setText(store.getNameStore());
        txtAddressDetailStore.setText(store.getAddress());
        txtNumberPhoneDetailStore.setText(store.getNumberPhone());
    }

    private void getData(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        nameUser=mUser.getDisplayName();
        idUser=mUser.getUid();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("admin");
        idStore = bundle.getString("idStore");
        readData();
    }

    private void readData(){
        arrayList = new ArrayList<>();
        adapter = new DetailStoreAdapter(DetailStoreAdminActivity.this, R.layout.layout_detailstore, arrayList);
        lstDetailStore.setAdapter(adapter);

        store = new Store();
        mDatabase = FirebaseDatabase.getInstance();
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRef = mDatabase.getReference("store");
        myRef.child(idStore).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                store = dataSnapshot.getValue(Store.class);
                txtNameDetailStore.setText(store.getNameStore());
                txtAddressDetailStore.setText(store.getAddress());
                txtNumberPhoneDetailStore.setText(store.getNumberPhone());

                myRefMilkTea.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                        if (milkTea.getIdStore().equals(idStore)) {
                            arrayList.add(milkTea);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Intent intent = new Intent(DetailStoreAdminActivity.this, AdminActivity.class);
                startActivity(intent);
                Toast.makeText(DetailStoreAdminActivity.this, "Khong thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEvent(){
        btnDeleteStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = mDatabase.getReference("store");
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
                Intent intent = new Intent(DetailStoreAdminActivity.this, EditStoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idStore",idStore);
                intent.putExtra("admin",bundle);
                startActivity(intent);
            }
        });

        lstDetailStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DetailStoreAdminActivity.this, MilkTeaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idMilkTea", arrayList.get(i).getId());
                bundle.putString("idStore", idStore);
                intent.putExtra("admin", bundle);
                startActivity(intent);
            }
        });
    }


}
