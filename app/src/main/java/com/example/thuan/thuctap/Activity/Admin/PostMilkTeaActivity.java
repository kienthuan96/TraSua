package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PostMilkTeaActivity extends AppCompatActivity {
    private EditText edtNameMilkTea;
    private EditText edtPriceMilkTea;
    private EditText edtDescribeMilkTea;
    private ImageView imgMilkTea;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private String idUser;
    private String idStore;
    private int PICK_IMAGE = 1;
    private Uri uri;
    private InputStream inputStream;
    private String idMilkTea;
    private String nameMilkTea;
    private String priceMikTea;
    private String descriptionMilTea;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_milk_tea);
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
                Intent intentAdmin=new Intent(PostMilkTeaActivity.this,MilkTeaActivity.class);
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
                inputStream = PostMilkTeaActivity.this.getContentResolver().openInputStream(data.getData());
                uri=data.getData();
                imgMilkTea.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String uploadIMG(Uri uri){
        Calendar calendar=Calendar.getInstance();
        String ten=calendar.getTimeInMillis()+"";

        StorageReference filepath=storageRef.child("IMG_CONTACT").child(ten);
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostMilkTeaActivity.this,"Thêm Thành Công !!!" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostMilkTeaActivity.this,"Thêm Thất Bại !!!" , Toast.LENGTH_SHORT).show();
            }
        });
        return ten;
    }

    private void event(){
        imgMilkTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });
    }

    private void getId(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        idUser=mUser.getUid();

        edtNameMilkTea = findViewById(R.id.edtNameMilkTea_postMilkTea);
        edtPriceMilkTea = findViewById(R.id.edtPriceMilkTea_postMilkTea);
        edtDescribeMilkTea = findViewById(R.id.edtDescribeMilkTea_postMilkTea);
        imgMilkTea = findViewById(R.id.imgMilkTea_postMilkTea);
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("admin");
        idStore = bundle.getString("idStore");
//        Toast.makeText(this, ""+idStore, Toast.LENGTH_SHORT).show();

        nameMilkTea = edtNameMilkTea.getText().toString();
        priceMikTea = edtPriceMilkTea.getText().toString();
        descriptionMilTea = edtDescribeMilkTea.getText().toString();
    }

    private void saveData(){
        getData();
        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference();
        MilkTea milkTea = new MilkTea();
        idMilkTea=myRef.push().getKey();
        milkTea.setId(idMilkTea);
        milkTea.setIdUser(idUser);
        milkTea.setIdStore(idStore);
        milkTea.setNameMilkTea(nameMilkTea);
        milkTea.setDateUp(getDateTime());
        milkTea.setPrice(priceMikTea);
        milkTea.setImageMilkTea(uploadIMG(uri));
        milkTea.setDescription(descriptionMilTea);
        milkTea.setStatus("Ready"); //Ready, Sell, End
        Toast.makeText(this, ""+milkTea.getIdStore()+milkTea.getNameMilkTea(), Toast.LENGTH_SHORT).show();
//        myRef.child("milkTea").child(idMilkTea).setValue(milkTea)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(PostMilkTeaActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(PostMilkTeaActivity.this, "That bai", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
