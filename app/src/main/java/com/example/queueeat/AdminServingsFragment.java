package com.example.queueeat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminServingsFragment extends Fragment {

    static List<StockClass> stocks = new ArrayList<>();
    static List<StockClass> filteredStocks = new ArrayList<>();
    static RecyclerView rv;
    static StocksAdapter adapter;
    EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_servings, container, false);

        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUtils.retrieveAllServings(FirebaseFirestore.getInstance(), getContext());

        // Initialize the adapter with an empty list
        adapter = new StocksAdapter(filteredStocks, getContext());
        rv.setAdapter(adapter);

        searchEditText = v.findViewById(R.id.searchUserEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return v;
    }

    public static void addToRecyclerview(List<StockClass> s, Context c) {
        stocks.clear();
        stocks.addAll(s);

        // Ensure the adapter field is assigned and notify changes
        if (adapter == null) {
            adapter = new StocksAdapter(filteredStocks, c);
            rv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void filter(String text) {
        filteredStocks.clear();
        if (text.isEmpty()) {
            filteredStocks.addAll(stocks);
        } else {
            for (StockClass stock : stocks) {
                if (stock.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredStocks.add(stock);
                }
            }
        }

        // Ensure the adapter is not null before notifying changes
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            Log.e("AdminServingsFragment", "Adapter is null in filter method");
        }
    }
}
