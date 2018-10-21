package com.example.thuan.thuctap.Activity.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuan.thuctap.Activity.Admin.AdminActivity;
import com.example.thuan.thuctap.Activity.BossActivity;
import com.example.thuan.thuctap.Activity.Common.Validator;
import com.example.thuan.thuctap.Activity.Shipper.ShipperActivity;
import com.example.thuan.thuctap.Activity.User.UserActivity;
import com.example.thuan.thuctap.Model.User;
import com.example.thuan.thuctap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.library.AnimateCheckBox;
import com.spark.submitbutton.SubmitButton;
import com.valdesekamdem.library.mdtoast.MDToast;

public class LoginActivity extends AppCompatActivity {
    private EditText edtAccount;
    private EditText edtPassword;
    private SubmitButton btnSubmit;
    private TextView txtRegister;
    private AnimateCheckBox chkSaveInfo;
    private MDToast mdToast;
    private ProgressDialog progressDialog;
    private Validator validator;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String account;
    private String password;
    private String PREFERENCE_NAME = "my_data";
    private String ACCOUNT_ADMIN = "admin";
    private String PASSWORD_ADMIN = "admin";
    private String STATUS_USER = "User";
    private String STATUS_ADMIN = "Admin";
    private String STATUS_SHIPPER = "Shipper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkInternet();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        getId();
//        checkLogin();

        getEvent();

    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPreferences();
    }

    public void savingPreferences()
    {
        //create getSharedPreferences
        SharedPreferences pre = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        //create Editor to save change
        SharedPreferences.Editor editor = pre.edit();
        boolean bchk = chkSaveInfo.isChecked();
        if(!bchk)
        {
            //remove save data
            editor.clear();
        }
        else
        {
            //save to editor
            editor.putString("account", account);
            editor.putString("password", password);
            editor.putBoolean("checked", bchk);
        }
        //accept save to file
        editor.commit();
    }

    /**
     * restore data save
     */
    public void restoringPreferences()
    {
        SharedPreferences pre = getSharedPreferences
                (PREFERENCE_NAME, MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk = pre.getBoolean("checked", false);
        if(bchk)
        {
            //lấy user, pwd, nếu không thấy giá trị mặc định là rỗng
            String user = pre.getString("account", "");
            String pwd = pre.getString("password", "");
            edtAccount.setText(user);
            edtPassword.setText(pwd);
        }
        chkSaveInfo.setChecked(bchk);
    }

    /**
     * check internet
     */
    private void checkInternet() {
        if (isConnected() == false) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.text_title_error_internet_login)
                    .setMessage(R.string.text_content_error_internet_login)
                    .setNegativeButton(R.string.text_button_error_internet_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .show();
        } else {
            mdToast = MDToast.makeText(LoginActivity.this,
                    getString(R.string.text_content_success_internet_login),
                    5000,
                    MDToast.TYPE_SUCCESS);
            mdToast.show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;
        return false;
    }
    /**
     * check user have login or not
     */
    private void checkLogin() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mdToast = MDToast.makeText(LoginActivity.this, "Name " + mAuth.getCurrentUser().getEmail(), 5000, MDToast.TYPE_ERROR);
            mdToast.show();
//            transAdmin();
        }else {
            mdToast = MDToast.makeText(LoginActivity.this, "null ", 5000, MDToast.TYPE_WARNING);
            mdToast.show();
        }
    }
    /**
     * get id
     */
    private void getId() {
        edtAccount = findViewById(R.id.edtAccount_login);
        edtPassword = findViewById(R.id.edtPassword_login);
        btnSubmit = findViewById(R.id.btnSubmit_login);
        txtRegister = findViewById(R.id.txtRegister_login);
        chkSaveInfo = findViewById(R.id.chkSaveInfo_login);
    }

    /**
     * get data from xml
     */
    private void getData() {
        account = edtAccount.getText().toString();
        password = edtPassword.getText().toString();
    }

    /**
     * get event
     */
    private void getEvent() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToRegisterScreen();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                if (validatorLogin()){
                    new BackgroundLogin().execute();
                }
            }
        });
    }

    /**
     * check error
     * @return true or false
     *
     */
    private boolean validatorLogin() {
        validator = new Validator();
//        LOGIN_E001
        if (validator.isBlank(account)){
            mdToast = MDToast.makeText(
                    LoginActivity.this,
                    getString(R.string.text_error_input_account_login),
                    5000,
                    MDToast.TYPE_ERROR
                );
            mdToast.show();
            return false;
        }

//        LOGIN_E002
        if (validator.isBlank(password)){
            mdToast = MDToast.makeText(LoginActivity.this,
                    getString(R.string.text_error_input_password_login),
                    5000,
                    MDToast.TYPE_ERROR
                );
            mdToast.show();
            return false;
        }
        return true;
    }

    /**
     * change from Login screen to Register screen
     */
    private void changeToRegisterScreen() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * change from Login screen to Admin screen
     */
    private void changeToAdminScreen() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * change from Login screen to User screen
     */
    private void changeToUserScreen() {
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * change from Login screen to Shipper screen
     */
    private void changeToShipperScreen() {
        Intent intent = new Intent(LoginActivity.this, ShipperActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * change from Login screen to Boss screen
     */
    private void changeToBossScreen() {
        Intent intent = new Intent(LoginActivity.this, BossActivity.class);
        finish();
        startActivity(intent);
    }
    /**
     * function sign in
     * @param email
     * @param password
     */
    private void signIn(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            myRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);

                                    if (user.getStatus().equals(STATUS_USER)) {
                                        changeToUserScreen();
                                    }

                                    if (user.getStatus().equals(STATUS_ADMIN)) {
                                        changeToAdminScreen();
                                    }

                                    if (user.getStatus().equals(STATUS_SHIPPER)) {
                                        changeToShipperScreen();
                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            mdToast = MDToast.makeText(LoginActivity.this, getString(R.string.text_log_in_error_login), 5000, MDToast.TYPE_ERROR);
                            mdToast.show();
                        }
                    }
                });
    }

    private class BackgroundLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (account.equals(ACCOUNT_ADMIN)
                    && password.equals(PASSWORD_ADMIN)) {
                changeToBossScreen();
            }
            signIn(account,password);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(getString(R.string.text_content_dialog_login));
            progressDialog.show();
        }
    }
}

