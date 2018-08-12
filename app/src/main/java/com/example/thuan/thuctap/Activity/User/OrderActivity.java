package com.example.thuan.thuctap.Activity.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Adapter.User.OrderAdapter;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.User;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {
    private String idOrder;
    private String idUser;
    private ArrayList<DetailOrder> arrayList;
    private OrderAdapter orderAdapter;
    private Long price = 0L;
    private Integer point = 0;
    private ArrayList<String> arrVoucher;
    private ArrayAdapter voucherAdapter;
    private String voucher;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefOrder, myRefUser;
    private TextView txtNameUser;
    private TextView txtPoint;
    private EditText edtAddressOrder;
    private ListView lstMilkTeaOrder;
    private Button btnOrder;
    private Spinner spinner;

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
        spinner = findViewById(R.id.spnVoucher_order);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        idOrder = bundle.getString("idOrder");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();

        arrVoucher = new ArrayList<>();
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
                    price += detailOrder.getPriceMilkTea() * detailOrder.getAmount();
                    if (price >= 50000) {
                        point = 2;
                    }
                    if (price >= 100000) {
                        point = 5;
                    }
                    if (price >= 200000) {
                        point = 10;
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefUser = mDatabase.getReference("user");
        myRefUser.child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtNameUser.setText(user.getName());
                arrVoucher.add("none");
                if (user.getPoint() >= 20) {
                    arrVoucher.add("30.000");
                }
                if (user.getPoint() >= 50) {
                    arrVoucher.add("50.000");
                }
                if (user.getPoint() >= 100) {
                    arrVoucher.add("150.000");
                }
                voucherAdapter = new ArrayAdapter(OrderActivity.this, android.R.layout.simple_spinner_item, arrVoucher);
                spinner.setDropDownHorizontalOffset(android.R.layout.simple_list_item_single_choice);
                spinner.setAdapter(voucherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void event() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                voucher = arrVoucher.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                voucher = arrVoucher.get(0);
            }
        });
        myRefOrder = mDatabase.getReference("order");
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voucher.equals("30.000")) {
                    price = Math.abs(price - 30000);
                    myRefUser.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            dataSnapshot.getRef().child("point").setValue(user.getPoint() - 20);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if (voucher.equals("50.000")) {
                    price = Math.abs(price - 50000);
                    myRefUser.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            dataSnapshot.getRef().child("point").setValue(user.getPoint() - 50);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if (voucher.equals("150.000")) {
                    price = Math.abs(price - 150000);
                    myRefUser.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            dataSnapshot.getRef().child("point").setValue(user.getPoint() - 100);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Order order = new Order();
                order.setId(idOrder);
                order.setArrayList(arrayList);
                order.setIdUser(idUser);
                order.setIdShipper("");
                order.setAddressOrder(edtAddressOrder.getText().toString());
                order.setDateOrder(getDateTime());
                order.setPriceOrder(price);
                order.setPointOrder(point);
                order.setRate(3);
                order.setStatus("Ready"); //Ready Proccess Done

                myRefOrder.child(order.getId()).setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OrderActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OrderActivity.this, UserActivity.class);
                                finish();
                                startActivity(intent);
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
        if (priceOrder() >= 50000) {
            return 2;
        }
        if (priceOrder() >= 100000) {
            return 5;
        }
        if (priceOrder() >= 200000) {
            return 10;
        }
        return 0;
    }
}
