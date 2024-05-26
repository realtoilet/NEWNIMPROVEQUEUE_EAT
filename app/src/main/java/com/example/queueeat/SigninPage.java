package com.example.queueeat;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.queueeat.databinding.ActivitySigninPageBinding;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninPage extends AppCompatActivity {
  static ViewPager vp2;
    public ActivitySigninPageBinding bind;
    public ViewPager2 vp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivitySigninPageBinding.inflate(getLayoutInflater());
        View v = bind.getRoot();
        setContentView(v);

        // Apply adjust pan behavior
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow(); // Initialize window variable properly
            window.setStatusBarColor(getResources().getColor(R.color.cyan));
        }

        bind.vp2.setAdapter(new ScreenSlidePageAdapter(this));

        if(SharedPrefUtils.checkAccount(this)){
            if(SharedPrefUtils.checkType(this)){
                startActivity(new Intent(SigninPage.this, HomePage.class));
            } else {
                startActivity(new Intent(SigninPage.this, AdminHomePage.class));
            }
        }

    }


    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(SigninPage ac) {
            super(ac);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment(bind.vp2);
                case 1:
                    return new SignupFragment(bind.vp2);
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
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
                        SigninPage.super.onBackPressed();
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
