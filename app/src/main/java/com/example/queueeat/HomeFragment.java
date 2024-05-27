package com.example.queueeat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import  android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import android.text.Editable;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {
    private static final String ARG_USER = "user";
    static String user;

    List<ProductClass> p;
    static RecyclerView recyclerView;
    RecyclerViewAdapter rva;
    TextView homeusername;
    View a,b, c;

    String currentState = "";
    public HomeFragment(String user){
        this.user = user;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        p = new ArrayList<>();
        rva = new RecyclerViewAdapter(p, getContext(), user);
        recyclerView.setAdapter(rva);
        currentState = "Meals";
        filterRecyclerView(p, getContext(), "Meals");
        FirebaseUtils.retrieveAllProducts(FirebaseFirestore.getInstance(), getContext(), new FirebaseUtils.RetrieveAllProductsListener() {
            @Override
            public void products(List<ProductClass> l) {
                p.clear();
                p.addAll(l);
                filterRecyclerView(p, getContext(), currentState);
            }
        });

        homeusername = view.findViewById(R.id.HomeUsername);
        a = view.findViewById(R.id.fakeButton_Snacks);
        b = view.findViewById(R.id.fakeButton_meals);
        c = view.findViewById(R.id.fakeButton_Ownmeal);

        EditText editTextSearch = view.findViewById(R.id.searchUserEditText);

        // Set the initial state: meals button selected
        setColors((LinearLayout) b, true, R.id.mealsicon, R.id.tv_meals);
        setColors((LinearLayout) a, false, R.id.Snacks, R.id.tv_snacks);

        // Set OnClickListener for Snacks button
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setEnabled(false);
                b.setEnabled(true);
                setColors((LinearLayout) a, true, R.id.Snacks, R.id.tv_snacks);
                setColors((LinearLayout) b, false, R.id.mealsicon, R.id.tv_meals);
                currentState = "Snacks";
                filterRecyclerView(p, getContext(), "Snacks");
            }
        });

        // Set OnClickListener for Meals button
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setEnabled(false);
                a.setEnabled(true);
                setColors((LinearLayout) b, true, R.id.mealsicon, R.id.tv_meals);
                setColors((LinearLayout) a, false, R.id.Snacks, R.id.tv_snacks);
                currentState = "Meals";
                filterRecyclerView(p, getContext(), "Meals");
            }
        });

        c.setOnClickListener(c->{
            openDiag();
        });

        // Set TextChangedListener for EditText
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Perform filtering based on search query
                RecyclerViewAdapter adapter = (RecyclerViewAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        homeusername.setText("hi, "+ SharedPrefUtils.returnUsernameForData(getContext()));
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

    public void openDiag() {
        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.seats_popup_dialog);
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setGravity(Gravity.CENTER);
        d.setCancelable(true);
        d.show();

        ViewPager2 vp = d.findViewById(R.id.vp2);
        ImageView next = d.findViewById(R.id.next);
        ImageView prev = d.findViewById(R.id.prev);
        Button btn = d.findViewById(R.id.btn_next);

        vp.setAdapter(new HomeFragment.ScreenSlidePageAdapter(this));
        vp.setUserInputEnabled(false);

        vp.setOffscreenPageLimit(8);

        next.setOnClickListener(v -> {
            if (vp.getCurrentItem() < 8) {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
        });

        prev.setOnClickListener(v -> {
            if (vp.getCurrentItem() > 0) {
                vp.setCurrentItem(vp.getCurrentItem() - 1);
            }
        });

        btn.setOnClickListener(v->{
            FirebaseUtils.setSeatForOwnMeal(FirebaseFirestore.getInstance());
            HomePage.vp2.setCurrentItem(HomePage.vp2.getCurrentItem() + 1);
            ListOfOrders.state = "ownmeal";
            d.dismiss();
        });
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(HomeFragment ac) {
            super(ac);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SeatsSelectorFragment();
                case 1:
                    return new SeatsSelector_BFragment();
                case 2:
                    return new Seats_Selector_CFragment();
                case 3:
                    return new Seats_Selector_DFragment();
                case 4:
                    return new Seats_Selector_EFragment();
                case 5:
                    return new Seats_Selector_FFragment();
                case 6:
                    return new Seats_Selector_GFragment();
                case 7:
                    return new Seats_Selector_HFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 8;
        }
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
