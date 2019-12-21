package com.example.technoparkmobileproject;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements NewsFragment.OnProfileSelectedListener {
/*
    private List<Fragment> fragments = new ArrayList<>(3);
    Integer fragmentNumber;
    String FRAGMENT_NUMBER = "fragment_number";
    BottomNavigationView bottomNavigationView;
    String USERNAME = "username";
    String ID = "id";
    String IS_OTHER = "is_other";
    Integer lastId;
    String lastUsername;
    Boolean isOther;

    private void buildFragmentsList() {
        NewsFragment newsFragment = new NewsFragment();
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        ProfileFragment profileFragment = ProfileFragment.newInstance(-1, "my");

        fragments.add(newsFragment);
        fragments.add(scheduleFragment);
        fragments.add(profileFragment);
    }

    private void switchFragment(int pos) {
        fragmentNumber = pos;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragments.get(pos))
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //   isOther=false;


        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                fragmentNumber = 0;
                                isOther = false;
                                switchFragment(fragmentNumber);
                                return true;
                            case R.id.navigation_dashboard:
                                fragmentNumber = 1;
                                switchFragment(fragmentNumber);
                                return true;
                            case R.id.navigation_notifications:
                                fragmentNumber = 2;
                                //      lastId = -1;
                                //      lastUsername = "my";
                                switchFragment(fragmentNumber);
                                return true;
                        }
                        return false;
                    }
                });
        buildFragmentsList();
        if (savedInstanceState != null) {
            isOther = savedInstanceState.getBoolean(IS_OTHER);
            fragmentNumber = savedInstanceState.getInt(FRAGMENT_NUMBER, 0);
            lastId = savedInstanceState.getInt(ID);
            lastUsername = savedInstanceState.getString(USERNAME);
        } else {
            fragmentNumber = 0;
            isOther = false;
        }
        if (fragmentNumber == 4) {
            //   lastId = savedInstanceState.getInt(ID);
            //   lastUsername = savedInstanceState.getString(USERNAME);
            onProfileSelected(lastId, lastUsername);
        } else {
            switchFragment(fragmentNumber);
        }
    }*/
/*
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_NUMBER, fragmentNumber);
        if (lastId == null) {
            outState.putInt(ID, -1);
            outState.putString(USERNAME, "my");
        } else {
            outState.putInt(ID, lastId);
            outState.putString(USERNAME, lastUsername);
        }
        outState.putBoolean(IS_OTHER, isOther);
    }

    @Override
    public void onBackPressed() {
        if (fragmentNumber == 0) {
            finish();
        } else {
            if (isOther && fragmentNumber != 4) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                onProfileSelected(lastId, lastUsername);

            } else {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                fragmentNumber = 0;
            }
        }
    }
*/


    /*

        NavController navController;
        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                     /*   if (fragmentNumber != NEWS_FRAGMENT_NUMBER) {
                            ft.replace(R.id.nav_host_fragment, NewsFragment.newInstance());
                            ft.addToBackStack(null);
                            ft.commit();
                            fragmentNumber = NEWS_FRAGMENT_NUMBER;
                        }
                        navController.navigate(R.id.navigation_home);
                        return true;
                    case R.id.navigation_dashboard:
                       /* if (fragmentNumber != SCHEDULE_FRAGMENT_NUMBER) {
                            ft.replace(R.id.nav_host_fragment, ScheduleFragment.newInstance());
                            ft.commit();
                            fragmentNumber = SCHEDULE_FRAGMENT_NUMBER;
                        }
                        navController.navigate(R.id.navigation_dashboard);
                        return true;
                    case R.id.navigation_notifications:
                        /*if (fragmentNumber != PROFILE_FRAGMENT_NUMBER) {
                            ft.replace(R.id.nav_host_fragment, ProfileFragment.newInstance());
                            ft.commit();
                            fragmentNumber = PROFILE_FRAGMENT_NUMBER;
                        }
                        navController.navigate(R.id.navigation_notifications);
                        return true;
                }
                return false;
            }
        };*/
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //   NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }


    @Override
    public void onProfileSelected(int id, String username) {
    /*  //  lastId = id;
      //  lastUsername = username;
      //    Log.d("profile",lastUsername);
       // isOther = true;
     //   fragmentNumber = 4;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, ProfileFragment.newInstance(id, username))
                .addToBackStack(null)
                .commit();*/
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putInt("id", id);
        NavOptions.Builder builder = new NavOptions.Builder();
        NavOptions navOptions = builder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).build();
        navController.navigate(R.id.navigation_notifications, bundle, navOptions);
        // navController.popBackStack(R.id.navigation_notifications,false);
    }


}



















