package com.example.thuan.thuctap.Activity.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Adapter.User.OrderAdapter;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.Order;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {
    private String idOrder;
    private String idUser;
    private ArrayList<DetailOrder> arrayList;
    private OrderAdapter orderAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefOrder;
    private TextView txtNameUser;
    private TextView txtPoint;
    private EditText edtAddressOrder;
    private ListView lstMilkTeaOrder;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getId();
        getData();
        event();
    }

    private void getId() {
        txtNameUser = findViewById(R.id.txtNameUser_order);
        txtPoint = findViewById(R.id.txtPoint_order);
        edtAddressOrder = findViewById(R.id.edtAddressOrder_order);
        lstMilkTeaOrder = findViewById(R.id.lstMilkTeaOrder_order);
        btnOrder = findViewById(R.id.btnOrder_order);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        idOrder = bundle.getString("idOrder");
        Toast.makeText(this, ""+idOrder, Toast.LENGTH_SHORT).show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();

        arrayList = new ArrayList<>();
        orderAdapter = new OrderAdapter(OrderActivity.this, R.layout.layout_detailorder, arrayList);
        lstMilkTeaOrder.setAdapter(orderAdapter);
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("detailOrder");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DetailOrder detailOrder = dataSnapshot.getValue(DetailOrder.class);
                if (detailOrder.getIdOrder().equals(idOrder)) {
                    arrayList.add(detailOrder);
                    orderAdapter.notifyDataSetChanged();
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
        myRefOrder = mDatabase.getReference("order");
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.setId(myRefOrder.push().getKey());
                order.setArrayList(arrayList);
                order.setIdUser(idUser);
                order.setAddressOrder(edtAddressOrder.getText().toString());
                order.setDateOrder(getDateTime());
                order.setPriceOrder(priceOrder());
                order.setPointOrder(pointOrder());
                myRefOrder.child(order.getId()).setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OrderActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private Long priceOrder() {
        Long price = 0L;
        for (int i = 0; i < arrayList.size(); i++ ) {
            price += arrayList.get(i).getPriceMilkTea();
        }
        return price;
    }

    private Integer pointOrder() {
        if (priceOrder() > 50000) {
            return 2;
        }
        if (priceOrder() > 100000) {
            return 5;
        }
        if (priceOrder() > 200000) {
            return 10;
        }
        return 0;
    }
}
