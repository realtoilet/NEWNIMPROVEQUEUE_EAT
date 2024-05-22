package com.example.queueeat;

import android.content.Context;
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
        ProductClass l = list.get(p);

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
            ProductClass l1 = list.get(h.getAdapterPosition());
            if (isChecked) {
                ListOfOrders.checkoutList.add(new ProductClass(l1.getItemName(), l1.getItemURL(), l1.getItemPrice(), l1.getItemQuantity(), l1.getDocId(), l1.getItemStocks(),l1.getItemType()));
            } else {
                for (int i = 0; i < ListOfOrders.checkoutList.size(); i++) {
                    if (ListOfOrders.checkoutList.get(i).getItemName().equals(l1.getItemName())) {
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
            ProductClass l1 = list.get(h.getAdapterPosition());

            for (int i = 0; i < ListOfOrders.orderList.size(); i++) {
                if (ListOfOrders.orderList.get(i).getItemName().equals(l1.getItemName())) {
                    ListOfOrders.orderList.remove(i);
                    this.notifyItemRemoved(h.getAdapterPosition());
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
