package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

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
    private String nameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store);
        getId();
        setData();
        event();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            try {
                inputStream = EditStoreActivity.this.getContentResolver().openInputStream(data.getData());
                uri = data.getData();
                imgStore.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

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
        store = new Store();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("store");
        myRef.child(idStore).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                store = dataSnapshot.getValue(Store.class);
                edtNameStore.setText(store.getNameStore());
                edtAddressStore.setText(store.getAddress());
                edtNumberStore.setText(store.getNumberPhone());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference pathReference = storageRef.child("IMG_CONTACT/"+store.getImageStore());
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        Glide.with(EditStoreActivity.this).load(imageURL).into(imgStore);
                    }
                });
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
                transAdmin();
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

    public String uploadIMG(Uri uri){
        Calendar calendar = Calendar.getInstance();
        nameImage = calendar.getTimeInMillis()+"";

        StorageReference filepath = storageRef.child("IMG_CONTACT").child(nameImage);
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditStoreActivity.this,"Thêm Thành Công !!!" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditStoreActivity.this,"Thêm Thất Bại !!!" , Toast.LENGTH_SHORT).show();
            }
        });
        return nameImage;
    }

    private void transAdmin() {
        Intent intent = new Intent(EditStoreActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private void saveEdit(){
        final String nameEdit = edtNameStore.getText().toString();
        final String addressEdit = edtAddressStore.getText().toString();
        final String numberEdit = edtNumberStore.getText().toString();

        myRef.child(idStore).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    dataSnapshot.getRef().child("nameStore").setValue(nameEdit);
                    dataSnapshot.getRef().child("address").setValue(addressEdit);
                    dataSnapshot.getRef().child("numberPhone").setValue(numberEdit);
                    dataSnapshot.getRef().child("imageMilkTea").setValue(uploadIMG(uri));
//                    uploadIMG(uri);
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
