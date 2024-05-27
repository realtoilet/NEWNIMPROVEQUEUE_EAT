package com.example.queueeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class rv_receipt extends RecyclerView.Adapter<rv_receipt.ViewHolder> {
    Context c;
    List<ForOrderClass> l;

    public rv_receipt(Context c, List<ForOrderClass> l) {
        this.c = c;
        this.l = l;
    }

    @NonNull
    @Override
    public rv_receipt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.rv_receipt, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull rv_receipt.ViewHolder h, int p) {
        ForOrderClass f = l.get(p);

        h.quantity.setText(String.valueOf(f.getItemQuantity()));
        h.name.setText(f.getItemName());
        h.price.setText(String.valueOf(f.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return l.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        public ViewHolder(@NonNull View v) {
            super(v);

            name = v.findViewById(R.id.itemname);
            price = v.findViewById(R.id.ReciptItemPrice);
            quantity = v.findViewById(R.id.itemQty);
        }
    }
}
