package com.example.thuan.thuctap.Activity;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appus.splash.Splash;
import com.example.thuan.thuctap.R;

public class SplashActivity extends AppCompatActivity {
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Splash.Builder splash = new Splash.Builder(this,getSupportActionBar());
        splash.perform();
    }
}