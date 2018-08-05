package com.example.thuan.thuctap.Activity.User;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.thuan.thuctap.Activity.Shipper.HistoryRegisterOrderActivity;
import com.example.thuan.thuctap.Adapter.User.HistoryOrderAdapter;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrderActivity extends AppCompatActivity {
    private ArrayList<Order> arrayList;
    private HistoryOrderAdapter adapter;
    private ListView lstHistoryOrder;
    private String idUser;
    private Integer number;
    private Integer rate;
    private Dialog dialog;
    private RatingBar rtbOrder;
    private Button btnRating;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        getId();
        getData();
        event();
    }

    private void getId() {
        lstHistoryOrder = findViewById(R.id.lstHistoryOrder_historyOrder);
    }

    private void getData() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();

        arrayList = new ArrayList<>();
        adapter = new HistoryOrderAdapter(HistoryOrderActivity.this, R.layout.layout_historyorder, arrayList);
        lstHistoryOrder.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        myRefOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getIdUser().equals(idUser) ) {
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

    private void event() {
        dialog = new Dialog(HistoryOrderActivity.this);
        dialog.setContentView(R.layout.layout_rating);
        dialog.setTitle("Đánh giá");

        rtbOrder = dialog.findViewById(R.id.rtbOrder);
        btnRating = dialog.findViewById(R.id.btnRating);

        rtbOrder.setRating(3);
        lstHistoryOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).getStatus().equals("Done")) {
                    dialog.show();
                    number = i;
                }
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = Math.round(rtbOrder.getRating());
                myRefOrder.child(arrayList.get(number).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Toast.makeText(HistoryOrderActivity.this, ""+rtbOrder.getRating(), Toast.LENGTH_SHORT).show();
                        dataSnapshot.getRef().child("rate").setValue(rate);
                        dialog.cancel();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
