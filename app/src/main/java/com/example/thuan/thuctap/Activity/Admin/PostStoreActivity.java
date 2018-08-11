package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class PostStoreActivity extends AppCompatActivity {
    private EditText edtNameStore;
    private EditText edtAddressStore;
    private EditText edtNumberStore;
    private ImageView imgStore;

    private Uri uri;
    private InputStream inputStream;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private String idUser;
    private String nameStore;
    private String addressStore;
    private String numberPhoneStore;
    private String idStore;
    private int PICK_IMAGE = 1;
    private MDToast mdToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_store);
        getId();
        event();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSaveStore:
                Intent intentAdmin=new Intent(PostStoreActivity.this,AdminActivity.class);
                saveData();
                finish();
                startActivity(intentAdmin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            try {
                inputStream = PostStoreActivity.this.getContentResolver().openInputStream(data.getData());
                uri=data.getData();
                imgStore.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void getId(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        idUser=mUser.getUid();

        edtNameStore=findViewById(R.id.edtNameStore_postStore);
        edtAddressStore=findViewById(R.id.edtAddressStore_postStore);
        edtNumberStore=findViewById(R.id.edtNumberPhoneStore_postStore);
        imgStore=findViewById(R.id.imgStore_postStore);
    }

    private void getDataFromXml(){
        nameStore=edtNameStore.getText().toString();
        addressStore=edtAddressStore.getText().toString();
        numberPhoneStore=edtNumberStore.getText().toString();

    }

    private void event(){
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
        Calendar calendar=Calendar.getInstance();
        String ten=calendar.getTimeInMillis()+"";

        StorageReference filepath=storageRef.child("IMG_CONTACT").child(ten);
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostStoreActivity.this,"Thêm Thành Công !!!" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostStoreActivity.this,"Thêm Thất Bại !!!" , Toast.LENGTH_SHORT).show();
            }
        });
        return ten;
    }


    private void saveData(){
        getDataFromXml();
        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference();
        ArrayList<MilkTea> arrayList=new ArrayList<>();
        Store store = new Store();
        idStore=myRef.push().getKey();
        store.setId(idStore);
        store.setIdUser(idUser);
        store.setNameStore(nameStore);
        store.setAddress(addressStore);
        store.setNumberPhone(numberPhoneStore);
        store.setImageStore(uploadIMG(uri));
        store.setListMilkTea(arrayList);
        myRef.child("store").child(idStore).setValue(store)
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     mdToast = MDToast.makeText(PostStoreActivity.this, "Thành Cong ", 5000, MDToast.TYPE_SUCCESS);
                     mdToast.show();
                 }
             }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mdToast = MDToast.makeText(PostStoreActivity.this, "That bai ", 5000, MDToast.TYPE_SUCCESS);
                    mdToast.show();
                }
        });
    }

}
