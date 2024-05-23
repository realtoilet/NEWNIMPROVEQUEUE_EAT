package com.example.queueeat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    private List<ProductClass> pFiltered; // Filtered list for search functionality

    private List<ProductClass> p;
    private Context c;
    public CheckForNewData check = null;
    public String user;

    public interface CheckForNewData {
        void getNewData(List<ProductClass> productList);
    }

    public RecyclerViewAdapter(List<ProductClass> p, Context c, String user) {
        this.user = user;
        this.p = p;
        this.pFiltered = new ArrayList<>(p); // Initialize filtered list with all items
        this.c = c;
    }

    public RecyclerViewAdapter(CheckForNewData check) {
        this.check = check;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        ProductClass pcopy = pFiltered.get(position); // Use filtered list

        Glide.with(c).load(pcopy.getItemURL()).into(holder.itemImage);
        holder.itemName.setText(pcopy.getItemName());
        holder.itemPrice.setText("₱" + pcopy.getItemPrice());

        // Adjust text size for itemName if it exceeds 6 characters
        if (holder.itemName.getText().length() > 4) {
            int originalTextSizeSp = (int) (holder.itemName.getTextSize() / holder.itemName.getResources().getDisplayMetrics().scaledDensity);
            int excessCharacters = holder.itemName.getText().length() - 4;
            int newTextSizeSp = originalTextSizeSp - (3 * excessCharacters);

            if (newTextSizeSp < 23) {
                newTextSizeSp = 23; // Ensure the text size does not go below 23sp
            }

            holder.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, newTextSizeSp);
        }

        // Measure the text width for itemPrice and adjust the text size if it exceeds 100dp
        holder.itemPrice.post(() -> {
            DisplayMetrics metrics = holder.itemPrice.getContext().getResources().getDisplayMetrics();
            float maxTextWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, metrics);

            holder.itemPrice.measure(0, 0);
            float textWidth = holder.itemPrice.getMeasuredWidth();

            if (textWidth > maxTextWidth) {
                int originalTextSizeSp = (int) (holder.itemPrice.getTextSize() / metrics.scaledDensity);
                int excessCharacters = (int) ((textWidth - maxTextWidth) / (textWidth / holder.itemPrice.getText().length()));
                int newTextSizeSp = originalTextSizeSp - (2 * excessCharacters);

                holder.itemPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, newTextSizeSp);
            }
        });

        holder.fakeBtn.setOnClickListener(v -> FirebaseUtils.checkIfOrdered(FirebaseFirestore.getInstance(), user, new FirebaseUtils.CheckForUserOrderListener() {
            @Override
            public void hasOrders(boolean allow) {
                if (allow) {
                    ProductClass pcopy1 = p.get(holder.getAdapterPosition());
                    boolean found = false;
                    for (int i = 0; i < ListOfOrders.orderList.size(); i++) {
                        if (pcopy1.getDocId().equals(ListOfOrders.orderList.get(i).getDocId())) {
                            ListOfOrders.orderList.get(i).setItemQuantity(ListOfOrders.orderList.get(i).getItemQuantity() + 1);
                            found = true;
                            break;
                        } else {
                            found = false;
                        }
                    }

                    if (!found) {
                        ListOfOrders.orderList.add(new ProductClass(pcopy1.getItemName(), pcopy1.getItemURL(), pcopy1.getItemPrice(), pcopy.getItemQuantity() + 1, pcopy.getDocId(), pcopy1.getItemStocks(),pcopy1.getItemType()));
                        if (CartFragment.btn_popup != null) {
                            CartFragment.updateButton();
                        }
                    }
                    System.out.println("------------------------------------------------------------------");
                    System.out.printf("%-20s | %-10s | %-10s | %-10s%n", "Item Name", "Price", "Quantity", "DocId");
                    System.out.println("------------------------------------------------------------------");
                    for (ProductClass order : ListOfOrders.orderList) {
                        System.out.printf("%-20s | %-10.2f | %-10d | %-10s%n", order.getItemName(), order.getItemPrice(), order.getItemQuantity(), order.getDocId());
                    }
                    System.out.println("------------------------------------------------------------------");
                } else {
                    Toast.makeText(c, "You have a undergoing order. Please wait for it to finish.", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    @Override
    public int getItemCount() {
        return pFiltered.size(); // Return size of filtered list
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView cardbg;
        TextView itemPrice;
        ImageView namebg;
        TextView itemName;
        CircleImageView itemImage;
        RelativeLayout imageBorder;

        CircleImageView fakeBtn;
        FloatingActionButton btn_addToServings;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            cardbg = itemView.findViewById(R.id.cardbg);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            imageBorder = itemView.findViewById(R.id.image_border);
            fakeBtn = itemView.findViewById(R.id.fake_add_button);
            btn_addToServings = itemView.findViewById(R.id.btn_addToServings);
        }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                List<ProductClass> filteredList = new ArrayList<>();
                if (TextUtils.isEmpty(filterPattern)) {
                    filteredList.addAll(p);
                } else {
                    for (ProductClass item : p) {
                        if (item.getItemName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                pFiltered.clear();
                pFiltered.addAll((List<ProductClass>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setInterface(CheckForNewData check) {
        this.check = check;
    }
}
