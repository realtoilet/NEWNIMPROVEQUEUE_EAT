package com.example.queueeat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class AdminHomePage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager2 vp2;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Uri imgAsUri;
    FloatingActionButton opendg;
    ImageView iv;
    AdminServingsFragment servingsFragment = new AdminServingsFragment();
    AdminSalesFragment salesFragment = new AdminSalesFragment();
    AdminSeatsFragment seatsFragment = new AdminSeatsFragment();
    AdminProfileFragment adminuserFragment = new AdminProfileFragment();
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        bottomNavigationView = findViewById(R.id.admin_bottom_navigation);
        bottomNavigationView.setBackground(null);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow(); // Initialize window variable properly
            window.setStatusBarColor(getResources().getColor(R.color.cyan));
        }

        startResultLauncher();
       bottomNavigationView = findViewById(R.id.admin_bottom_navigation );
        vp2 = findViewById(R.id.vp21);

        vp2.setAdapter(new ScreenSlidePageAdapter(this));

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.servings) {
                    vp2.setCurrentItem(0);
                    return true;
                } else if (menuItem.getItemId() == R.id.sales) {
                    vp2.setCurrentItem(1);
                    return true;
                } else if (menuItem.getItemId() == R.id.seats) {
                    vp2.setCurrentItem(2);
                    return true;
                } else if (menuItem.getItemId() == R.id.adminUserProfile) {
                    vp2.setCurrentItem(3);
                    return true;
                }
                return false;
            }

        });

        opendg = findViewById(R.id.btn_addToServings);

        opendg.setOnClickListener(v->{
            openDiag();
        });

    }

    public void openDiag(){
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.addproductdialouge);
        d.setCancelable(true);
        d.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setGravity(Gravity.CENTER);

        d.show();

        TextView name = d.findViewById(R.id.productName);
        TextView price = d.findViewById(R.id.productPrice);
        TextView quantity = d.findViewById(R.id.productQTY);
        AppCompatRadioButton meals = d.findViewById(R.id.Meals);
        AppCompatRadioButton snacks = d.findViewById(R.id.Snacks);
        FrameLayout getImg = d.findViewById(R.id.addImage);
        FrameLayout add = d.findViewById(R.id.addProduct);
        iv = d.findViewById(R.id.imageProduct);

        FirebaseUtils fu = new FirebaseUtils();

        // Set up mutual exclusivity for radio buttons
        meals.setOnClickListener(v -> {
            if (meals.isChecked()) {
                snacks.setChecked(false);
                type = "Meals";
            }
        });

        snacks.setOnClickListener(v -> {
            if (snacks.isChecked()) {
                meals.setChecked(false);
                type = "Snacks";
            }
        });

        getImg.setOnClickListener(vi -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        add.setOnClickListener(vi -> {
            String priceText = price.getText().toString();
            String itemQuantity = quantity.getText().toString();
            if (name.getText().toString().isEmpty() || priceText.isEmpty() || itemQuantity.isEmpty() || imgAsUri == null) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            } else if (priceText.length() > 5) {
                Toast.makeText(this, "Price can't exceed 5 characters", Toast.LENGTH_SHORT).show();
            } else if (itemQuantity.length() > 5) {
                Toast.makeText(this, "Quantity can't exceed 5 characters", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> m = new HashMap<>();
                m.put("itemName", name.getText().toString());
                m.put("itemPrice", Float.parseFloat(priceText));
                m.put("itemQuantity", Integer.parseInt(itemQuantity));
                m.put("Type", type);
                fu.addNewProduct(m, imgAsUri, FirebaseFirestore.getInstance(), FirebaseStorage.getInstance().getReference().child("product_images/" + name.getText().toString()));
                d.dismiss();
            }
        });
    }


    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(AdminHomePage ac) {
            super(ac);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0: return servingsFragment;
                case 1: return salesFragment;
                case 2: return seatsFragment;
                case 3: return adminuserFragment;
                default: return null;
            }
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
    public void startResultLauncher(){
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        imgAsUri = uri;
                        iv.setImageURI(imgAsUri);
                    }
                });
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
                        AdminHomePage.super.onBackPressed();
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