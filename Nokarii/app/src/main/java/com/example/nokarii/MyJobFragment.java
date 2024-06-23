package com.example.nokarii;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.nokarii.databinding.FragmentMyJobBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyJobFragment extends Fragment implements MyJobAdapter.OnItemClickListener {

    private FragmentMyJobBinding binding;
    private MyJobAdapter myJobAdapter;
    private List<ReadWriteUserDetails> currentUserData;
    private String userType;
    private String currentUserUid;
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;

    public MyJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyJobBinding.inflate(inflater, container, false);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userType = getUserType();
        initRecyclerView();
        fetchCurrentUserDetails();
        setupFloatingActionButton();
    }

    private void setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPostJobDialog();
            }
        });
    }

    private void showPostJobDialog() {
        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile;

        // Inflate the dialog layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dailouge, null);

        // Initialize views in the dialog
        ImageView imageView = dialogView.findViewById(R.id.newjob_imageView_profile_dp);
        AutoCompleteTextView autoCompleteTextViewSalary = dialogView.findViewById(R.id.newjob_autoCompleteTextViewSalary);
  AutoCompleteTextView autoCompleteTextViewProfession = dialogView.findViewById(R.id.newjob_autoCompleteTextViewProfession);
  AutoCompleteTextView autoCompleteTextViewQualification = dialogView.findViewById(R.id.newjob_autoCompleteTextViewe_qual);
  AutoCompleteTextView autoCompleteTextViewExperience = dialogView.findViewById(R.id.newjob_autoCompleteTextView_experience);


// Set up ArrayAdapter for salary suggestions
        ArrayAdapter<String> salaryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.suggestions_salary));
        autoCompleteTextViewSalary.setAdapter(salaryAdapter);
        ArrayAdapter<String>  professionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.suggestions_jobs));
        autoCompleteTextViewProfession.setAdapter(professionAdapter);
        ArrayAdapter<String> qualificationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.suggestions_qualifications));
        autoCompleteTextViewQualification.setAdapter(qualificationAdapter);
        ArrayAdapter<String> ExperienceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.suggestions_experience));
        autoCompleteTextViewExperience.setAdapter(ExperienceAdapter);

        // Set the current user's image on the ImageView

        if ("Job Seeker".equals(userType)) {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobSeekerUsers");
        } else {
            referenceProfile = FirebaseDatabase.getInstance().getReference("JobGiverUsers");
        }
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);
                    if (user != null) {
                        // Load the user's image using an image loading library like Picasso or Glide
                        // For simplicity, assuming the user has a field 'profileImage' containing the image URL
                        // You should replace this with your actual logic to load the image

                        // Use Picasso or Glide to load the image into the ImageView
                        // For example, using Picasso:
                        // Inside onDataChange when loading the image
                        Uri uri = firebaseUser.getPhotoUrl();
                        Picasso.get().load(uri).into(imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
                Log.e("MyJobFragment", "Database Error: " + databaseError.getMessage());
            }
        });

        // Set default values or listeners for other views if needed
        autoCompleteTextViewSalary.setHint("Enter your salary expectation");

        // Set the custom view for the dialog
        builder.setView(dialogView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the positive button click
                        String salary = autoCompleteTextViewSalary.getText().toString();
                        String profession = autoCompleteTextViewProfession.getText().toString();
                        String experience = autoCompleteTextViewExperience.getText().toString();
                        String qualification = autoCompleteTextViewQualification.getText().toString();

                        // Generate a unique key for each entry
                        String entryKey = FirebaseDatabase.getInstance().getReference().push().getKey();

                        // Create a new child node with the unique key under the user's UID
                        DatabaseReference userReference;
                        if ("Job Seeker".equals(userType)) {
                            userReference = FirebaseDatabase.getInstance().getReference("JobSeekerUsers").child(currentUserUid).child("new jobs").child(entryKey);
                        } else {
                            userReference = FirebaseDatabase.getInstance().getReference("JobGiverUsers").child(currentUserUid).child("new jobs").child(entryKey);
                        }

                        // Save the data under the child node
                        userReference.child("salary").setValue(salary);
                        userReference.child("profession").setValue(profession);
                        userReference.child("experience").setValue(experience);
                        userReference.child("qualification").setValue(qualification);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Handle the negative button click or simply dismiss the dialog
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initRecyclerView() {
        currentUserData = new ArrayList<>();
        myJobAdapter = new MyJobAdapter(userType, currentUserData, currentUserUid);

        // Set the click listener
        myJobAdapter.setOnItemClickListener(this);

        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        binding.MyJobRecyclerView.setLayoutManager(layoutManager);
        binding.MyJobRecyclerView.setAdapter(myJobAdapter);
    }

    private void fetchCurrentUserDetails() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            currentUserUid = currentUser.getUid();

            DatabaseReference userReference;
            if ("Job Seeker".equals(userType)) {
                userReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers").child(currentUserUid);
            } else {
                userReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers").child(currentUserUid);
            }

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);

                        if (user != null) {
                            // Clear the existing list before adding new data
                            currentUserData.clear();

                            // Add user details to the list
                            currentUserData.add(user);

                            if (dataSnapshot.hasChild("new jobs")) {
                                DataSnapshot jobDetailsSnapshot = dataSnapshot.child("new jobs");
                                // Iterate through each child in the "new jobs" node
                                for (DataSnapshot childSnapshot : jobDetailsSnapshot.getChildren()) {
                                    ReadWriteUserDetails jobDetails = childSnapshot.getValue(ReadWriteUserDetails.class);
                                    currentUserData.add(jobDetails);
                                }
                            }


                            myJobAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Log.e("MyJobFragment", "Database Error: " + databaseError.getMessage());
                }
            });
        }
    }



    @Override
    public void onItemClick(String userId) {
        showDeleteProfileDialog(userId);
    }

    private void showDeleteProfileDialog(final String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Profile");
        builder.setMessage("Are you sure you want to delete this profile?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openDeleteProfileActivity(userId);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openDeleteProfileActivity(String userId) {
        Intent intent = new Intent(getActivity(), DeleteProfileActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private String getUserType() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userType", "");
    }
}
