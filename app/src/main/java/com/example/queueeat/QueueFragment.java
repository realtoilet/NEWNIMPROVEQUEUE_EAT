package com.example.queueeat;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.queueeat.databinding.FragmentQueueBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class QueueFragment extends Fragment {

    FirebaseFirestore firestore;
    TextView quenum;
    FragmentQueueBinding b;
    String user;
    TextView receipt_userName;

    // Constructor with user parameter
    public QueueFragment(String user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentQueueBinding.inflate(inflater, container, false);
        b.receiptUserName.setText("");
        firestore = FirebaseFirestore.getInstance();
        b.queueNumber.setText("");

        // Example of using FirebaseUtils.getCurrentQueue
        FirebaseUtils.getCurrentQueue(FirebaseFirestore.getInstance(), user, queueNumber -> {
            Log.d("QueueFragment", "Current Queue Number: " + queueNumber);
            if (queueNumber != -1) {
                b.queueNumber.setText(String.valueOf(queueNumber));
            } else {
                b.queueNumber.setText("0");
            }
        });

        b.receiptUserName.setText(SharedPrefUtils.returnUsernameForData(getContext()));

        return b.getRoot();
    }
}
