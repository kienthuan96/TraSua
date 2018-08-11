package com.example.thuan.thuctap.Activity.Shipper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thuan.thuctap.Activity.Login.LoginActivity;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailOrderActivity extends AppCompatActivity {
    private TextView txtNameUser;
    private TextView txtDateOrder;
    private TextView txtPriceOrder;
    private TextView txtAddressOrder;
    private ListView lstDetailOrder;
    private Button btnRegisterOrder;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder, myRefUser;
    private String idUser;
    private String idOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        getId();
        getData();
        event();
    }

    private void getId() {
        txtNameUser = findViewById(R.id.txtNameUser_detailOrder);
        txtDateOrder = findViewById(R.id.txtDateOrder_detaiOrder);
        txtPriceOrder = findViewById(R.id.txtPriceOrder_detailOrder);
        txtAddressOrder = findViewById(R.id.txtAddressOrder_detaiOrder);
        lstDetailOrder = findViewById(R.id.lstDetaiOrder_detailOrder);
        btnRegisterOrder = findViewById(R.id.btnRegisterOrder_detailOrder);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("shipper");
        idOrder = bundle.getString("idOrder");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance();
        myRefUser = mDatabase.getReference("user");
        myRefOrder = mDatabase.getReference("order");
        myRefOrder.child(idOrder).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                txtAddressOrder.setText(order.getAddressOrder());
                txtDateOrder.setText(order.getDateOrder());
                txtPriceOrder.setText(order.getPriceOrder().toString());
                myRefUser.child(order.getIdUser()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        txtNameUser.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void event() {
        btnRegisterOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRefOrder.child(idOrder).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            dataSnapshot.getRef().child("status").setValue("Proccess");
                            dataSnapshot.getRef().child("idShipper").setValue(idUser);
                            transShipper();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void transShipper(){
        Intent intent=new Intent(DetailOrderActivity.this, ShipperActivity.class);
        finish();
        startActivity(intent);
    }
}
