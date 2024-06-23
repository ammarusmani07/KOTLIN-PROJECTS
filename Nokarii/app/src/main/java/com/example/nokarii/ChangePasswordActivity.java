package com.example.nokarii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {


    private FirebaseAuth authProfile;
    private EditText editTextPwdCurr,editTextPwdNew;
    private TextView textViewAuthenticated;
    private Button buttonChangePwd,buttonReAuthenticate;
    private ProgressBar progressBar;
    private String userPwdCurr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle("Change Password");

        editTextPwdNew = findViewById(R.id.editText_change_pwd_new);
        editTextPwdCurr = findViewById(R.id.editText_change_pwd_current);
        textViewAuthenticated = findViewById(R.id.textView_change_pwd_authenticated);
        progressBar  = findViewById(R.id.progressBar);
        buttonReAuthenticate = findViewById(R.id.button_change_pwd_authenticate);
        buttonChangePwd = findViewById(R.id.button_change_pwd);

        //Disable editText for New Password, Confirm New Password and Make Change Pwd Button UnClickable till user is authenticate


        editTextPwdNew.setEnabled(false);
        // editTextPwdConfirmNew.setEnabled(false);
        buttonChangePwd.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();


        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong! User's details not available", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
            startActivity(intent);
            finish();


        }
        else
        {
            reAuthenticateUser(firebaseUser);
        }














    }


    //ReAuthenticate User before changing password
    private void reAuthenticateUser(FirebaseUser firebaseUser) {

        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPwdCurr = editTextPwdCurr.getText().toString();
                if(TextUtils.isEmpty(userPwdCurr))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                    editTextPwdCurr.setError("Please enter your current password tu authenticate");
                    editTextPwdCurr.requestFocus();
                }
                else
                {

                    progressBar.setVisibility(View.VISIBLE);


                    //ReAuthenticate User Now

                    AuthCredential credential  = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwdCurr);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);


                                //Disable editText for Current Password. Enable EditText for New Password and Confirm New Password

                                editTextPwdCurr.setEnabled(false);
                                editTextPwdNew.setEnabled(true);
                                //  editTextPwdConfirmNew.setEnabled(true);


                                //Enable Change Pwd Button. Disable Authenticate Button

                                buttonReAuthenticate.setEnabled(false);
                                buttonChangePwd.setEnabled(true);


                                //Set TextView to show User is authenticated/verified
                                textViewAuthenticated.setText("You are authenticated/verified. " + "You can change password now!");
                                Toast.makeText(ChangePasswordActivity.this, "password has been verified."+ "Change Password now", Toast.LENGTH_SHORT).show();




                                //Update color of Change Password Button

                                buttonChangePwd.setBackgroundTintList(ContextCompat.getColorStateList(
                                        ChangePasswordActivity.this,R.color.dark_green));



                                buttonChangePwd.setOnClickListener(v1 -> {
                                    changePwd(firebaseUser);
                                });


                            }
                            else
                            {
                                try {
                                    throw task.getException();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            progressBar.setVisibility(View.GONE);



                        }
                    });




                }


            }
        });


    }

    private void changePwd(FirebaseUser firebaseUser) {

        String userPwdNew = editTextPwdNew.getText().toString();

        if(TextUtils.isEmpty(userPwdNew))
        {
            Toast.makeText(this, "New Password is needed", Toast.LENGTH_SHORT).show();
            editTextPwdNew.setError("Please enter your new password");
            editTextPwdNew.requestFocus();

        }
        else{
            progressBar.setVisibility(View.VISIBLE);

            firebaseUser.updatePassword(userPwdNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this,UserProfileActivity.class);
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
                            Toast.makeText(ChangePasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }






    }


    //Creating ActionBar Menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items

        getMenuInflater().inflate(R.menu.common_menu,menu);



        return super.onCreateOptionsMenu(menu);



    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id  = item.getItemId();

        if(id== R.id.menu_refresh)
        {
            //Refresh
            startActivity(getIntent());
            finish();
        }
        else if(id == R.id.menu_update_profile)
        {
            Intent intent = new Intent(ChangePasswordActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(id== R.id.menu_update_email)
        {
            Intent intent = new Intent(ChangePasswordActivity.this,UpdateProfileActivity.class);
            startActivity(intent);

        }
       /* else if(id== R.id.menu_Settings)
        {
            Toast.makeText(this, "menu_settings ", Toast.LENGTH_SHORT).show();

        }
        else if(id== R.id.menu_change_password)
        {
            Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);

        }
        else if(id== R.id.menu_delete_profile)
        {
            Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);

        }*/
        else if(id== R.id.menu_logout)
        {
            authProfile.signOut();

            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePasswordActivity.this,MainActivity.class);
            startActivity(intent);


            /*Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);

            //Clear stack to prevent user coming back to UserprofileActivity on pressing back button after Logging out

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //Close UserProfile Activity*/
        }
        else
        {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }





        return super.onOptionsItemSelected(item);

    }
}