package com.example.nokarii;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.nokarii.databinding.ActivityDashboardBinding;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.group));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_profile));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.portfolio));
        binding.bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_home));

        binding.bottomNavigation.show(1, true);

        // Remove this line since it replaces the fragment immediately after adding it
         getSupportFragmentManager().beginTransaction().replace(R.id.myContainer, new UserFragment()).commit();

        binding.bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()) {
                    case 1:
                        replace(new UserFragment());
                        break;

                    case 2:
                        replace(new MyJobFragment());
                        break;

                    case 3:
                         replace(new CategoryUserFragment());
                        break;

                    case 4:
                        Intent intent= new Intent(DashboardActivity.this,UserProfileActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return null;
            }
        });
    }

    private void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.myContainer, fragment);
        transaction.addToBackStack(null); // Add this line to enable back navigation
        transaction.commit();
    }
}
