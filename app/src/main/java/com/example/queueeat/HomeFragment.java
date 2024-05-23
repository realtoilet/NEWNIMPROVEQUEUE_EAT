package com.example.queueeat;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import  android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    private static final String ARG_USER = "user";
    static String user;

    List<ProductClass> p;
    static RecyclerView recyclerView;
    TextView homeusername;
    View a,b;

    public HomeFragment(String user){
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));



        FirebaseUtils.retrieveAllProducts(FirebaseFirestore.getInstance(), getContext());

        // Execute this when the app starts and meals button is preselected
        FirebaseUtils.filterAllProducts(FirebaseFirestore.getInstance(), getContext(), "Meals");

        homeusername = view.findViewById(R.id.HomeUsername);
        a = view.findViewById(R.id.fakeButton_Snacks);
        b = view.findViewById(R.id.fakeButton_meals);

        // Set the initial state: meals button selected
        setColors((LinearLayout) b, true, R.id.mealsicon, R.id.tv_meals);
        setColors((LinearLayout) a, false, R.id.Snacks, R.id.tv_snacks);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setEnabled(false);
                b.setEnabled(true);

                // Set the selected state for fakeButton_Snacks
                setColors((LinearLayout) a, true, R.id.Snacks, R.id.tv_snacks);

                // Reset the state for fakeButton_meals
                setColors((LinearLayout) b, false, R.id.mealsicon, R.id.tv_meals);

                FirebaseUtils.filterAllProducts(FirebaseFirestore.getInstance(), getContext(), "Snacks");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(false);
                a.setEnabled(true);

                // Set the selected state for fakeButton_meals
                setColors((LinearLayout) b, true, R.id.mealsicon, R.id.tv_meals);

                // Reset the state for fakeButton_Snacks
                setColors((LinearLayout) a, false, R.id.Snacks, R.id.tv_snacks);

                FirebaseUtils.filterAllProducts(FirebaseFirestore.getInstance(), getContext(), "Meals");
            }
        });

        homeusername.setText(SharedPrefUtils.returnUsernameForData(getContext()));
        user = SharedPrefUtils.returnUsernameForData(getContext());
        return view;
    }

    private void setColors(LinearLayout layout, boolean selected, int imageViewId, int textViewId) {
        ImageView imageView = layout.findViewById(imageViewId);
        TextView textView = layout.findViewById(textViewId);

        if (imageView != null && textView != null) {
            if (selected) {
                imageView.setColorFilter(getResources().getColor(R.color.btn_color));
                textView.setTextColor(getResources().getColor(R.color.btn_color));
                layout.setBackgroundResource(R.drawable.bttn_bg_selected);  // Only change background for fakeButton_meals
                layout.setPadding(20, 20, 20, 20);  // Add padding to avoid overlap
            } else {
                imageView.clearColorFilter();
                textView.setTextColor(getResources().getColor(R.color.icon_btn_color));
                layout.setBackgroundResource(R.drawable.btn_bg);  // Reset background for other buttons
                layout.setPadding(10, 10, 10, 10);  // Reset padding
            }
        } else {
            Log.e("MyApp", "ImageView or TextView is null in setColors");
        }
    }







    public static void addToRecyclerView(List<ProductClass> p, Context c){
        recyclerView.setAdapter(new RecyclerViewAdapter(p, c, user));
    }
    public static void filterRecyclerView(List<ProductClass> p, Context c, String filter){
        List<ProductClass> b = new ArrayList<>();
        for(ProductClass item : p){
            if(item.getItemType().equals(filter)){
                b.add(item);
            }
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(b,c,user));
    }
}
