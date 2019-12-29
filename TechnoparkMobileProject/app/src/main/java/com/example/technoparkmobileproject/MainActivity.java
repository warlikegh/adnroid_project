package com.example.technoparkmobileproject;

import android.os.Bundle;

import com.example.technoparkmobileproject.ui.news.NewsFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends FragmentActivity implements Router{

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onProfileSelected(int id, String username) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putInt("id", id);
        NavOptions.Builder builder = new NavOptions.Builder();
        NavOptions navOptions = builder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).build();
        navController.navigate(R.id.navigation_notifications, bundle, navOptions);
    }

    @Override
    public void onGroupSelected(Integer id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        NavOptions.Builder builder = new NavOptions.Builder();
        NavOptions navOptions = builder.setEnterAnim(R.anim.nav_default_enter_anim).setExitAnim(R.anim.nav_default_exit_anim).build();
        navController.navigate(R.id.groupFragment, bundle, navOptions);
    }
}
