package com.example.thuan.thuctap.Activity.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thuan.thuctap.R;

public class MilkTeaActivity extends AppCompatActivity {
    Button btnAdd;
    private String idStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin1);
        getId();
        event();
    }

    private void getId(){
        btnAdd = findViewById(R.id.btnAddMilkTea_admin1);
    }

    private void event(){
        getData();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MilkTeaActivity.this, PostMilkTeaActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("idStore",idStore);
                intent.putExtra("admin",bundle);
                startActivity(intent);
            }
        });
    }

    private void getData(){
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("admin");
        idStore=bundle.getString("idStore");
    }
}
