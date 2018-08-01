package com.example.thuan.thuctap.Activity.Shipper;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thuan.thuctap.Adapter.Shipper.HistoryRegisterOrderAdapter;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.User;
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

public class HistoryRegisterOrderActivity extends AppCompatActivity {
    private ListView lstHistoryRegisterOrder;
    private HistoryRegisterOrderAdapter adapter;
    private ArrayList<Order> arrayList;
    private ArrayList<String> arrayListPhone;
    private String idUser;
    private String idOrder;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder, myRefUser;
    private Dialog dialog;
    private Button btnCodeOrder;
    private EditText edtCodeOrder;
    private Integer number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_register_order);
        getId();
        getData();
        event();
    }

    private void getId() {
        lstHistoryRegisterOrder = findViewById(R.id.lstHistoryRegisterOrder_historyRegisterOrder);
    }

    private void getData() {
        arrayListPhone = new ArrayList<>();
        arrayList = new ArrayList<>();
        adapter = new HistoryRegisterOrderAdapter(HistoryRegisterOrderActivity.this, R.layout.layout_historyregisterorder, arrayList);
        lstHistoryRegisterOrder.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        myRefUser = mDatabase.getReference("user");
        myRefOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getIdShipper().equals(idUser))
                {
                    arrayList.add(order);
                    adapter.notifyDataSetChanged();

                    myRefUser.child(order.getIdUser()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            arrayListPhone.add(user.getNumberPhone().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
        dialog = new Dialog(HistoryRegisterOrderActivity.this);
        dialog.setContentView(R.layout.layout_codeorder);
        dialog.setTitle("Nhap ma");
        btnCodeOrder = dialog.findViewById(R.id.btnCodeOrder);
        edtCodeOrder = dialog.findViewById(R.id.edtCodeOrder);

        btnCodeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idOrder.equals(edtCodeOrder.getText().toString())) {
                    dialog.cancel();
                    myRefOrder.child(idOrder).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("status").setValue("Done");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    myRefUser.child(arrayList.get(number).getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            Toast.makeText(HistoryRegisterOrderActivity.this, ""+arrayList.get(number).getPointOrder(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(HistoryRegisterOrderActivity.this, ""+user.getPoint(), Toast.LENGTH_SHORT).show();
                            dataSnapshot.getRef().child("point").setValue(user.getPoint() + arrayList.get(number).getPointOrder());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        lstHistoryRegisterOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idOrder = arrayList.get(i).getId();
                number = i;
                if (arrayList.get(i).getStatus().equals("Proccess")) {
                    dialog.show();
                }
            }
        });

        lstHistoryRegisterOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", arrayListPhone.get(i), null)));
                return false;
            }
        });

    }
}
