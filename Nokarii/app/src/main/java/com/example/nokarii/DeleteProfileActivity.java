package com.example.nokarii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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

public class DeleteProfileActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText editTextUserPwd;
    private ProgressBar progressBar;
    private String userPwd;
    private Button buttonReAuthentication,buttonDeleteUser;
    private TextView textViewAuthenticated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);


        getSupportActionBar().setTitle("Delete Your Profile");
        progressBar = findViewById(R.id.progressBar);
        editTextUserPwd = findViewById(R.id.editText_delete_user_pwd);
        textViewAuthenticated = findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser = findViewById(R.id.button_delete_user);
        buttonReAuthentication =findViewById(R.id.button_delete_user_authenticate);

        //Disable Delete User Button Until User is authenticated

        buttonDeleteUser.setEnabled(false);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser= authProfile.getCurrentUser();


        if(firebaseUser.equals(""))
        {
            Toast.makeText(this, "Something went wrong!"+"User Detail's are not available at the moment", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
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

        buttonReAuthentication.setOnClickListener(v -> {

            userPwd = editTextUserPwd.getText().toString();
            if(TextUtils.isEmpty(userPwd))
            {
                Toast.makeText(this, "Password is needed", Toast.LENGTH_SHORT).show();
                editTextUserPwd.setError("Please enter your current password to authenticate");
                editTextUserPwd.requestFocus();
            }
            else
            {
                progressBar.setVisibility(View.VISIBLE);

                //ReAuthenticate User Now

                AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwd);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);

                            //Disable editText for Password
                            editTextUserPwd.setEnabled(false);

                            //Enable Delete User Button. Disable Authenticate Button

                            buttonReAuthentication.setEnabled(false);
                            buttonDeleteUser.setEnabled(true);

                            //Set TextView to show User is authenticated/verified

                            textViewAuthenticated.setText("You are authenticated/verified"+"You can delete your profile and related data now!");
                            Toast.makeText(DeleteProfileActivity.this, "Password has been verified."+"" +
                                    "You can delete your profile now. Be careful, this action is irreversible", Toast.LENGTH_SHORT).show();


                            buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(
                                    DeleteProfileActivity.this,R.color.dark_green
                            ));

                            buttonDeleteUser.setOnClickListener(v1 -> {
                                showAlertDialog();
                            });


                        }
                        else
                        {
                            try {
                                throw task.getException();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }



                    }
                });


            }













        });
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete User and Related Data?");
        builder.setMessage("Do you really want to delete your profile and related data? The action is irreversible!");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteUser(firebaseUser);

            }
        });

        //Return to User Profile Activity if User presses Cancel Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(DeleteProfileActivity.this,UserProfileActivity.class);
                startActivity(intent);
                finish();


            }
        });

        //Create the Alert Dialog
        AlertDialog alertDialog = builder.create();

        //change the button color of Continue

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });

        //show AlertDialog
        alertDialog.show();



    }

    private void deleteUser(FirebaseUser firebaseUser) {


        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    authProfile.signOut();
                    Toast.makeText(DeleteProfileActivity.this, "User has been deleted!", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(DeleteProfileActivity.this,MainActivity.class);
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
                        Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });

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
            Intent intent = new Intent(DeleteProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(id== R.id.menu_update_email)
        {
            Intent intent = new Intent(DeleteProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);

        }

        else if(id== R.id.menu_change_password)
        {
            Intent intent = new Intent(DeleteProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);

        }
       else if(id== R.id.menu_delete_profile)
        {
            Intent intent = new Intent(DeleteProfileActivity.this,DashboardActivity.class);
            startActivity(intent);

        }
        else if(id== R.id.menu_logout)
        {
            authProfile.signOut();

            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(DeleteProfileActivity.this,UpdateProfileActivity.class);

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