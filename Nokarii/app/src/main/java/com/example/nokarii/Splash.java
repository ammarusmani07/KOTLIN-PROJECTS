package com.example.nokarii;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to launch the IntroActivity
                Intent intent = new Intent(Splash.this, IntroActivity.class);

                // Start the IntroActivity
                startActivity(intent);

                // Optional: Close the Splash activity to prevent going back to it
                finish();
            }
        }, 2000); // Delay for 2000 mi
    }
}