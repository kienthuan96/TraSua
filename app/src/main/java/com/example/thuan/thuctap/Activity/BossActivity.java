package com.example.thuan.thuctap.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.thuan.thuctap.Activity.Admin.AdminActivity;
import com.example.thuan.thuctap.Adapter.Admin.AdminAdapter;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BossActivity extends AppCompatActivity {
    private ListView lstStore;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ArrayList<Store> arrStore;
    private AdminAdapter adminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);
        getId();
        getData();
    }

    private void getId() {
        lstStore = findViewById(R.id.lstStore_boss);
    }

    private void getData() {
        arrStore = new ArrayList<>();
        adminAdapter=new AdminAdapter(BossActivity.this,R.layout.layout_admin,arrStore);
        lstStore.setAdapter(adminAdapter);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("store");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Store store = dataSnapshot.getValue(Store.class);
                arrStore.add(store);
                adminAdapter.notifyDataSetChanged();
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
}
