package com.example.thuan.thuctap.Adapter.Shipper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.thuan.thuctap.Activity.Shipper.ShipperActivity;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryRegisterOrderAdapter extends ArrayAdapter<Order>{
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefUser, myRefOrder;

    private String idStore;
    private Context context;
    private int layout;
    private ArrayList<Order> arrayList;

    public HistoryRegisterOrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, parent, false);


        final TextView txtNameUserOrder = convertView.findViewById(R.id.txtNameUser_historyRegisterOrder);
        TextView txtAddressOrder = convertView.findViewById(R.id.txtAddressOrder_historyRegisterOrder);
        TextView txtPriceOrder = convertView.findViewById(R.id.txtPriceOrder_historyRegisterOrder);
        TextView txtDateOrder = convertView.findViewById(R.id.txtDateOrder_historyRegisterOrder);
        TextView txtStatusOrder = convertView.findViewById(R.id.txtStatusOrder_historyRegisterOrder);
        Button btnDeleteOrder = convertView.findViewById(R.id.btnDeleteOrder_historyRegisterOrder);
        final Order order = arrayList.get(position);

        txtAddressOrder.setText(order.getAddressOrder());
        txtPriceOrder.setText(String.format("%,d", order.getPriceOrder()));
        txtDateOrder.setText(order.getDateOrder().toString());
        txtStatusOrder.setText(order.getStatus());

        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        myRefUser = mDatabase.getReference("user");
        myRefUser.child(order.getIdUser()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txtNameUserOrder.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRefOrder.child(order.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("idShipper").setValue("");
                        dataSnapshot.getRef().child("status").setValue("Ready");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return convertView;
    }
}
