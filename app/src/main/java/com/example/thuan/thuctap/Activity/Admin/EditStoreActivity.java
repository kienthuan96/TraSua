package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class EditStoreActivity extends AppCompatActivity {
    private EditText edtNameStore;
    private EditText edtAddressStore;
    private EditText edtNumberStore;
    private ImageView imgStore;
    private Button btnSave;

    private Uri uri;
    private InputStream inputStream;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private int PICK_IMAGE = 1;
    private String idStore;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);
        getId();
        setData();
        event();
    }

    public void getId(){
        edtNameStore=findViewById(R.id.edtNameStore_editStore);
        edtAddressStore=findViewById(R.id.edtAddressStore_editStore);
        edtNumberStore=findViewById(R.id.edtNumberPhoneStore_editStore);
        imgStore=findViewById(R.id.imgStore_editStore);
        btnSave=findViewById(R.id.btnSave_editStore);
    }

    private void setData(){
        getData();
    }

    private void getData(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("admin");
        idStore=bundle.getString("idStore");
        readData();
    }

    private void readData(){
        store=new Store();
        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference("store");
        myRef.child(idStore).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                store=dataSnapshot.getValue(Store.class);
                edtNameStore.setText(store.getNameStore());
                edtAddressStore.setText(store.getAddress());
                edtNumberStore.setText(store.getNumberPhone());

                StorageReference pathReference = storageRef.child("IMG_CONTACT/"+store.getImageStore());
//                Glide.with(EditStoreActivity.this).using(new FirebaseImageLoader()).load(pathReference).into(imgStore);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditStoreActivity.this, "Khong thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void event(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEdit();
            }
        });

        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });
    }

    private void saveEdit(){
        final String nameEdit=edtNameStore.getText().toString();
        final String addressEdit=edtAddressStore.getText().toString();
        final String numberEdit=edtNumberStore.getText().toString();

        myRef.child(idStore).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    dataSnapshot.getRef().child("nameStore").setValue(nameEdit);
                    dataSnapshot.getRef().child("address").setValue(addressEdit);
                    dataSnapshot.getRef().child("numberPhone").setValue(numberEdit);
                    Toast.makeText(EditStoreActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
