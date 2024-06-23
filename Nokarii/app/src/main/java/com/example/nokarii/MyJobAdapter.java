package com.example.nokarii;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nokarii.databinding.MyJobUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyJobAdapter extends RecyclerView.Adapter<MyJobAdapter.ViewHolder> {

    private List<ReadWriteUserDetails> userData;
    private FirebaseAuth authProfile;
    private String userType; // User type (Job Seeker or Job Giver)
    private String currentUserUid;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(String userId);
    }

    public MyJobAdapter(String userType, List<ReadWriteUserDetails> userData, String currentUserUid) {
        this.userType = userType;
        this.userData = userData;
        this.currentUserUid = currentUserUid;
        authProfile = FirebaseAuth.getInstance();
    }

    public void setUserData(List<ReadWriteUserDetails> userData) {
        this.userData = userData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_job_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReadWriteUserDetails user = userData.get(position);

        bindCurrentUserDetails(holder, user);
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyJobUserBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyJobUserBinding.bind(itemView);
        }
    }

    private void bindCurrentUserDetails(ViewHolder holder, ReadWriteUserDetails user) {
        holder.binding.MyJobTextViewJobtype.setText(user.getJobType());
        holder.binding.MyJobTextProfession.setText(user.getProfession());
        holder.binding.MyJobTextSalary.setText(user.getSalary());
        holder.binding.MyJobTextViewGender.setText(user.getGender());

        if (user.getProfilePicUrl() != null) {
            Picasso.get().load(Uri.parse(user.getProfilePicUrl())).into(holder.binding.MyJobImageViewProfile);
        }

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(currentUserUid);
            }
        });
    }
}
