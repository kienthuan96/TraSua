package com.example.thuan.thuctap.Adapter.User;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thuan.thuctap.Model.DetailOrder;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<DetailOrder>{
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefMilkTea, myRefStore;

    private String idStore;
    private Context context;
    private int layout;
    private ArrayList<DetailOrder> arrayList;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DetailOrder> objects) {
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


        final TextView txtNameMilkTea = convertView.findViewById(R.id.txtNameMilkTea_detailOrder);
        final TextView txtNameStore = convertView.findViewById(R.id.txtNameStore_detailOrder);
        TextView txtPrice = convertView.findViewById(R.id.txtPriceMilkTea_detailOrder);
        TextView txtAmount = convertView.findViewById(R.id.txtAmount_detailOrder);
        ImageView imgMilkTea = convertView.findViewById(R.id.imgMilkTea_user);

        DetailOrder detailOrder = arrayList.get(position);

        txtAmount.setText(detailOrder.getAmount().toString());
        txtPrice.setText(String.valueOf(detailOrder.getAmount() * detailOrder.getPriceMilkTea()));

        mDatabase = FirebaseDatabase.getInstance();
        myRefStore = mDatabase.getReference("store");
        myRefMilkTea = mDatabase.getReference("milkTea");
        myRefMilkTea.child(detailOrder.getIdMilkTea()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                txtNameMilkTea.setText(milkTea.getNameMilkTea());
                idStore = milkTea.getIdStore();
                myRefStore.child(idStore).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Store store = dataSnapshot.getValue(Store.class);
                        txtNameStore.setText(store.getNameStore());
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