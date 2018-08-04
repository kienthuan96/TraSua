package com.example.thuan.thuctap.Adapter.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailStoreAdapter extends ArrayAdapter<MilkTea> {
    private Context context;
    private int layout;
    private ArrayList<MilkTea> arrayList;
    public DetailStoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MilkTea> objects) {
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

        TextView txtNameMilkTea = convertView.findViewById(R.id.txtNameMilkTea_layoutDetailStore);
        TextView txtPriceMilkTea = convertView.findViewById(R.id.txtPriceMilkTea_layoutDetailStore);
        TextView txtDateMilkTea = convertView.findViewById(R.id.txtDateMilkTea_layoutDetailStore);

        MilkTea milkTea = arrayList.get(position);
        txtNameMilkTea.setText(milkTea.getNameMilkTea());
        txtPriceMilkTea.setText(String.format("%,d", milkTea.getPrice()));
        txtDateMilkTea.setText(milkTea.getDateUp());


        return convertView;
    }
}
