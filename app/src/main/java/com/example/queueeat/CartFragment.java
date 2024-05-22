package com.example.queueeat;

import static com.example.queueeat.HomeFragment.user;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartFragment extends Fragment {
    HomePage home;
    static RecyclerView rv;
    RV_Cart adapter;
    List<ProductClass> list;
    static Button btn_popup;
    static CheckBox cb;
    static TextView price;

    // List to hold child fragments
    private List<Fragment> childFragments;
    int docSize = 0;
    QueueFragment queueFragment = new QueueFragment(user);

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        btn_popup = v.findViewById(R.id.btn_checkout);
        cb = v.findViewById(R.id.cb);
        price = v.findViewById(R.id.amount_total);
        price.setText("0");
        home = (HomePage) getActivity();
        btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiag();
            }
        });

        list = ListOfOrders.orderList;

        rv = v.findViewById(R.id.recyclerView);
        adapter = new RV_Cart(list, getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        updateButton();

        // Get child fragments
        getChildFragments();

        // Set up checkbox listeners
        setupCheckboxListeners();

        return v;
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

        vp.setAdapter(new ScreenSlidePageAdapter(this));
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

        btn.setOnClickListener(v -> {
            List<ForOrderClass> f = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();

            for(ProductClass p : ListOfOrders.checkoutList){
                f.add(new ForOrderClass(p.getItemName(), p.getItemPrice(), p.getItemQuantity()));
            }

            FirebaseUtils.returnDocumentCount(FirebaseFirestore.getInstance(), new FirebaseUtils.CountCallback() {
                @Override
                public void docCount(int count) {
                    int queuenum = (count > 0) ? count + 1 : 1;

                    m.put("user", SharedPrefUtils.returnUsernameForData(getContext()));
                    m.put("orderList", f);
                    m.put("seatNumber", ListOfOrders.currSeat);
                    m.put("queueNumber", queuenum);
                    m.put("queue", true);
                    m.put("timestamp", FieldValue.serverTimestamp());
                    FirebaseUtils.sendOrder(m, FirebaseFirestore.getInstance());
                    Toast.makeText(home, "Thanks for ordering", Toast.LENGTH_SHORT).show();
                    HomePage.vp2.setCurrentItem(HomePage.vp2.getCurrentItem() - 1);
                    d.dismiss();

                }

            });

        });
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(CartFragment ac) {
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

    @Override
    public void onResume() {
        super.onResume();
        list = ListOfOrders.orderList;
        adapter.notifyDataSetChanged();
    }

    public static void updateButton() {
        if (ListOfOrders.checkoutList.size() > 0) {
            btn_popup.setVisibility(View.VISIBLE);
        } else {
            btn_popup.setVisibility(View.INVISIBLE);
        }
    }
    public static void updateTextview() {
        double total = 0;
        for (int i = 0; i < ListOfOrders.checkoutList.size(); i++) {
            total += ListOfOrders.checkoutList.get(i).getItemPrice() * ListOfOrders.checkoutList.get(i).getItemQuantity();
        }
        price.setText(total + ""); // Format the price to 2 decimal places
    }

    // Method to retrieve child fragments
    private void getChildFragments() {
        childFragments = getChildFragmentManager().getFragments();
    }

    // Method to set up checkbox listeners
    private void setupCheckboxListeners() {
        if (childFragments != null) {
            for (Fragment fragment : childFragments) {
                if (fragment instanceof SeatsSelectorFragment) {
                    SeatsSelectorFragment seatsFragment = (SeatsSelectorFragment) fragment;

                    for (CheckBox checkBox : seatsFragment.getCheckBoxList()) {
                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                // Uncheck checkboxes in other fragments
                                for (Fragment otherFragment : childFragments) {
                                    if (otherFragment != fragment && otherFragment instanceof SeatsSelectorFragment) {
                                        SeatsSelectorFragment otherSeatsFragment = (SeatsSelectorFragment) otherFragment;
                                        otherSeatsFragment.clearCheckedCheckbox();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}