package com.example.thuan.thuctap.Activity.Admin;

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

import com.example.thuan.thuctap.Activity.Event.MyEditTextDatePicker;
import com.example.thuan.thuctap.Adapter.Admin.StatisticalAdapter;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticalActivity extends AppCompatActivity {
    private EditText edtDate;
    private ListView lstOrder;
    private Button btnSearch;
    private TextView txtAmountOrder;
    private TextView txtTotal;

    private ArrayList<DetailOrder> arrayList;
    private StatisticalAdapter adapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefOrder, myRefMilkTea, myRefStore;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Date dateClick;
    private String idUser;
    private Integer amount = 0;
    private Long total = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);

        getId();
//        getData();
        event();
    }

    private void getId() {
        edtDate = findViewById(R.id.edtDate_statistical);
        lstOrder = findViewById(R.id.lstStatistical_statistical);
        btnSearch = findViewById(R.id.btnSearch_statistical);
        txtAmountOrder = findViewById(R.id.txtAmountOrder_statistical);
        txtTotal = findViewById(R.id.txtTotalOrder_statistical);
    }

    private void getData(final Date dateClick) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();

        arrayList = new ArrayList<>();
        adapter = new StatisticalAdapter(StatisticalActivity.this, R.layout.layout_statistical, arrayList);
        lstOrder.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance();
        myRefStore = mDatabase.getReference("store");
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRefOrder = mDatabase.getReference("order");
        myRefOrder.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Order order = dataSnapshot.getValue(Order.class);
                try {
                    Date date = new SimpleDateFormat("yyyy/MM/dd").parse(order.getDateOrder());
                    if (date.compareTo(dateClick) == 0 ) {
                        final ArrayList<DetailOrder> arrayListDetailOrder = order.getArrayList();
                        for (int i=0; i<arrayListDetailOrder.size(); i++) {
                            final int finalI = i;
                            myRefMilkTea.child(arrayListDetailOrder.get(i).getIdMilkTea()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                                    myRefStore.child(milkTea.getIdStore()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Store store = dataSnapshot.getValue(Store.class);
                                            if (store.getIdUser().equals(idUser)) {
                                                amount += 1;
                                                total += order.getPriceOrder();
                                                txtAmountOrder.setText(amount.toString());
                                                txtTotal.setText(String.format("%,d", total));

                                                arrayList.add(arrayListDetailOrder.get(finalI));
                                                adapter.notifyDataSetChanged();
                                            }
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


                    }
                } catch (ParseException e) {
                    e.printStackTrace();
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
        MyEditTextDatePicker myEditTextDatePicker = new MyEditTextDatePicker(StatisticalActivity.this, R.id.edtDate_statistical);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StatisticalActivity.this, ""+edtDate.getText(), Toast.LENGTH_SHORT).show();
                try {
                    dateClick = new SimpleDateFormat("yyyy/MM/dd").parse(edtDate.getText().toString());
                    getData(dateClick);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}

