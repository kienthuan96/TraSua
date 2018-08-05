package com.example.thuan.thuctap.Adapter.User;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<MilkTea> {
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;

    private Context context;
    private int layout;
    private ArrayList<MilkTea> arrayList;

    public UserAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MilkTea> objects) {
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

        TextView txtNameMilkTea = convertView.findViewById(R.id.txtNameMilkTea_user);
        TextView txtPriceMilkTea = convertView.findViewById(R.id.txtPriceMilkTea_user);
        TextView txtStatusMilkTea = convertView.findViewById(R.id.txtStatusMilkTea_user);
        final TextView txtNameStoreMilkTea = convertView.findViewById(R.id.txtStoreMilkTea_user);
        final ImageView imgMilkTea = convertView.findViewById(R.id.imgMilkTea_user);

        MilkTea milkTea = arrayList.get(position);
        txtNameMilkTea.setText(milkTea.getNameMilkTea());
        txtPriceMilkTea.setText(milkTea.getPrice());
        txtStatusMilkTea.setText(milkTea.getStatus());

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("store");
        myRef.child(milkTea.getIdStore()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                txtNameStoreMilkTea.setText(store.getNameStore());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("IMG_CONTACT/"+milkTea.getImageMilkTea());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context).load(imageURL).into(imgMilkTea);
            }
        });

        return convertView;
    }
}
