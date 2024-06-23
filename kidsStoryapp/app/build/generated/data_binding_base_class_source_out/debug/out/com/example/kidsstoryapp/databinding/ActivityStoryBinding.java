// Generated by view binder compiler. Do not edit!
package com.example.kidsstoryapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.kidsstoryapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityStoryBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ImageView bgImage;

  @NonNull
  public final ImageButton btnNext;

  @NonNull
  public final ImageButton btnPlay;

  @NonNull
  public final ImageButton btnPrevious;

  @NonNull
  public final ImageView storyImage;

  @NonNull
  public final TextView tvMoral;

  @NonNull
  public final TextView tvStory;

  @NonNull
  public final TextView tvStoryTitle;

  private ActivityStoryBinding(@NonNull ConstraintLayout rootView, @NonNull ImageView bgImage,
      @NonNull ImageButton btnNext, @NonNull ImageButton btnPlay, @NonNull ImageButton btnPrevious,
      @NonNull ImageView storyImage, @NonNull TextView tvMoral, @NonNull TextView tvStory,
      @NonNull TextView tvStoryTitle) {
    this.rootView = rootView;
    this.bgImage = bgImage;
    this.btnNext = btnNext;
    this.btnPlay = btnPlay;
    this.btnPrevious = btnPrevious;
    this.storyImage = storyImage;
    this.tvMoral = tvMoral;
    this.tvStory = tvStory;
    this.tvStoryTitle = tvStoryTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityStoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityStoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_story, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityStoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bgImage;
      ImageView bgImage = ViewBindings.findChildViewById(rootView, id);
      if (bgImage == null) {
        break missingId;
      }

      id = R.id.btnNext;
      ImageButton btnNext = ViewBindings.findChildViewById(rootView, id);
      if (btnNext == null) {
        break missingId;
      }

      id = R.id.btnPlay;
      ImageButton btnPlay = ViewBindings.findChildViewById(rootView, id);
      if (btnPlay == null) {
        break missingId;
      }

      id = R.id.btnPrevious;
      ImageButton btnPrevious = ViewBindings.findChildViewById(rootView, id);
      if (btnPrevious == null) {
        break missingId;
      }

      id = R.id.storyImage;
      ImageView storyImage = ViewBindings.findChildViewById(rootView, id);
      if (storyImage == null) {
        break missingId;
      }

      id = R.id.tvMoral;
      TextView tvMoral = ViewBindings.findChildViewById(rootView, id);
      if (tvMoral == null) {
        break missingId;
      }

      id = R.id.tvStory;
      TextView tvStory = ViewBindings.findChildViewById(rootView, id);
      if (tvStory == null) {
        break missingId;
      }

      id = R.id.tvStoryTitle;
      TextView tvStoryTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvStoryTitle == null) {
        break missingId;
      }

      return new ActivityStoryBinding((ConstraintLayout) rootView, bgImage, btnNext, btnPlay,
          btnPrevious, storyImage, tvMoral, tvStory, tvStoryTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
