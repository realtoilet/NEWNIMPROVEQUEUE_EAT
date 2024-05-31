package com.example.queueeat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RV_Cart extends RecyclerView.Adapter<RV_Cart.ViewHolder> {
    public List<ProductClass> list;
    Context c;

    public RV_Cart(List<ProductClass> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public RV_Cart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.cart_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RV_Cart.ViewHolder h, int p) {
        int position = h.getAdapterPosition(); // Store the position

        // Check if the position is valid
        if (position != RecyclerView.NO_POSITION && position < list.size()) {
            ProductClass l = list.get(position);

            // Check if checkoutList and orderList are not empty before using them
            if (!ListOfOrders.checkoutList.isEmpty() && !ListOfOrders.orderList.isEmpty()) {
                // Your existing code for handling onBindViewHolder
            } else {
                // Handle empty lists gracefully or log a message
                Log.e("RV_Cart", "checkoutList or orderList is empty.");
            }

            h.a.setOnClickListener(v -> {
                l.setItemQuantity(l.getItemQuantity() + 1);
                h.quantity.setText(String.valueOf(l.getItemQuantity()));

                for (ProductClass e : ListOfOrders.checkoutList) {
                    if (e.getDocId().equals(l.getDocId())) {
                        e.setItemQuantity(l.getItemQuantity());
                    }
                }
                h.price.setText("₱" + l.getItemPrice() * l.getItemQuantity());
                printList(ListOfOrders.checkoutList);
                CartFragment.updateTextview();
            });

            h.m.setOnClickListener(v -> {
                if (l.getItemQuantity() > 1) {
                    l.setItemQuantity(l.getItemQuantity() - 1);
                    h.quantity.setText(String.valueOf(l.getItemQuantity()));
                    for (ProductClass e : ListOfOrders.checkoutList) {
                        if (e.getDocId().equals(l.getDocId())) {
                            e.setItemQuantity(l.getItemQuantity());
                        }
                    }
                }
                h.price.setText("₱" + l.getItemPrice() * l.getItemQuantity());
                printList(ListOfOrders.checkoutList);
                CartFragment.updateTextview();
            });

            h.name.setText(l.getItemName());
            h.price.setText("₱" + l.getItemPrice() * l.getItemQuantity());
            h.quantity.setText(String.valueOf(l.getItemQuantity()));
            Glide.with(c).load(l.getItemURL()).into(h.iv);

            h.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    ListOfOrders.checkoutList.add(new ProductClass(l.getItemName(), l.getItemURL(), l.getItemPrice(), l.getItemQuantity(), l.getDocId(), l.getItemStocks(), l.getItemType()));
                } else {
                    for (int i = 0; i < ListOfOrders.checkoutList.size(); i++) {
                        if (ListOfOrders.checkoutList.get(i).getItemName().equals(l.getItemName())) {
                            ListOfOrders.checkoutList.remove(i);
                            break;
                        }
                    }
                }
                CartFragment.updateButton();
                CartFragment.updateTextview();
                printList(ListOfOrders.orderList);
            });

            h.delete.setOnClickListener(v -> {
                for (int i = 0; i < ListOfOrders.orderList.size(); i++) {
                    if (ListOfOrders.orderList.get(i).getItemName().equals(l.getItemName())) {
                        ListOfOrders.orderList.remove(i);
                        this.notifyItemRemoved(position);
                        break;
                    }
                }
                CartFragment.updateButton();
                CartFragment.updateTextview();
            });

            CartFragment.cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                for (int i = 0; i < getItemCount(); i++) {
                    ViewHolder holder = (ViewHolder) CartFragment.rv.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        holder.cb.setChecked(isChecked);
                    }
                }
                CartFragment.updateTextview();
            });

        } else {
            // Handle invalid position gracefully or log a message
            Log.e("RV_Cart", "Invalid position: " + position);
        }
    }



    public void printList(List<ProductClass> e) {
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-20s | %-10s | %-10s | %-10s%n", "Item Name", "Price", "Quantity", "DocId");
        System.out.println("------------------------------------------------------------------");
        for (ProductClass order : e) {
            System.out.printf("%-20s | %-10.2f | %-10d | %-10s%n", order.getItemName(), order.getItemPrice(), order.getItemQuantity(), order.getDocId());
        }
        System.out.println("------------------------------------------------------------------");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView name, price;
        CheckBox cb;
        ImageButton delete;
        TextInputEditText quantity;
        CircleImageView a, m;

        public ViewHolder(@NonNull View i) {
            super(i);
            iv = i.findViewById(R.id.img);
            name = i.findViewById(R.id.cart_itemName);
            price = i.findViewById(R.id.cart_itemPrice);
            cb = i.findViewById(R.id.cb);
            delete = i.findViewById(R.id.imageButton);
            quantity = i.findViewById(R.id.quantity);
            a = i.findViewById(R.id.add);
            m = i.findViewById(R.id.min);
        }
    }
}