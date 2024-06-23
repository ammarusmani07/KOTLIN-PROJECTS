package com.example.nokarii;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private String textFullName, textDob, textGender, textMobile, textProfession, textSalary;
    String getUserType;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private EditText editTextUpdateName, editTextUpdateDob, editTextUpdateMobile, editTextUpdateProfession, editTextUpdateSalary;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        getUserType = getIntent().getStringExtra("userType");
        progressBar = findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_update_profile_name);
        editTextUpdateDob = findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile = findViewById(R.id.editText_update_profile_mobile);
        editTextUpdateSalary = findViewById(R.id.editText_update_salary);
        editTextUpdateProfession = findViewById(R.id.editText_update_profession);
        radioGroupUpdateGender = findViewById(R.id.radio_group_update_profile_gender);

        // Show Profile Data
        showProfile(firebaseUser);

        // Upload Profile Pic
        Button buttonUploadProfilePic = findViewById(R.id.button_update_profile);
        buttonUploadProfilePic.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateProfileActivity.this, UploadProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Setting up DatePicker on EditText
        EditText editTextDob = findViewById(R.id.editText_update_profile_dob);
        editTextDob.setOnClickListener(v -> {
            String textDoB[] = textDob.split("/");
            int day = Integer.parseInt(textDoB[0]);
            int month = Integer.parseInt(textDoB[1]) - 1; // to take care of month index starting from 0
            int year = Integer.parseInt(textDoB[2]);

            DatePickerDialog picker = new DatePickerDialog(UpdateProfileActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        EditText editTexttdob = findViewById(R.id.editText_update_profile_dob);
                        editTexttdob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    }, year, month, day);
            picker.show();
        });

        // Update Profile
        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(v -> updateProfile(firebaseUser));
    }

    // Update Profile
    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID = radioGroupUpdateGender.getCheckedRadioButtonId();
        radioButtonUpdateGenderSelected = findViewById(selectedGenderID);

        String mobileRegex = "[6-9][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(textMobile);

        if (TextUtils.isEmpty(editTextUpdateName.getText().toString())) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Full Name is required");
            editTextUpdateName.requestFocus();
        } else if (TextUtils.isEmpty(editTextUpdateMobile.getText().toString())) {
            Toast.makeText(this, "Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("MobileNumber required");
            editTextUpdateMobile.requestFocus();
        } else if (TextUtils.isEmpty(editTextUpdateDob.getText().toString())) {
            Toast.makeText(this, "Please enter your DateOfBirth", Toast.LENGTH_SHORT).show();
            editTextUpdateDob.setError("DateOfBirth required");
            editTextUpdateDob.requestFocus();
        } else if (editTextUpdateMobile.getText().toString().length() != 11) {
            Toast.makeText(this, "Please re-enter your mobile no.", Toast.LENGTH_SHORT).show();
            editTextUpdateMobile.setError("Mobile No. should be 11 digits");
            editTextUpdateMobile.requestFocus();
        } else {
            textGender = radioButtonUpdateGenderSelected.getText().toString();
            textFullName = editTextUpdateName.getText().toString();
            textDob = editTextUpdateDob.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();
            textProfession = editTextUpdateProfession.getText().toString();
            textSalary = editTextUpdateSalary.getText().toString();

            // Fetch existing user details from Firebase
            DatabaseReference referenceProfile;
            if ("Job Seeker".equals(getUserType)) {
                referenceProfile = FirebaseDatabase.getInstance().getReference("JobSeekerUsers");
            } else {
                referenceProfile = FirebaseDatabase.getInstance().getReference("JobGiverUsers");
            }

            String userID = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);

            referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User details already exist, retrieve them
                        ReadWriteUserDetails existingUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                        // Check if fields are empty, if not, use existing values
                        if (TextUtils.isEmpty(textProfession)) {
                            textProfession = existingUserDetails.getProfession();
                        }
                        if (TextUtils.isEmpty(textSalary)) {
                            textSalary = existingUserDetails.getSalary();
                        }

                        // Create the updated user details object
                        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(
                                textDob, textGender, textMobile, textFullName, firebaseUser.getEmail(),
                                existingUserDetails.getProfilePicUrl(), existingUserDetails.getJobType(),
                                textProfession, textSalary, existingUserDetails.getQualification(),
                                existingUserDetails.getExperience());

                        // Update user profile
                        referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(textFullName).build();
                                firebaseUser.updateProfile(profileUpdates);
                                Toast.makeText(UpdateProfileActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(UpdateProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle errors
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    // Fetch data from Firebase and display
    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();

        DatabaseReference referenceProfile;
        // Determine the Firebase node based on the user's type (Job Seeker or Job Giver)
        if ("Job Seeker".equals(getUserType)) {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobSeekerUsers");
        } else {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobGiverUsers");
        }

        progressBar.setVisibility(View.VISIBLE);

        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    textFullName = firebaseUser.getDisplayName();
                    textDob = readUserDetails.getDoB();
                    textGender = readUserDetails.getGender();
                    textMobile = readUserDetails.getMobile();
                    textProfession = readUserDetails.getProfession();
                    textSalary = readUserDetails.getSalary();

                    EditText editTextName = findViewById(R.id.editText_update_profile_name);
                    EditText editTextDob = findViewById(R.id.editText_update_profile_dob);
                    EditText editTextMobile = findViewById(R.id.editText_update_profile_mobile);
                    EditText editTextSalary = findViewById(R.id.editText_update_salary);
                    EditText editTextProfession = findViewById(R.id.editText_update_profession);

                    editTextName.setText(textFullName);
                    editTextDob.setText(textDob);
                    editTextMobile.setText(textMobile);
                    editTextProfession.setText(textProfession);
                    editTextSalary.setText(textSalary);
                    // Show Gender through Radio Button
                    if (textGender.equals("Male")) {
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_male);
                    } else {
                        radioButtonUpdateGenderSelected = findViewById(R.id.radio_female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
