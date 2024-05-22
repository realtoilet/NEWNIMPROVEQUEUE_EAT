package com.example.queueeat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomePage extends AppCompatActivity implements RecyclerViewAdapter.CheckForNewData {

    BottomNavigationView bottomNavigationView;
    static ViewPager2 vp2;
    Context context;
    RecyclerViewAdapter rv;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.cyan));
        }

        rv = new RecyclerViewAdapter(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        vp2 = findViewById(R.id.vp2);

        vp2.setAdapter(new ScreenSlidePageAdapter(this));
        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int selectedId = item.getItemId();

                if (selectedId == R.id.Home) {
                    // If "Home" is selected and "Queue" is not already selected, select "Queue"
                    if (selectedId != R.id.queue) {
                        bottomNav.setSelectedItemId(R.id.queue);
                    }
                    // (Optional) Add logic to handle Home selection here
                    return true;
                } else if (selectedId == R.id.queue) {
                    // If "Queue" is selected, do nothing (already selected)
                    return true;
                }
                return false; // Handle other selections (optional)
            }
        });



        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.Home) {
                    vp2.setCurrentItem(0);
                    return true;
                } else if (menuItem.getItemId() == R.id.queue) {
                    vp2.setCurrentItem(1);
                    return true;
                } else if (menuItem.getItemId() == R.id.shoppingCart) {
                    vp2.setCurrentItem(2);
                    return true;
                } else if (menuItem.getItemId() == R.id.userProfile) {
                    vp2.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void getNewData(List<ProductClass> productList) {
        // Implement your logic for getting new data
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment(getIntent().getStringExtra("user"));
                case 1:
                    return new QueueFragment(getIntent().getStringExtra("user"));
                case 2:
                    return new CartFragment();
                case 3:
                    return new UserFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

    @Override
    public void onBackPressed() {
        if (false) {
            super.onBackPressed();
        }


        new AlertDialog.Builder(this)
                .setTitle("Exit App").setMessage("Are you sure you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomePage.super.onBackPressed();
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }
}



