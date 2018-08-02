package com.example.thuan.thuctap.Activity.Shipper;

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
import android.widget.ListView;

import com.example.thuan.thuctap.Activity.User.OrderActivity;
import com.example.thuan.thuctap.Activity.User.UserActivity;
import com.example.thuan.thuctap.Adapter.Shipper.ShipperAdapter;
import com.example.thuan.thuctap.Adapter.User.OrderAdapter;
import com.example.thuan.thuctap.Model.DetailOrder;
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

public class ShipperActivity extends AppCompatActivity {
    private ListView lstOrder;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder;
    private String idUser;
    private ArrayList<Order> arrayList;
    private ShipperAdapter shipperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipper);
        getId();
        getData();
        event();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shipperAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shipper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuHistoryRegisterOrder:
                Intent intent = new Intent(ShipperActivity.this, HistoryRegisterOrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuRating:
                Intent intentRating = new Intent(ShipperActivity.this, RatingShipperActivity.class);
                startActivity(intentRating);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getId() {
        lstOrder = findViewById(R.id.lstOrder_shipper);
    }

    private void getData() {
        arrayList = new ArrayList<>();
        shipperAdapter = new ShipperAdapter(ShipperActivity.this, R.layout.layout_shipper, arrayList);
        lstOrder.setAdapter(shipperAdapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        myRefOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getStatus().equals("Ready")) {
                    arrayList.add(order);
                    shipperAdapter.notifyDataSetChanged();
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
        lstOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ShipperActivity.this, DetailOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", arrayList.get(i).getId());
                intent.putExtra("shipper", bundle);
                startActivity(intent);
            }
        });
    }
}
