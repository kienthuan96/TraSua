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

import com.example.thuan.thuctap.Activity.Login.RegisterActivity;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Store;
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
import com.valdesekamdem.library.mdtoast.MDToast;

import java.security.PrivilegedAction;


public class DetailMilkTeaActivity extends AppCompatActivity {
    private String idOrder;
    private String idMilkTea;
    private EditText edtAmount;
    private Button btnOrder;
    private TextView txtNameMilkTea;
    private TextView txtNameStoreMilkTea;
    private TextView txtPriceMilkTea;
    private TextView txtDesMilkTea;
    private MDToast mdToast;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefMilkTea, myRefStore;
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
        txtNameMilkTea = findViewById(R.id.txtNameMilkTea_detailMilkTea);
        txtNameStoreMilkTea = findViewById(R.id.txtStoreMilkTea_detailMilkTea);
        txtPriceMilkTea = findViewById(R.id.txtPriceMilkTea_detailMilkTea);
        txtDesMilkTea = findViewById(R.id.txtDecriptionMilkTea_detailMilkTea);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("user");
        idMilkTea = bundle.getString("milkTea");
        idOrder = bundle.getString("idOrder");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("detailOrder");
        myRefStore = mDatabase.getReference("store");
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRefMilkTea.child(idMilkTea).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                milkTea = dataSnapshot.getValue(MilkTea.class);
                txtNameMilkTea.setText(milkTea.getNameMilkTea());
                txtPriceMilkTea.setText(milkTea.getPrice());
                txtDesMilkTea.setText(milkTea.getDescription());
                myRefStore.child(milkTea.getIdStore()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Store store = dataSnapshot.getValue(Store.class);
                        txtNameStoreMilkTea.setText(store.getNameStore());
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

    private void getEvent() {
        getData();
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkError()) {
                    DetailOrder detailOrder = new DetailOrder();
                    detailOrder.setId(myRef.push().getKey());
                    detailOrder.setAmount(Integer.parseInt(edtAmount.getText().toString()));
                    detailOrder.setIdMilkTea(idMilkTea);
                    detailOrder.setPriceMilkTea(Long.parseLong(milkTea.getPrice()));
                    detailOrder.setIdOrder(idOrder);
                    myRef.child(detailOrder.getId()).setValue(detailOrder)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mdToast = MDToast.makeText(DetailMilkTeaActivity.this, "Đặt số lượng thành công ", 5000, MDToast.TYPE_SUCCESS);
                                    mdToast.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mdToast = MDToast.makeText(DetailMilkTeaActivity.this, "Đặt số lượng thất bại ", 5000, MDToast.TYPE_ERROR);
                            mdToast.show();
                        }
                    });
                }

            }
        });
    }

    private boolean checkError() {
        if (edtAmount.getText().toString().isEmpty())
        {
            mdToast = MDToast.makeText(DetailMilkTeaActivity.this, "Hãy nhập số lượng ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }

        if (Integer.parseInt(edtAmount.getText().toString()) < 0)
        {
            mdToast = MDToast.makeText(DetailMilkTeaActivity.this, "Số lượng không hợp lệ ", 5000, MDToast.TYPE_ERROR);
            mdToast.show();
            return false;
        }
        return true;
    }
}
