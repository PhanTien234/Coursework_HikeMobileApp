package com.example.HikeAppCW.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.HikeAppCW.fragments.AddHikeFragment;
import com.example.HikeAppCW.fragments.HikeFragment;
import com.example.HikeAppCW.R;
import com.example.HikeAppCW.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFrameLayout(new HikeFragment());

        navigationBar = findViewById(R.id.navMenu);
        navigationBar.setSelectedItemId(R.id.navHome);

        navigationBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navHome){
                replaceFrameLayout(new HikeFragment());
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