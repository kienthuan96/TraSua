package com.example.thuan.thuctap.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thuan.thuctap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valdesekamdem.library.mdtoast.MDToast;

public class AdminActivity extends AppCompatActivity {
    private TextView txtNameUser;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MDToast mdToast;
    private String nameUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        checkLogin();
        getId();
        setData();
    }

    /**
     * kiem tra da dang nhap user chua
     */
    private void checkLogin(){
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if (mAuth!=null){
            mdToast = MDToast.makeText(AdminActivity.this, "Da dang nhap "+mUser.getDisplayName(), 5000, MDToast.TYPE_SUCCESS);
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
                Intent intentLogOut=new Intent(AdminActivity.this,LoginActivity.class);
                mAuth.signOut();
                startActivity(intentLogOut);
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
        txtNameUser=findViewById(R.id.txtNameUser_admin);
    }

    private void getData(){
        nameUser=mUser.getDisplayName();
    }
    private void setData(){
        getData();
        txtNameUser.setText(nameUser);
    }
}
