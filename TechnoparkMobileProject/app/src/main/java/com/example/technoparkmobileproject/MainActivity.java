package com.example.technoparkmobileproject;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.technoparkmobileproject.ui.news.NewsFragment;

import com.example.technoparkmobileproject.ui.profile.ProfileFragment;
import com.example.technoparkmobileproject.ui.shedule.ScheduleFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity /*implements NewsFragment.OnItemSelectedListener*/ {

    NavController navController;

    private List<Fragment> fragments = new ArrayList<>(3);
    Integer fragmentNumber;
    String FRAGMENT_NUMBER = "fragment_number";

    private void buildFragmentsList() {
        NewsFragment newsFragment = new NewsFragment();
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        fragments.add(newsFragment);
        fragments.add(scheduleFragment);
        fragments.add(profileFragment);
    }

    private void switchFragment(int pos) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragments.get(pos))
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragmentNumber = 0;
                                switchFragment(fragmentNumber);
                                return true;
                            case R.id.navigation_dashboard:
                                fragmentNumber = 1;
                                switchFragment(fragmentNumber);
                                return true;
                            case R.id.navigation_notifications:
                                fragmentNumber = 2;
                                switchFragment(fragmentNumber);
                                return true;
                        }
                        return false;
                    }
                });
        buildFragmentsList();
        if (savedInstanceState != null) {
            fragmentNumber = savedInstanceState.getInt(FRAGMENT_NUMBER, 0);
        } else {
            fragmentNumber = 0;
        }
        switchFragment(fragmentNumber);

     /*   BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_NUMBER, fragmentNumber);
    }

    /*   public void onItemSelected(int id) {
        navController.navigate(R.id.articleFragment);
        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment,  ArticleFragment.newInstance(result))
                .addToBackStack(null)
                .commit();
    }*/
}

