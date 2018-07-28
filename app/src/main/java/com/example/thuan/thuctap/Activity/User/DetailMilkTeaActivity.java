package com.example.thuan.thuctap.Activity.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivilegedAction;


public class DetailMilkTeaActivity extends AppCompatActivity {
    private String idOrder;
    private String idMilkTea;
    private EditText edtAmount;
    private Button btnOrder;
    private TextView txtNameMilkTea;
    private TextView txtNameStoreMilkTea;
    private TextView txtPriceMilkTea;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefMilkTea;
    private MilkTea milkTea = new MilkTea();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_milk_tea);
        getId();
        getEvent();
    }

    private void getId() {
        btnOrder = findViewById(R.id.btnOrder_detailMilkTea);
        edtAmount = findViewById(R.id.edtAmountMilkTea_detailMilkTea);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        idMilkTea = bundle.getString("milkTea");
        idOrder = bundle.getString("idOrder");
        Toast.makeText(this, ""+idOrder, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("detailOrder");
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRefMilkTea.child(idMilkTea).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                milkTea = dataSnapshot.getValue(MilkTea.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getEvent() {
        getData();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailOrder detailOrder = new DetailOrder();
                detailOrder.setId(myRef.push().getKey());
                detailOrder.setAmount(Integer.parseInt(edtAmount.getText().toString()));
                detailOrder.setIdMilkTea(idMilkTea);
                detailOrder.setPriceMilkTea(Long.parseLong(milkTea.getPrice()));
                detailOrder.setIdOrder(idOrder);
//                Toast.makeText(DetailMilkTeaActivity.this, milkTea.getPrice(), Toast.LENGTH_SHORT).show();
                myRef.child(detailOrder.getId()).setValue(detailOrder)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailMilkTeaActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailMilkTeaActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
