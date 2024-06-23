package com.example.nokarii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nokarii.databinding.ActivityUserProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";

    private FirebaseAuth authProfile;
    private String fullName, email, doB, gender, mobile;

    ActivityUserProfileBinding binding;
    String userType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authProfile = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_SHORT).show();
        } else {
            checkIfProfilePictureUploaded(firebaseUser);

            binding.progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }





        binding.imageViewProfileDp.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, UploadProfileActivity.class);

            intent.putExtra("name", fullName);
            intent.putExtra("email", email);
            intent.putExtra("dob", doB);
            intent.putExtra("gender", gender);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
        });
    }
    private void checkIfProfilePictureUploaded(FirebaseUser firebaseUser) {
        Uri uri = firebaseUser.getPhotoUrl();
        if (uri == null) {
            showProfilePictureDialog();
        }
    }
    // User coming to UserProfileActivity after successful registration

    private void showProfilePictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Profile Picture Not Uploaded");
        builder.setMessage("Please upload your profile picture. It adds a personal touch to your profile!");

        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle what should happen when the user clicks OK
            Intent intent = new Intent(UserProfileActivity.this, UploadProfileActivity.class);
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile;
           userType=getUserType();
           binding.button.setOnClickListener(v -> {
               Intent intent= new Intent(UserProfileActivity.this,DashboardActivity.class);
               startActivity(intent);
           });
        // Determine the Firebase node based on the user's type (Job Seeker or Job Giver)
        if ("Job Seeker".equals(userType)) {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobSeekerUsers");
        } else if ("Job Giver".equals(userType)) {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobGiverUsers");
        } else {
            Toast.makeText(this, "Invalid user type", Toast.LENGTH_SHORT).show();
            return;
        }
        // Retrieve user information from the selected Firebase node
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if (readWriteUserDetails != null) {
                    fullName =readWriteUserDetails.userName;
                    email = readWriteUserDetails.userEmail;
                    doB = readWriteUserDetails.doB;
                    gender = readWriteUserDetails.gender;
                    mobile = readWriteUserDetails.mobile;

                    // Update UI with user information
                    binding.textViewShowWelcome.setText("Welcome, " + fullName + "!");
                    binding.textViewShowFullName.setText(fullName);
                    binding.textViewShowEmail.setText(email);
                    binding.textViewShowDob.setText(doB);
                    binding.textViewShowGender.setText(gender);
                    binding.textViewShowMobile.setText(mobile);

                    // Load user profile picture using Picasso
                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(binding.imageViewProfileDp);
                } else {
                    Toast.makeText(UserProfileActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }

                // Hide progress bar after loading user profile
                binding.progressBar.setVisibility(View.GONE);
            }
            private void showProfilePictureDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                builder.setTitle("Profile Picture Not Uploaded");
                builder.setMessage("Please upload your profile picture. It adds a personal touch to your profile!");

                builder.setPositiveButton("OK", (dialog, which) -> {
                    // Handle what should happen when the user clicks OK
                    Intent intent = new Intent(UserProfileActivity.this, UploadProfileActivity.class);
                    startActivity(intent);
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong! ", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching data from Firebase: " + error.getMessage());

                // Hide progress bar in case of an error
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            // Refresh
            startActivity(getIntent());
            finish();
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_change_password) {
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UserProfileActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {

            authProfile.signOut();

            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();



            Intent intent = new Intent(UserProfileActivity.this,LoginActivity.class);

            //Clear stack to prevent user coming back to UserprofileActivity on pressing back button after Logging out

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //Close UserProfile Activity*/
        }

         else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private String getUserType() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userType", "");
    }


}
