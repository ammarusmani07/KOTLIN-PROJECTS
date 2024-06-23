package com.example.nokarii;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nokarii.databinding.FragmentUserBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment implements AdapterUser.OnItemClickListener {

    private AdapterUser adapter;
    private RecyclerView recyclerView;
    private List<String> userIds; // List of user IDs
    private String userType; // User type (Job Seeker or Job Giver)
    private DatabaseReference databaseReference;
    private FragmentUserBinding binding;
    private SearchView searchView;
    private String currentProfessionQuery;
    private List<String> filteredUserIds = new ArrayList<>();

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        userType = getUserType();
        searchView = view.findViewById(R.id.searchView);
        if ("Job Seeker".equals(userType)) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers");
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers");
        }

        // Initialize RecyclerView
        recyclerView = binding.RecyclerView;
        userIds = new ArrayList<>();
        adapter = new AdapterUser(userIds, userType);
        adapter.setOnItemClickListener(this);

        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Fetch user IDs from Firebase Database
        fetchUserIds();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentProfessionQuery = query;
                performSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentProfessionQuery = newText;
                performSearch();
                return true;
            }
        });

        return view;
    }

    private void fetchUserIds() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.getKey();
                    userIds.add(userId);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    public void onItemClick(String userId) {
        openUserDetailActivity(userId);
    }

    private void openUserDetailActivity(String userId) {
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private String getUserType() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userType", "");
    }

    private void performSearch() {
        filteredUserIds.clear();

        for (String userId : userIds) {
            fetchUserDetails(userId);
        }
    }

    private void fetchUserDetails(String userId) {
        DatabaseReference userReference;
        if ("Job Seeker".equals(userType)) {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers").child(userId);
        } else {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers").child(userId);
        }

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);

                    if (user != null) {
                        String userProfession = user.getProfession();

                        if (userProfession != null && userProfession.toLowerCase().startsWith(currentProfessionQuery.toLowerCase())) {
                            filteredUserIds.add(userId);
                        }

                        adapter.setFilteredList(filteredUserIds);

                        if (dataSnapshot.hasChild("new jobs")) {
                            // Fetch data from the "new jobs" node
                            DataSnapshot jobDetailsSnapshot = dataSnapshot.child("new jobs");
                            for (DataSnapshot childSnapshot : jobDetailsSnapshot.getChildren()) {
                                ReadWriteUserDetails jobDetails = childSnapshot.getValue(ReadWriteUserDetails.class);

                                // Customize logic based on your requirements
                                if (jobDetails != null) {
                                    // Example: Print or log job details
                                    Log.d("JobDetails", "Job details");
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
}
