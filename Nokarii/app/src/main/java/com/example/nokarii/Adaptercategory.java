package com.example.nokarii;

import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nokarii.ReadWriteUserDetails;
import com.example.nokarii.databinding.UserDataBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Adaptercategory extends RecyclerView.Adapter<Adaptercategory.ViewHolder> {

    private List<String> categoryIds; // Replace with the actual type if it's not a String
    private DatabaseReference databaseReference;

    public Adaptercategory(List<String> categoryIds) {
        this.categoryIds = categoryIds;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserDataBinding binding = UserDataBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String categoryId = categoryIds.get(position);
        fetchCategoryDetails(categoryId, holder);
    }

    private void fetchCategoryDetails(String categoryId, ViewHolder holder) {
        DatabaseReference jobSeekerReference = databaseReference.child("JobSeekerUsers").child(categoryId);
        DatabaseReference jobGiverReference = databaseReference.child("JobGiverUsers").child(categoryId);

        jobSeekerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);

                    if (user != null) {
                        populateHolderWithData(holder, user);
                    }
                } else {
                    jobGiverReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);

                                if (user != null) {
                                    populateHolderWithData(holder, user);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle errors here
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    private void populateHolderWithData(ViewHolder holder, ReadWriteUserDetails user) {
        holder.binding.textViewDob.setText(user.getSalary());
        holder.binding.textViewGender.setText(user.getGender());
        holder.binding.textViewFullName.setText(user.getJobType());
        holder.binding.textViewEmail.setText(user.getProfession());

        if (user.profilePicUrl != null) {
            Picasso.get().load(Uri.parse(user.profilePicUrl)).into(holder.binding.ImageViewProfile);
        }
    }

    @Override
    public int getItemCount() {
        return categoryIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public UserDataBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserDataBinding.bind(itemView);

            // Add any click listeners or other initialization logic for your item view
        }
    }
}
