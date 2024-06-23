package com.example.nokarii;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nokarii.databinding.UserDataBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String userId);
    }

    private OnItemClickListener mListener;
    private FirebaseAuth authProfile;
    private String userType; // User type (Job Seeker or Job Giver)
    private List<String> userIds;
    private List<String> filteredUserIds; // Add this line for the filtered list

    public AdapterUser(List<String> userIds, String userType) {
        this.userIds = userIds;
        this.userType = userType;
        authProfile = FirebaseAuth.getInstance();
    }

    // Add this method to set the filtered list
    public void setFilteredList(List<String> filteredUserIds) {
        this.filteredUserIds = filteredUserIds;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userId = userIds.get(position);
        fetchUserDetails(userId, holder);
    }

    private void fetchUserDetails(String userId, ViewHolder holder) {
        DatabaseReference userReference;
        if ("Job Seeker".equals(userType)) {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobGiverUsers").child(userId);
        } else {
            userReference = FirebaseDatabase.getInstance().getReference().child("JobSeekerUsers").child(userId);
        }

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ReadWriteUserDetails user = dataSnapshot.getValue(ReadWriteUserDetails.class);

                    if (user != null) {
                        holder.binding.textViewDob.setText(user.getSalary());
                        holder.binding.textViewGender.setText(user.getGender());
                        holder.binding.textViewFullName.setText(user.getJobType());
                        holder.binding.textViewEmail.setText(user.getProfession());

                        if (user.profilePicUrl != null) {
                            Picasso.get().load(Uri.parse(user.profilePicUrl)).into(holder.binding.ImageViewProfile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    @Override
    public int getItemCount() {
        // Use the filtered list if available, otherwise use the original list
        return filteredUserIds != null ? filteredUserIds.size() : userIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UserDataBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserDataBinding.bind(itemView);

            // Set click listener on the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String userId = filteredUserIds != null ? filteredUserIds.get(position) : userIds.get(position);
                            mListener.onItemClick(userId);
                        }
                    }
                }
            });
        }
    }
}
