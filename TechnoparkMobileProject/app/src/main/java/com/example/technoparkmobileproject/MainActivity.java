package com.example.technoparkmobileproject;

import android.os.Bundle;

import com.example.technoparkmobileproject.ui.news.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity /*implements NewsFragment.OnItemSelectedListener*/ {

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
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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

