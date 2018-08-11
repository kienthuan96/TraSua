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
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminAdapter extends ArrayAdapter<Store> {
    private Context context;
    private int layout;
    private ArrayList<Store> arrayList;
    public AdminAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Store> objects) {
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

        TextView txtNameStore=convertView.findViewById(R.id.txtNameStore_admin);
        TextView txtAddressStore=convertView.findViewById(R.id.txtAddressStore_admin);
        final ImageView imgStore=convertView.findViewById(R.id.imgStore_admin);

        Store store=arrayList.get(position);
        txtNameStore.setText(store.getNameStore());
        txtAddressStore.setText(store.getAddress());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("IMG_CONTACT/"+store.getImageStore());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageURL = uri.toString();
                Glide.with(context).load(imageURL).into(imgStore);
            }
        });

        return convertView;
    }
}
