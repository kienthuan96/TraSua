package com.example.thuan.thuctap.Activity.Shipper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.thuan.thuctap.Adapter.Shipper.HistoryRegisterOrderAdapter;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryRegisterOrderActivity extends AppCompatActivity {
    private ListView lstHistoryRegisterOrder;
    private HistoryRegisterOrderAdapter adapter;
    private ArrayList<Order> arrayList;
    private String idUser;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_register_order);
        getId();
        getData();
    }

    private void getId() {
        lstHistoryRegisterOrder = findViewById(R.id.lstHistoryRegisterOrder_historyRegisterOrder);
    }

    private void getData() {
        arrayList = new ArrayList<>();
        adapter = new HistoryRegisterOrderAdapter(HistoryRegisterOrderActivity.this, R.layout.layout_historyregisterorder, arrayList);
        lstHistoryRegisterOrder.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        myRefOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getIdShipper().equals(idUser))
                {
                    arrayList.add(order);
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
}
