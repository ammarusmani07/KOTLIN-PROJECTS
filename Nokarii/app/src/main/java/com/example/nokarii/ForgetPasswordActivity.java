package com.example.nokarii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.nokarii.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    String email;

    ActivityForgetPasswordBinding binding;

    private final static String TAG ="ForgotPasswordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Forgot Password");

        binding.buttonPasswordReset.setOnClickListener(v -> {
            email  = binding.editTextPasswordResetEmail.getText().toString();
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(this, "please enter your registered email", Toast.LENGTH_SHORT).show();
                binding.editTextPasswordResetEmail.setError("Email is required");
                binding.editTextPasswordResetEmail.requestFocus();
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(this, "please enter valid email", Toast.LENGTH_SHORT).show();
                binding.editTextPasswordResetEmail.setError("Valid email is required");
                binding.editTextPasswordResetEmail.requestFocus();


            }
            else
            {


                binding.progressBar.setVisibility(View.VISIBLE);
                resetPassword(email);

            }












        });







    }

    private void resetPassword(String Stremail) {

        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(Stremail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(ForgetPasswordActivity.this, "please check your inbox for password rest link", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(ForgetPasswordActivity.this,MainActivity.class);

                    //Clear stack to prevent user coming back to ForgetPasswordActivity

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); //Close UserProfile Activity

                }
                else
                {

                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e)
                    {
                        binding.editTextPasswordResetEmail.setError("User does not exists or is no longer valid. Please register again.");
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG,e.toString());
                        Toast.makeText(ForgetPasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }




                    Toast.makeText(ForgetPasswordActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.progressBar.setVisibility(View.GONE);

    }
}