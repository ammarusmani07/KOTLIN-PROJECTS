package com.example.nokarii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nokarii.databinding.FragmentCategoryUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryUserFragment extends Fragment {

    private Adaptercategory adapter;
    private RecyclerView recyclerView;
    private List<String> categoryIds; // Replace with the actual type if it's not a String

    // Reference to the Firebase Database
    private DatabaseReference databaseReference;
    private FragmentCategoryUserBinding binding;

    public CategoryUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView
        recyclerView = binding.RecyclerView;
        categoryIds = new ArrayList<>();
        adapter = new Adaptercategory(categoryIds);

        int spanCount = 2; // Number of columns in the grid
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Fetch category IDs from Firebase Database
        fetchCategoryIds();

        return view;
    }

    private void fetchCategoryIds() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.child("JobSeekerUsers").getChildren()) {
                    String categoryId = snapshot.getKey();
                    categoryIds.add(categoryId);
                    if (dataSnapshot.child("JobSeekerUsers").child("new jobs").exists()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.child("JobSeekerUsers").child("new jobs").getChildren()) {
                            // Fetch data from "new jobs" node
                            // Adjust this part based on the structure of your data
                            String jobTitle = snapshot1.child("jobTitle").getValue(String.class);
                            // Add additional fields as needed

                            // Process the fetched data as needed
                        }
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.child("JobGiverUsers").getChildren()) {
                    String categoryId = snapshot.getKey();
                    categoryIds.add(categoryId);
                    if (dataSnapshot.child("JobGiverUsers").child("new jobs").exists()) {
                        for (DataSnapshot snapshot1 : dataSnapshot.child("JobGiverUsers").child("new jobs").getChildren()) {
                            // Fetch data from "new jobs" node
                            // Adjust this part based on the structure of your data
                            String jobTitle = snapshot1.child("jobTitle").getValue(String.class);
                            // Add additional fields as needed

                            // Process the fetched data as needed
                        }
                    }

                }
                adapter.notifyDataSetChanged();
            }


            // Check for "new jobs" node within "JobGiverUsers"

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    // Add any other methods or logic as needed

}
