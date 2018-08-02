package com.example.thuan.thuctap.Adapter.Shipper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thuan.thuctap.Model.Order;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RatingShipperAdapter extends ArrayAdapter<User>{
    private Context context;
    private int layout;
    private ArrayList<User> arrayList;

    public RatingShipperAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
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

        TextView txtNameShipper = convertView.findViewById(R.id.txtName_ratingShipper);
        TextView txtPointShipper = convertView.findViewById(R.id.txtPoint_ratingShipper);
        final User user = arrayList.get(position);

        txtNameShipper.setText(user.getName());
        txtPointShipper.setText(user.getPoint().toString());


        return convertView;
    }
}
