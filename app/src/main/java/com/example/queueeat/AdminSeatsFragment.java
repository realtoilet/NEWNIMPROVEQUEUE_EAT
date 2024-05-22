package com.example.queueeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminSeatsFragment extends Fragment {

    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_orders, container, false);
        rv = v.findViewById(R.id.orders_rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUtils.retrieveAllOrders(FirebaseFirestore.getInstance(), new FirebaseUtils.QueueListener() {
            @Override
            public void newQueue(List<OrderClass> list) {
                rv.setAdapter(new RV_AdminOrders(list, getContext()));
            }
        });

        return v;
    }
}