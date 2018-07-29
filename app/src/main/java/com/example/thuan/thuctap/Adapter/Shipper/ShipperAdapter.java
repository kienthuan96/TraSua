package com.example.thuan.thuctap.Adapter.Shipper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShipperAdapter extends ArrayAdapter<Order> {
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefUser;

    private String idStore;
    private Context context;
    private int layout;
    private ArrayList<Order> arrayList;

    public ShipperAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
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


        final TextView txtNameUserOrder = convertView.findViewById(R.id.txtNameUserOrder_shipper);
        TextView txtAddressOrder = convertView.findViewById(R.id.txtAddressOrder_shipper);
        TextView txtPriceOrder = convertView.findViewById(R.id.txtPriceOrder_shipper);
        TextView txtDateOrder = convertView.findViewById(R.id.txtDateOrder_shipper);
        final Order order = arrayList.get(position);

        txtAddressOrder.setText(order.getAddressOrder());
        txtPriceOrder.setText(order.getPriceOrder().toString());
        txtDateOrder.setText(order.getDateOrder().toString());


        mDatabase = FirebaseDatabase.getInstance();
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

        return convertView;
    }
}
