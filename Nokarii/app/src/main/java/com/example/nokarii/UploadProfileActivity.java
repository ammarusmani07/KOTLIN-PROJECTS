package com.example.nokarii;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.nokarii.databinding.ActivityUploadProfileBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.squareup.picasso.Picasso;

public class UploadProfileActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private Uri uriImage;
    private static final int PICK_IMAGE_REQUREST = 1;
    ActivityUploadProfileBinding binding;

    // Catch Data
    String fullName, email, doB, gender, mobile;
    String getUserType;

    private DatabaseReference userReference;
    private ReadWriteUserDetails existingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();


        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri = firebaseUser.getPhotoUrl();

        // Set User's current DP in ImageView(if upload already). We will Picasso since imageViewer setImage
        // Regular URIs.

        // Catch User Previous Activity Data
        fullName = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        doB = getIntent().getStringExtra("dob");
        gender = getIntent().getStringExtra("gender");
        mobile = getIntent().getStringExtra("mobile");

        // Choosing image to upload
        Picasso.get().load(uri).into(binding.imageViewProfileDp);

        binding.uploadPicChooseButton.setOnClickListener(v -> {
            openFileChooser();
        });

        // Upload Image
        binding.uploadPicButton.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            UploadPic();
        });
     getUserType=getUserType();
        // Determine the Firebase node based on the user's type (Job Seeker or Job Giver)
        if ("Job Seeker".equals(getUserType)) {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers").child(firebaseUser.getUid());
        } else {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers").child(firebaseUser.getUid());
        }

        // Fetch the existing user data
        fetchExistingUserData();
    }

    private void fetchExistingUserData() {
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    existingUser = dataSnapshot.getValue(ReadWriteUserDetails.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void UploadPic() {
        if (uriImage != null) {
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "." + getFileExtension(uriImage));

            fileReference.putFile(uriImage).addOnSuccessListener(taskSnapshot -> {
                fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Uri downloadUri = uri;

                    // Update the user's data, including the new profile picture URL
                    existingUser.setProfilePicUrl(downloadUri.toString());

                    // Update the existing node in the database
                    userReference.setValue(existingUser).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Finally, set the display image of the user after upload
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();

                            firebaseUser.updateProfile(profileUpdate).addOnCompleteListener(profileTask -> {
                                if (profileTask.isSuccessful()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(UploadProfileActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(UploadProfileActivity.this, UserProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UploadProfileActivity.this, "Failed to update user profile!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(UploadProfileActivity.this, "Failed to update user data!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(UploadProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            });
        } else {
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No File Selected!", Toast.LENGTH_SHORT).show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUREST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUREST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            binding.imageViewProfileDp.setImageURI(uriImage);
        }
    }
    private String getUserType() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userType", "");
    }
}
