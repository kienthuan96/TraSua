package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thuan.thuctap.Activity.Login.LoginActivity;
import com.example.thuan.thuctap.Activity.Shipper.ShipperActivity;
import com.example.thuan.thuctap.Adapter.Admin.AdminAdapter;
import com.example.thuan.thuctap.Model.Store;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private TextView txtNameUser;
    private ListView lstStore;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private MDToast mdToast;
    private String nameUser;
    private String idUser;
    private ArrayList<Store> arrStore;
    private AdminAdapter adminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
//        checkLogin();
        getId();
        setData();
        eventListView();
    }

    /**
     * kiem tra da dang nhap user chua
     */
    private void checkLogin(){
        mAuth= FirebaseAuth.getInstance();
        if (mAuth!=null){
            mUser=mAuth.getCurrentUser();
            mdToast = MDToast.makeText(AdminActivity.this, "Da dang nhap ", 5000, MDToast.TYPE_SUCCESS);
            mdToast.show();
        }else{
            Intent intent= new Intent(AdminActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogOut:
                Intent intentLogin = new Intent(AdminActivity.this, LoginActivity.class);
                mAuth.signOut();
                finish();
                startActivity(intentLogin);
                return true;
            case R.id.menuPost:
                Intent intentPost=new Intent(AdminActivity.this,PostStoreActivity.class);
                startActivity(intentPost);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getId(){
//        txtNameUser=findViewById(R.id.txtNameUser_admin);
        lstStore=findViewById(R.id.lstStore_admin);
    }

    private void getData(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
//        nameUser=mUser.getDisplayName();
        idUser = mUser.getUid();
        readDatạ();
    }
    private void setData(){
        getData();
       // txtNameUser.setText(nameUser);
        adminAdapter=new AdminAdapter(AdminActivity.this,R.layout.layout_admin,arrStore);
        lstStore.setAdapter(adminAdapter);
    }


    private void readDatạ()
    {
        arrStore=new ArrayList<>();
        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference("store");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Store store=dataSnapshot.getValue(Store.class);
                if (store.getIdUser().equals(idUser)){
                    arrStore.add(store);
                }
                adminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void eventListView(){
//        lstStore.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AdminActivity.this, "SS", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        lstStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(AdminActivity.this, ""+arrStore.get(i).getNameStore(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminActivity.this, DetailStoreAdminActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idStore",arrStore.get(i).getId());
                intent.putExtra("admin",bundle);
                startActivity(intent);
            }
        });
    }
}
