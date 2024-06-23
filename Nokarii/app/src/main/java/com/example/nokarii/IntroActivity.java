package com.example.nokarii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.nokarii.databinding.ActivityIntroBinding;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (auth.getCurrentUser() != null) {
            // User is already logged in, redirect to the dashboard activity
            Intent intent = new Intent(IntroActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        }

        binding.buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.textViewRegisterLink.setOnClickListener(v -> {
            // Add a log to check if the click listener is being triggered
            Log.d("IntroActivity", "Register Clicked");

            Intent intent1 = new Intent(IntroActivity.this, SignupActivity.class);
            startActivity(intent1);
        });
    }
}
