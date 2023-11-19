package com.example.testcw.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.testcw.fragments.AddHikeFragment;
import com.example.testcw.fragments.HomeFragment;
import com.example.testcw.R;
import com.example.testcw.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFrameLayout(new HomeFragment());

        navigationView = findViewById(R.id.navMenu);
        navigationView.setSelectedItemId(R.id.navHome);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navHome){
                replaceFrameLayout(new HomeFragment());
            } else if (item.getItemId() == R.id.navAdd){
                replaceFrameLayout(new AddHikeFragment());
            } else if (item.getItemId() == R.id.navSearch){
                replaceFrameLayout(new SearchFragment());
            }
            return true;
        });

    }
    private void replaceFrameLayout(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}