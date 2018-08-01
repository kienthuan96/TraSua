package com.example.thuan.thuctap.Activity.User;

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
import android.widget.Toast;

import com.example.thuan.thuctap.Activity.Admin.MilkTeaActivity;
import com.example.thuan.thuctap.Activity.Admin.PostMilkTeaActivity;
import com.example.thuan.thuctap.Adapter.User.UserAdapter;
import com.example.thuan.thuctap.Model.MilkTea;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private ListView lstMilkTea;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef, myRefOrder;
    private UserAdapter userAdapter;
    private ArrayList<MilkTea> arrayListMilkTea;
    private String idUser;
    private String idOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getId();
        setData();
        event();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuOrder:
                Intent intent = new Intent(UserActivity.this, OrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", idOrder);
                intent.putExtra("user", bundle);
                finish();
                startActivity(intent);
                return true;
            case R.id.menuHistoryOrder:
                Intent intentHistory = new Intent(UserActivity.this, HistoryOrderActivity.class);
                startActivity(intentHistory);
                return true;
            case R.id.menuInfoUser:
                Intent intentInfo = new Intent(UserActivity.this, EditUserActivity.class);
                startActivity(intentInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getId() {
        lstMilkTea = findViewById(R.id.lstMilkTea_user);

        arrayListMilkTea = new ArrayList<>();
    }

    private void getData(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        idUser = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRefOrder = mDatabase.getReference("order");
        idOrder = myRefOrder.push().getKey();
        Toast.makeText(this, ""+ idOrder, Toast.LENGTH_SHORT).show();
        myRef=mDatabase.getReference("milkTea");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MilkTea milkTea = dataSnapshot.getValue(MilkTea.class);
                arrayListMilkTea.add(milkTea);
                userAdapter.notifyDataSetChanged();
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

    private void setData() {
        getData();

        userAdapter = new UserAdapter(UserActivity.this, R.layout.layout_user, arrayListMilkTea);
        lstMilkTea.setAdapter(userAdapter);
    }

    private void event(){
        lstMilkTea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(UserActivity.this, DetailMilkTeaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", idOrder);
                bundle.putString("milkTea", arrayListMilkTea.get(i).getId());
                intent.putExtra("user", bundle);
                startActivity(intent);
            }
        });
    }
}
