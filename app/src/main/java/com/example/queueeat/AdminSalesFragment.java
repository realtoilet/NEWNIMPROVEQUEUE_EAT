package com.example.queueeat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminSalesFragment extends Fragment {

    public AdminSalesFragment() {
        // Required empty public constructor
    }

    TextView totalsold, profit;
    EditText capital;
    double totalsales = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_sales, container, false);

        totalsold = v.findViewById(R.id.numOfSold);
        profit = v.findViewById(R.id.TotalIncome);
        FirebaseUtils.getAllSales(FirebaseFirestore.getInstance(), new FirebaseUtils.GetAllSalesFromTimeStamp() {
            @Override
            public void getSales(double sales, int orderscount) {
                totalsold.setText("₱"+sales);
                totalsales = sales;
            }
        });

        capital = v.findViewById(R.id.numOfcapital);

        capital.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty()){
                    profit.setText("₱"+totalsales);
                } else {
                    int capitalint = Integer.parseInt(capital.getText().toString());

                    if(capitalint > 0){
                        profit.setText(String.valueOf(totalsales - capitalint));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}