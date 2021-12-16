package com.example.blindguidnace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent  (SplashActivity.this, StartActivity.class));
                        finish();
                    }
    },2000);
}}