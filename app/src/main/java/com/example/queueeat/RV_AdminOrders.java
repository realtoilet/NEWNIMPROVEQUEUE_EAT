package com.example.queueeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RV_AdminOrders extends RecyclerView.Adapter<RV_AdminOrders.VH> {
    List<OrderClass> list;
    Context c;

    public RV_AdminOrders(List<OrderClass> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public RV_AdminOrders.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(c).inflate(R.layout.rv_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RV_AdminOrders.VH h, int p) {
        OrderClass oc = list.get(p);

        h.name.setText(oc.getEmail());
        h.num.setText(String.valueOf(oc.getQueueNumber()));

        StringBuilder orderDetails = new StringBuilder();
        for (ForOrderClass orderItem : oc.getOrder()) {
            orderDetails.append("x").append(orderItem.getItemQuantity()).append(" - ").append(orderItem.getItemName()).append("\n");
        }
        h.order.setText(orderDetails.toString().trim());

        h.accept.setOnClickListener(v -> {
            FirebaseUtils.moveQueue(FirebaseFirestore.getInstance(), oc.getDocID());
            list.remove(p);
            notifyItemRemoved(p);
            updateQueueNumbers(p);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateQueueNumbers(int removedPosition) {
        for (int i = removedPosition; i < list.size(); i++) {
            OrderClass order = list.get(i);
            order.setQueueNumber(order.getQueueNumber() - 1);
            notifyItemChanged(i);
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView name, order, num;
        Button accept;

        public VH(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.name);
            order = v.findViewById(R.id.order);
            num = v.findViewById(R.id.num);
            accept = v.findViewById(R.id.accept);
        }
    }
}
