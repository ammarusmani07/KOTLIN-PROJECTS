package com.example.nokarii;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nokarii.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    private static final String TAG="LoginActivity";

    private FirebaseAuth authProfile;
    private ProgressBar progressBar;

    String textEmail,textPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = findViewById(R.id.progressBar);
        authProfile = FirebaseAuth.getInstance();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        //Show Hide Password using Eye Icon
        binding.imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_password);
        binding.imageViewShowHidePwd.setOnClickListener(v -> {
            if(binding.editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                //If password is visible then hide it

                binding.editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //Change Icon
                binding.imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_password);

            }
            else
            {
                binding.editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.imageViewShowHidePwd.setImageResource(R.drawable.ic_show_password);

            }


        });
    binding.textViewRegisterLink.setOnClickListener(v -> {
        Intent intent= new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    });

        binding.buttonForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
            startActivity(intent);
        });

        binding.buttonLogin.setOnClickListener(v -> {


            textEmail = binding.editTextLoginEmail.getText().toString();
            textPassword = binding.editTextLoginPwd.getText().toString();


            if(TextUtils.isEmpty(textEmail))
            {
                Toast.makeText(this, "please enter your email", Toast.LENGTH_SHORT).show();
                binding.editTextLoginEmail.setError("Email is required");
                binding.editTextLoginPwd.requestFocus();
            }

            else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())
            {

                Toast.makeText(this, "please re-enter your email", Toast.LENGTH_SHORT).show();
                binding.editTextLoginEmail.setError("Valid Email is required");
                binding.editTextLoginPwd.requestFocus();

            }
            else if (TextUtils.isEmpty(textPassword))
            {
                Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
                binding.editTextLoginPwd.setError("Password is required");
                binding.editTextLoginPwd.requestFocus();

            }
            else {

                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail,textPassword);
            }


        });

    }

    private void loginUser(String email, String Pwd)
    {

        authProfile.signInWithEmailAndPassword(email,Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {

                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e)
                    {
                        binding.editTextLoginEmail.setError("User does not exists or is no longer valid, Please register again.");
                        binding.editTextLoginEmail.requestFocus();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        binding.editTextLoginEmail.setError("Invalid credentials. Kindly, check and re-enter.");
                        binding.editTextLoginEmail.requestFocus();
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.toString());
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }


                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);

            }
        });
    }

}
