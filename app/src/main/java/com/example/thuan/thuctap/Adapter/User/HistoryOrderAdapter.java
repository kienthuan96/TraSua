package com.example.thuan.thuctap.Adapter.User;

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
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrderAdapter extends ArrayAdapter<Order> {
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRefMilkTea, myRefStore, myRefDetailOrder;

    private String idStore;
    private Context context;
    private int layout;
    private ArrayList<Order> arrayList;

    public HistoryOrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> objects) {
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

        TextView txtPrice = convertView.findViewById(R.id.txtPriceOrder_historyOrder);
        TextView txtAddress = convertView.findViewById(R.id.txtAddressOrder_historyOrder);
        TextView txtDate = convertView.findViewById(R.id.txtDateOrder_historyOrder);
        TextView txtCode = convertView.findViewById(R.id.txtCodeOrder_historyOrder);
        TextView txtStatus = convertView.findViewById(R.id.txtStatusOrder_historyOrder);


        final Order order = arrayList.get(position);

        txtPrice.setText(order.getPriceOrder().toString());
        txtAddress.setText(order.getAddressOrder());
        txtDate.setText(order.getDateOrder());
        txtCode.setText(order.getId());
        txtStatus.setText(order.getStatus());

        return convertView;
    }
}
