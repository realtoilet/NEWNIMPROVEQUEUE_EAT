package com.example.queueeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {
    List<StockClass> s;
    Context c;

    public StocksAdapter(List<StockClass> s, Context c) {
        this.s = s;
        this.c = c;
    }

    @NonNull
    @Override
    public StocksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.stocks, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StocksAdapter.ViewHolder h, int position) {
        StockClass sc = s.get(position);

        h.delete.setOnClickListener(v-> {
            for (int i = 0; i < s.size(); i++) {
                s.remove(i);
                this.notifyItemRemoved(i);
                FirebaseUtils.removeProduct(FirebaseFirestore.getInstance(), sc.getDocID());
            }
        });
        h.itemName.setText(sc.getName());
        h.type.setText(sc.getType());
        h.itemStocks.setText(String.valueOf(sc.getStocks()));

        h.add.setOnClickListener(v->{
            sc.setStocks(sc.getStocks() + 1);
            h.itemStocks.setText(String.valueOf(sc.getStocks()));
        });

        h.min.setOnClickListener(v->{
            if(sc.getStocks() > 0){
                sc.setStocks(sc.getStocks() - 1);
            }
            h.itemStocks.setText(String.valueOf(sc.getStocks()));
        });
    }

    @Override
    public int getItemCount() {
        return s.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName, itemStocks, type;
        CircleImageView add, min;
        ImageButton delete;
        public ViewHolder(@NonNull View v) {
            super(v);

            itemName = v.findViewById(R.id.name);
            itemStocks = v.findViewById(R.id.stock);
            add = v.findViewById(R.id.add);
            min = v.findViewById(R.id.min);
            type = v.findViewById(R.id.type);
            delete = v.findViewById(R.id.imgbtn_delete);
        }
    }
}
