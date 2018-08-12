package com.example.thuan.thuctap.Activity.Splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.thuan.thuctap.Activity.Login.LoginActivity;
import com.example.thuan.thuctap.R;
import com.yasic.library.particletextview.MovingStrategy.RandomMovingStrategy;
import com.yasic.library.particletextview.Object.ParticleTextViewConfig;
import com.yasic.library.particletextview.View.ParticleTextView;

public class SplashActivity extends AppCompatActivity {
    ParticleTextView particleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        particleTextView = findViewById(R.id.particleTextView);


        ParticleTextViewConfig config = new ParticleTextViewConfig.Builder()
                .setRowStep(5)
                .setColumnStep(5)
                .setTargetText("Trà Sữa App ")
                .setReleasing(0.2)
                .setParticleRadius(2)
                .setMiniDistance(0.1)
                .setTextSize(150)
                .setDelay((long) 2000)
                .setMovingStrategy(new RandomMovingStrategy())
                .instance();
        particleTextView.setConfig(config);
        particleTextView.startAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },6000);

    }
}
