package com.example.queueeat;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtils {

    public interface LoginCallback {
        void loginResult(boolean result);
    }

    public interface CountCallback {
        void docCount(int count);
    }

    public interface CheckForUserOrderListener {
        void hasOrders(boolean allow);
    }

    public interface QueueListener {
        void newQueue(List<OrderClass> list);
    }

    public interface CurrentQueueListener {
        void currentQueue(int queue, List<ForOrderClass> list);
    }

    public interface GetAllSalesFromTimeStamp{
        void getSales(double sales, int orderscount);
    }
    public interface RemoveItemsListener{
        void remove();
    }




    public void addAccount(String username, String password, String type, String email, FirebaseFirestore db, Context c) {
        CollectionReference usersRef = db.collection("USERS");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().size() > 0) {
                            Toast.makeText(c, "Email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> account = new HashMap<>();
                            account.put("username", username);
                            account.put("password", password);
                            account.put("email", email);
                            account.put("type", type);

                            usersRef.add(account);
                        }
                    }
                });
    }

    public void loginAccount(String name, String password, FirebaseFirestore db, LoginCallback cb) {
        CollectionReference usersRef = db.collection("USERS");

        usersRef.whereEqualTo("username", name)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cb.loginResult(task.getResult().size() > 0);
                    }
                });
    }

    public void addNewProduct(Map<String, Object> product, Uri imgAsUri, FirebaseFirestore db, StorageReference storageRef) {
        final DocumentReference df = db.collection("PRODUCTS").document();
        storageRef.putFile(imgAsUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        product.put("itemImg", uri.toString());
                        df.set(product, SetOptions.merge());
                    });
                });
    }

    public static void retrieveAllProducts(FirebaseFirestore d, Context c) {
        List<ProductClass> p = new ArrayList<>();

        d.collection("PRODUCTS")
                .addSnapshotListener((q, e) -> {
                    if (e != null) return;

                    for (DocumentChange dc : q.getDocumentChanges()) {
                        DocumentSnapshot docs = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                                p.add(new ProductClass(docs.getString("itemName"), Uri.parse(docs.getString("itemImg")), docs.getDouble("itemPrice"), 0, docs.getId(), Integer.parseInt(docs.getLong("itemQuantity").toString()),docs.get("Type").toString()));
                        }
                    }
                    HomeFragment.addToRecyclerView(p, c);
                });
    }
    public interface KupalKaSalon{
        void lmao(List<ProductClass> b);
    }
    public static void filterAllProducts(FirebaseFirestore d, Context c, String f) {
        List<ProductClass> p = new ArrayList<>();

        d.collection("PRODUCTS")
                .addSnapshotListener((q, e) -> {
                    if (e != null) return;

                    for (DocumentChange dc : q.getDocumentChanges()) {
                        DocumentSnapshot docs = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                            case REMOVED:
                                p.add(new ProductClass(docs.getString("itemName"), Uri.parse(docs.getString("itemImg")), docs.getDouble("itemPrice"), 0, docs.getId(), Integer.parseInt(docs.getLong("itemQuantity").toString()),docs.get("Type").toString()));
                        }
                    }
                   HomeFragment.filterRecyclerView(p,c,f);
                });
    }

    public static void retrieveAllServings(FirebaseFirestore d, Context c) {
        List<StockClass> p = new ArrayList<>();
        d.collection("PRODUCTS")
                .addSnapshotListener((q, e) -> {
                    if (e != null) return;
                    for (DocumentChange dc : q.getDocumentChanges()) {
                        DocumentSnapshot docs = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                                p.add(new StockClass(docs.getString("itemName"), Integer.parseInt(docs.getLong("itemQuantity").toString()), docs.getId(), docs.getString("Type")));
                                break;
                        }
                    }
                    AdminServingsFragment.addToRecyclerview(p, c);
                });
    }

    public static void removeProduct(FirebaseFirestore f, String docID) {
        f.collection("PRODUCTS").document(docID).delete();
    }

    public static void addAllSeats(FirebaseFirestore d) {
        for (int i = 1; i <= 64; i++) {
            DocumentReference ref = d.collection("SEATS").document("seat_" + i);
            Map<String, Object> m = new HashMap<>();
            m.put("seatNumber", i);
            m.put("isTaken", false);
            m.put("userSeating", "none");
            ref.update("isTaken", false);
        }
    }

    public static void setDataToButtons(FirebaseFirestore f, int min, int max, View v) {
        f.collection("SEATS")
                .whereGreaterThanOrEqualTo("seatNumber", min)
                .whereLessThanOrEqualTo("seatNumber", max)
                .orderBy("seatNumber", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, e) -> {
                    if (e != null) {
                        return;
                    }

                    for (DocumentChange d : snap.getDocumentChanges()) {
                        DocumentSnapshot document = d.getDocument();
                        int seatNumber = document.getLong("seatNumber").intValue();
                        boolean isTaken = document.getBoolean("isTaken");

                        int c_id = v.getResources().getIdentifier("checkbox" + seatNumber, "id", v.getContext().getPackageName());
                        CheckBox c = v.findViewById(c_id);
                        c.setChecked(isTaken);
                        c.setEnabled(!isTaken);
                        if (isTaken) {
                            CompoundButtonCompat.setButtonTintList(c, ContextCompat.getColorStateList(v.getContext(), R.color.checkbox_selector_to_red));
                        } else {
                            CompoundButtonCompat.setButtonTintList(c, ContextCompat.getColorStateList(v.getContext(), R.color.checkbox_selector_to_green));
                        }
                    }
                });


    }

    public static void uncheckButtonOnceTimerIsDone(FirebaseFirestore f, int currSeat, View v){
        f.collection("SEATS")
                .whereEqualTo("seatNumber", currSeat)
                .get()
                .addOnCompleteListener(task -> {
                    f.collection("SEATS").document("seat_" + currSeat).update("isTaken", false);
                });
        if(v!=null){
            CheckBox c = v.findViewById(v.getResources().getIdentifier("checkBox" + currSeat, "id", v.getContext().getPackageName()));

            if(c!=null){
                c.setChecked(false);
            }
        }
    }

    public static void sendOrder(Map<String, Object> m, FirebaseFirestore f) {
        f.collection("ORDERS").document().set(m);

        f.collection("SEATS")
                .whereEqualTo("seatNumber", ListOfOrders.currSeat)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.isEmpty()) {
                        for (DocumentSnapshot doc : task) {
                            f.collection("SEATS")
                                    .document(doc.getId())
                                    .update("isTaken", true);
                        }
                    }
                });
    }

    public static void retrieveAllOrders(FirebaseFirestore f, QueueListener q) {
        f.collection("ORDERS")
                .whereEqualTo("queue", true)
                .orderBy("queueNumber", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, e) -> {
                    if (e != null) {
                        System.out.println("Error fetching orders: " + e.getMessage());
                        return;
                    }

                    List<OrderClass> orders = new ArrayList<>();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        String user = doc.getString("user");
                        List<Map<String, Object>> orderList = (List<Map<String, Object>>) doc.get("orderList");
                        List<ForOrderClass> orderItems = new ArrayList<>();

                        if (orderList != null) {
                            for (Map<String, Object> map : orderList) {
                                String itemName = (String) map.get("itemName");
                                Double itemPrice = (Double) map.get("itemPrice");
                                Long itemQuantity = (Long) map.get("itemQuantity");

                                orderItems.add(new ForOrderClass(itemName, itemPrice, itemQuantity.intValue()));
                            }
                        }

                        int seat = doc.getLong("seatNumber").intValue();
                        int number = doc.getLong("queueNumber").intValue();
                        orders.add(new OrderClass(user, orderItems, seat, number, doc.getId()));
                    }
                    q.newQueue(orders);
                });
    }

    public static void returnDocumentCount(FirebaseFirestore f, CountCallback c) {
        f.collection("ORDERS")
                .whereEqualTo("queue", true)
                .get().addOnCompleteListener(snap -> {
                    if (snap.isSuccessful()) {
                        c.docCount(snap.getResult().size());
                    } else {
                        c.docCount(-1);
                    }
                });
    }

    public static void checkIfOrdered(FirebaseFirestore f, String user, CheckForUserOrderListener h) {
        f.collection("ORDERS")
                .document(user)
                .addSnapshotListener((s, e) -> {
                    if (e != null) return;

                    if (s.exists()) {
                        if (s.getBoolean("queue")) {
                            h.hasOrders(false);
                        } else {
                            h.hasOrders(true);
                        }
                    } else {
                        h.hasOrders(true);
                    }
                });
    }

    public static void getCurrentQueue(FirebaseFirestore f, String user, CurrentQueueListener queue) {
        f.collection("ORDERS")
                .whereEqualTo("user", user)
                .whereIn("queue", Arrays.asList(true, false))
                .orderBy("queue", Query.Direction.ASCENDING)
                .addSnapshotListener((snap, e) -> {
                    if (e != null) {
                        System.err.println("Error: " + e.getMessage());
                        return;
                    }

                    if (snap == null || snap.isEmpty()) {
                        System.out.println("No documents found.");
                        return;
                    }

                    List<ForOrderClass> order = new ArrayList<>();
                    boolean queueFound = false;

                    for (DocumentChange dc : snap.getDocumentChanges()) {
                        DocumentSnapshot doc = dc.getDocument();

                        List<Map<String, Object>> orderList = (List<Map<String, Object>>) doc.get("orderList");

                        if (orderList != null) {
                            order.clear();
                            for (Map<String, Object> map : orderList) {
                                String itemName = (String) map.get("itemName");
                                Double itemPrice = (Double) map.get("itemPrice");
                                Long itemQuantity = (Long) map.get("itemQuantity");

                                order.add(new ForOrderClass(itemName, itemPrice, itemQuantity.intValue()));
                            }
                        }

                        Boolean isQueue = doc.getBoolean("queue");
                        Long queueNumber = doc.getLong("queueNumber");

                        System.out.println("Document ID: " + doc.getId());
                        System.out.println("Queue: " + isQueue);
                        System.out.println("Queue Number: " + queueNumber);

                        if (isQueue != null && queueNumber != null) {
                            if (isQueue) {
                                queue.currentQueue(queueNumber.intValue(), order);
                                queueFound = true;
                            } else {
                                queue.currentQueue(-2, null);
                            }
                        }

                        switch (dc.getType()) {
                            case MODIFIED:
                                if (isQueue != null && queueNumber != null) {
                                    if (isQueue) {
                                        queue.currentQueue(queueNumber.intValue(), order);
                                    } else {
                                        queue.currentQueue(-1, order);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    if (!queueFound) {
                        // Handle the case where no queue document with queue=true is found
                        queue.currentQueue(-2, null);
                    }
                });
    }




    public static void moveQueue(FirebaseFirestore f, String docID) {
        // Set the queue field to false for the specified document
        f.collection("ORDERS").document(docID).update("queue", false)
                .addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        f.collection("ORDERS")
                                .whereEqualTo("queue", true)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot snap = task.getResult();

                                        if (snap != null && !snap.isEmpty()) {
                                            for (DocumentSnapshot doc : snap) {
                                                String docId = doc.getId();
                                                Long seatNumberLong = doc.getLong("queueNumber");
                                                if (seatNumberLong != null) {
                                                    int seatNumber = seatNumberLong.intValue();
                                                    System.out.println("Updating document " + docId + " with queueNumber " + seatNumber + " to " + (seatNumber - 1));

                                                    f.collection("ORDERS").document(docId)
                                                            .update("queueNumber", seatNumber - 1);
                                                }
                                            }
                                        }
                                    }
                                });
                    }
                });
    }

    public static void getAllSales(FirebaseFirestore f, GetAllSalesFromTimeStamp sales){
        Timestamp start = DateUtils.getStartOfDay(2024, Calendar.MAY, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +1);
        Timestamp end = DateUtils.getEndOfDay(2024, Calendar.MAY, Calendar.getInstance().get(Calendar.DAY_OF_MONTH) +1);
        f.collection("ORDERS")
                .addSnapshotListener((snap, e)->{
                    if(e != null) return;
                    double totalSales = 0;
                    List<ForOrderClass> order = new ArrayList<>();
                    for(DocumentChange dc : snap.getDocumentChanges()){
                        DocumentSnapshot doc = dc.getDocument();
                        List<Map<String, Object>> orderList = (List<Map<String, Object>>) doc.get("orderList");

                        if (orderList != null) {
                            for (Map<String, Object> map : orderList) {
                                String itemName = (String) map.get("itemName");
                                Double itemPrice = (Double) map.get("itemPrice");
                                Long itemQuantity = (Long) map.get("itemQuantity");

                                order.add(new ForOrderClass(itemName, itemPrice, itemQuantity.intValue()));

                                totalSales += itemPrice * itemQuantity;
                            }
                        }

                    }
                    sales.getSales(totalSales, snap.size());
                });
    }
}
