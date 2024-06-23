package com.example.nokarii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.nokarii.databinding.ActivityUpdateEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {


    ActivityUpdateEmailBinding binding;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    String userOldEmail,userNewEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Update Email");

        binding.buttonUpdateEmail.setEnabled(false);  // make button disabled in the beginning until the user is authenticate
        binding.editTextUpdateEmailNew.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser  = authProfile.getCurrentUser();


        //Set old email ID on TextView

        userOldEmail = firebaseUser.getEmail();
        binding.textViewUpdateEmailOld.setText(userOldEmail);


        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong! User's details not available.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            reAuthenticate(firebaseUser);
        }
















    }

    private void reAuthenticate(FirebaseUser firebaseUser) {
        binding.buttonAuthenticateUser.setOnClickListener(v -> {


            if(TextUtils.isEmpty(binding.editTextUpdateEmailVerifyPassword.getText().toString()))
            {
                Toast.makeText(this, "Password is needed to continue", Toast.LENGTH_SHORT).show();
                binding.editTextUpdateEmailVerifyPassword.setError("Please enter your Password for authentication");
                binding.editTextUpdateEmailVerifyPassword.requestFocus();

            }

            else
            {
                binding.progressBar.setVisibility(View.VISIBLE);

                AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail,binding.editTextUpdateEmailVerifyPassword.getText().toString());

                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(UpdateEmailActivity.this, "Password has been Verified."+"your can update email now.", Toast.LENGTH_SHORT).show();

                            //Set TextView to show that user is authenticated

                            binding.textViewUpdateEmailAuthenticated.setText("Your authentication. You can update your email now ");


                            //Disable EditText for password  button to verify user and enable EditText for new Email and Update button
                            binding.editTextUpdateEmailNew.setEnabled(true);
                            binding.editTextUpdateEmailVerifyPassword.setEnabled(false);
                            binding.buttonUpdateEmail.setEnabled(true);
                            binding.buttonAuthenticateUser.setEnabled(true);



                            //change color of update button

                            binding.buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this,
                                    R.color.dark_green
                            ));



                            binding.buttonUpdateEmail.setOnClickListener(v1 -> {

                                userNewEmail = binding.editTextUpdateEmailNew.getText().toString();
                                if(TextUtils.isEmpty(userNewEmail))
                                {
                                    Toast.makeText(UpdateEmailActivity.this, "New Email is Required", Toast.LENGTH_SHORT).show();
                                    binding.editTextUpdateEmailNew.setError("Please enter new Email");
                                    binding.editTextUpdateEmailNew.requestFocus();

                                }
                                else if(!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches())
                                {
                                    Toast.makeText(UpdateEmailActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
                                    binding.editTextUpdateEmailNew.setError("Please provide valid Email");
                                    binding.editTextUpdateEmailNew.requestFocus();

                                }
                                else if(userOldEmail.matches(userNewEmail))
                                {
                                    Toast.makeText(UpdateEmailActivity.this, "New Email cannot be same as old Email", Toast.LENGTH_SHORT).show();
                                    binding.editTextUpdateEmailNew.setError("Please enter new Email");
                                    binding.editTextUpdateEmailNew.requestFocus();

                                }
                                else
                                {
                                    binding.progressBar.setVisibility(View.VISIBLE);
                                    updateEmail(firebaseUser);
                                }






                            });




                        }
                        else
                        {

                            try {
                                throw task.getException();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(UpdateEmailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(UpdateEmailActivity.this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();


                            }

                            binding.progressBar.setVisibility(View.GONE);
                        }







                    }

                });


            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {

        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isComplete())
                {
                    //Verify Email
                    firebaseUser.sendEmailVerification();

                    Toast.makeText(UpdateEmailActivity.this, "Email has been updated. Please verify your new Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateEmailActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    try {
                        throw task.getException();

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(UpdateEmailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }



                }

                binding.progressBar.setVisibility(View.GONE);



            }
        });

    }
}