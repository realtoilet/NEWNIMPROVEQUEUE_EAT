//package com.example.queueeat;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {
//    FirebaseFirestore firestore = FirebaseFirestore.();
//
//    Context c;
//
//    public ReceiptAdapter(List<StockClass> s, Context c) {
//        this.s = s;
//        this.c = c;
//    }
//
//    @NonNull
//    @Override
//    public ReceiptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_receipt, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReceiptAdapter.ViewHolder h, int position) {
//
//
//            }
//
//    @Override
//    public int getItemCount() {
//        return s.size();
//    }
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView itemName, quantity, Price;
//
//        public ViewHolder(@NonNull View v) {
//            super(v);
//            itemName =v.findViewById(R.id.itemname);
//            quantity =v.findViewById(R.id.itemQty);
//            Price =v.findViewById(R.id.ReciptItemPrice);
//
//        }
//    }
//}
