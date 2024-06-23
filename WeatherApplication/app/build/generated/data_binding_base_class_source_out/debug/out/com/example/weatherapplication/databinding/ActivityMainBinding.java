// Generated by view binder compiler. Do not edit!
package com.example.weatherapplication.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherapplication.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView condition;

  @NonNull
  public final Flow flow;

  @NonNull
  public final TextView humidity;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final LinearLayout linearLayout2;

  @NonNull
  public final LinearLayout linearLayout3;

  @NonNull
  public final LinearLayout linearLayout4;

  @NonNull
  public final LinearLayout linearLayout5;

  @NonNull
  public final LinearLayout linearLayout6;

  @NonNull
  public final LottieAnimationView lottieAnimationView2;

  @NonNull
  public final TextView sea;

  @NonNull
  public final SearchView searchView;

  @NonNull
  public final TextView sunrise;

  @NonNull
  public final TextView sunset;

  @NonNull
  public final TextView textViewCondition;

  @NonNull
  public final TextView textViewToday;

  @NonNull
  public final TextView textViewcityname;

  @NonNull
  public final TextView textViewdate;

  @NonNull
  public final TextView textViewday;

  @NonNull
  public final TextView textViewmaxtemp;

  @NonNull
  public final TextView textViewmintemp;

  @NonNull
  public final TextView textViewtemp;

  @NonNull
  public final TextView windspeed;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull TextView condition,
      @NonNull Flow flow, @NonNull TextView humidity, @NonNull LinearLayout linearLayout,
      @NonNull LinearLayout linearLayout2, @NonNull LinearLayout linearLayout3,
      @NonNull LinearLayout linearLayout4, @NonNull LinearLayout linearLayout5,
      @NonNull LinearLayout linearLayout6, @NonNull LottieAnimationView lottieAnimationView2,
      @NonNull TextView sea, @NonNull SearchView searchView, @NonNull TextView sunrise,
      @NonNull TextView sunset, @NonNull TextView textViewCondition,
      @NonNull TextView textViewToday, @NonNull TextView textViewcityname,
      @NonNull TextView textViewdate, @NonNull TextView textViewday,
      @NonNull TextView textViewmaxtemp, @NonNull TextView textViewmintemp,
      @NonNull TextView textViewtemp, @NonNull TextView windspeed) {
    this.rootView = rootView;
    this.condition = condition;
    this.flow = flow;
    this.humidity = humidity;
    this.linearLayout = linearLayout;
    this.linearLayout2 = linearLayout2;
    this.linearLayout3 = linearLayout3;
    this.linearLayout4 = linearLayout4;
    this.linearLayout5 = linearLayout5;
    this.linearLayout6 = linearLayout6;
    this.lottieAnimationView2 = lottieAnimationView2;
    this.sea = sea;
    this.searchView = searchView;
    this.sunrise = sunrise;
    this.sunset = sunset;
    this.textViewCondition = textViewCondition;
    this.textViewToday = textViewToday;
    this.textViewcityname = textViewcityname;
    this.textViewdate = textViewdate;
    this.textViewday = textViewday;
    this.textViewmaxtemp = textViewmaxtemp;
    this.textViewmintemp = textViewmintemp;
    this.textViewtemp = textViewtemp;
    this.windspeed = windspeed;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.condition;
      TextView condition = ViewBindings.findChildViewById(rootView, id);
      if (condition == null) {
        break missingId;
      }

      id = R.id.flow;
      Flow flow = ViewBindings.findChildViewById(rootView, id);
      if (flow == null) {
        break missingId;
      }

      id = R.id.humidity;
      TextView humidity = ViewBindings.findChildViewById(rootView, id);
      if (humidity == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.linearLayout2;
      LinearLayout linearLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout2 == null) {
        break missingId;
      }

      id = R.id.linearLayout3;
      LinearLayout linearLayout3 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout3 == null) {
        break missingId;
      }

      id = R.id.linearLayout4;
      LinearLayout linearLayout4 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout4 == null) {
        break missingId;
      }

      id = R.id.linearLayout5;
      LinearLayout linearLayout5 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout5 == null) {
        break missingId;
      }

      id = R.id.linearLayout6;
      LinearLayout linearLayout6 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout6 == null) {
        break missingId;
      }

      id = R.id.lottieAnimationView2;
      LottieAnimationView lottieAnimationView2 = ViewBindings.findChildViewById(rootView, id);
      if (lottieAnimationView2 == null) {
        break missingId;
      }

      id = R.id.sea;
      TextView sea = ViewBindings.findChildViewById(rootView, id);
      if (sea == null) {
        break missingId;
      }

      id = R.id.searchView;
      SearchView searchView = ViewBindings.findChildViewById(rootView, id);
      if (searchView == null) {
        break missingId;
      }

      id = R.id.sunrise;
      TextView sunrise = ViewBindings.findChildViewById(rootView, id);
      if (sunrise == null) {
        break missingId;
      }

      id = R.id.sunset;
      TextView sunset = ViewBindings.findChildViewById(rootView, id);
      if (sunset == null) {
        break missingId;
      }

      id = R.id.textViewCondition;
      TextView textViewCondition = ViewBindings.findChildViewById(rootView, id);
      if (textViewCondition == null) {
        break missingId;
      }

      id = R.id.textViewToday;
      TextView textViewToday = ViewBindings.findChildViewById(rootView, id);
      if (textViewToday == null) {
        break missingId;
      }

      id = R.id.textViewcityname;
      TextView textViewcityname = ViewBindings.findChildViewById(rootView, id);
      if (textViewcityname == null) {
        break missingId;
      }

      id = R.id.textViewdate;
      TextView textViewdate = ViewBindings.findChildViewById(rootView, id);
      if (textViewdate == null) {
        break missingId;
      }

      id = R.id.textViewday;
      TextView textViewday = ViewBindings.findChildViewById(rootView, id);
      if (textViewday == null) {
        break missingId;
      }

      id = R.id.textViewmaxtemp;
      TextView textViewmaxtemp = ViewBindings.findChildViewById(rootView, id);
      if (textViewmaxtemp == null) {
        break missingId;
      }

      id = R.id.textViewmintemp;
      TextView textViewmintemp = ViewBindings.findChildViewById(rootView, id);
      if (textViewmintemp == null) {
        break missingId;
      }

      id = R.id.textViewtemp;
      TextView textViewtemp = ViewBindings.findChildViewById(rootView, id);
      if (textViewtemp == null) {
        break missingId;
      }

      id = R.id.windspeed;
      TextView windspeed = ViewBindings.findChildViewById(rootView, id);
      if (windspeed == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, condition, flow, humidity,
          linearLayout, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6,
          lottieAnimationView2, sea, searchView, sunrise, sunset, textViewCondition, textViewToday,
          textViewcityname, textViewdate, textViewday, textViewmaxtemp, textViewmintemp,
          textViewtemp, windspeed);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
