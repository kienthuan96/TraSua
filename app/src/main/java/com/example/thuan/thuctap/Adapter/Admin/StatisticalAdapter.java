package com.example.thuan.thuctap.Adapter.Admin;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StatisticalAdapter extends ArrayAdapter<DetailOrder> {

    private Context context;
    private int layout;
    private ArrayList<DetailOrder> arrayList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefUser, myRefMilkTea, myRefOrder;

    public StatisticalAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DetailOrder> objects) {
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

        final TextView txtNameUser = convertView.findViewById(R.id.txtNameUser_statistical);
        final TextView txtNameMilkTea = convertView.findViewById(R.id.txtNameMilkTea_statistical);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount_statistical);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice_statistical);
        TextView txtTotal = convertView.findViewById(R.id.txtTotal_statistical);

        DetailOrder detailOrder = arrayList.get(position);

        mDatabase = FirebaseDatabase.getInstance();
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRefMilkTea.child(detailOrder.getIdMilkTea()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                txtNameMilkTea.setText(milkTea.getNameMilkTea());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtAmount.setText(detailOrder.getAmount().toString() + " x ");
        txtPrice.setText(detailOrder.getPriceMilkTea().toString());
        txtTotal.setText(String.valueOf(detailOrder.getAmount() * detailOrder.getPriceMilkTea()));

        myRefOrder = mDatabase.getReference("order");
        myRefOrder.child(detailOrder.getIdOrder()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                myRefUser= mDatabase.getReference("user");
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

        return convertView;
    }
}