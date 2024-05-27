package com.example.queueeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminSalesFragment extends Fragment {

    public AdminSalesFragment() {
        // Required empty public constructor
    }

    TextView totalsold, profit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_sales, container, false);

        totalsold = v.findViewById(R.id.numOfSold);

        FirebaseUtils.getAllSales(FirebaseFirestore.getInstance(), new FirebaseUtils.GetAllSalesFromTimeStamp() {
            @Override
            public void getSales(double sales, int orderscount) {
                totalsold.setText("₱"+sales);
            }
        });
        return v;
    }
}