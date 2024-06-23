package com.example.nokarii;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView imageViewProfileDetail;
    private TextView textViewFullNameDetail;
    private TextView   textviewProfessionDetail;
    private  TextView textviewDobDetail;
    private TextView   textviewMobileEtail;
    private TextView textViewSalaryDetail;
    private TextView   textviewQualificationDetail;
    private TextView  textviewExperienceDetail;
   private ImageView ImageViewBackk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Initialize views
        imageViewProfileDetail = findViewById(R.id.Detail_imageView_profile_dp);
        textViewFullNameDetail = findViewById(R.id.Detail_textView_show_full_name);
        textviewProfessionDetail = findViewById(R.id.Detail_textView_show_email);
        textviewExperienceDetail=findViewById(R.id.Detail_textView_show_experience);
        textViewSalaryDetail=findViewById(R.id.Detail_textView_show_gender);
        textviewMobileEtail=findViewById(R.id.Detail_textView_show_mobile);
        ImageViewBackk=findViewById(R.id.ImageViewBack);
        textviewDobDetail=findViewById(R.id.Detail_textView_show_dob);
        textviewQualificationDetail=findViewById(R.id.Detail_textView_show_qualification);
        ImageView imageViewWhatsApp = findViewById(R.id.imageViewPhone);

// Set OnClickListener for WhatsApp icon
        imageViewWhatsApp.setOnClickListener(view -> {
            // Get the user's mobile number from the displayed details
            String userMobileNumber = textviewMobileEtail.getText().toString();

            if (!TextUtils.isEmpty(userMobileNumber)) {
                // Open WhatsApp with the user's mobile number
                openWhatsApp(userMobileNumber);
            } else {
                Toast.makeText(this, "Mobile number not available", Toast.LENGTH_SHORT).show();
            }
        });
      ImageViewBackk.setOnClickListener(v -> {
          Intent intent=new Intent(this,DashboardActivity.class);
          startActivity(intent);
      });

        // Retrieve userId from intent
        String userId = getIntent().getStringExtra("userId");

        // Fetch and display user details based on userId from Firebase Database
        fetchAndDisplayUserDetails(userId);
    }

    private void fetchAndDisplayUserDetails(String userId) {
        DatabaseReference jobSeekerReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers").child(userId);
        DatabaseReference jobGiverReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers").child(userId);

        // First, check JobSeekerUsers
        jobSeekerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);
                    if (user != null) {
                        // Set the user-specific details
                        textViewFullNameDetail.setText(user.getUserName());
                         textViewSalaryDetail.setText(user.getSalary());
                         textviewMobileEtail.setText(user.getMobile());
                         textviewQualificationDetail.setText(user.getQualification());
                         textviewExperienceDetail.setText(user.getExperience());
                         textviewProfessionDetail.setText(user.getProfession());
                         textviewDobDetail.setText(user.getDoB());
                        // Load profile picture using Picasso
                        Picasso.get().load(Uri.parse(user.profilePicUrl)).into(imageViewProfileDetail);
                    }
                } else {
                    // If not found in JobSeekerUsers, check JobGiverUsers
                    jobGiverReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);
                                if (user != null) {
                                    // Set the user-specific details
                                    textViewFullNameDetail.setText("Name:"+ user.getUserName());
                                    textViewSalaryDetail.setText(user.getSalary());
                                    textviewMobileEtail.setText(user.getMobile());
                                    textviewQualificationDetail.setText(user.getQualification());
                                    textviewExperienceDetail.setText(user.getExperience());
                                    textviewProfessionDetail.setText(user.getProfession());
                                    textviewDobDetail.setText(user.getDoB());
                                    // Load profile picture using Picasso
                                    Picasso.get().load(Uri.parse(user.profilePicUrl)).into(imageViewProfileDetail);
                                }
                            } else {
                                // Handle the case where the user is not found in either node
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this,DashboardActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    private void openWhatsApp(String phoneNumber) {
        // Format the phone number by removing spaces, dashes, and other characters
        String formattedPhoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Construct the WhatsApp intent
        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
        whatsappIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + formattedPhoneNumber));

        // Verify that WhatsApp is installed on the device
        if (whatsappIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(whatsappIntent);
        } else {
            // Handle the case where WhatsApp is not installed
            Toast.makeText(this, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show();
        }
    }
}
