package com.example.queueeat;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
// Import FrameLayout
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.queueeat.databinding.FragmentQueueBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class QueueFragment extends Fragment {

    private FirebaseFirestore firestore;
    private TextView queueNumber;
    private FragmentQueueBinding binding;
    private String user;
    private TextView receiptUserName;
    private Dialog endTimerDialog; // Reference to hold the inflated dialog instance

    // Constructor with user parameter
    public QueueFragment(String user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQueueBinding.inflate(inflater, container, false);
        queueNumber = binding.queueNumber; // Reference the text view directly
        firestore = FirebaseFirestore.getInstance();
        queueNumber.setText("");

        // Example of using FirebaseUtils.getCurrentQueue
        FirebaseUtils.getCurrentQueue(FirebaseFirestore.getInstance(), user, queueNumber -> {
            Log.d("QueueFragment", "Current Queue Number: " + queueNumber);
            if (queueNumber != -1) {
                this.queueNumber.setText(String.valueOf(queueNumber));
            } else {
                this.queueNumber.setText("0");
            }
        });

        binding.receiptUserName.setText(SharedPrefUtils.returnUsernameForData(getContext()));

        // Set click listener for endTimer text view (assuming it exists)
        TextView endTimer = binding.endTimer; // Update with the actual ID of your endTimer text view
        if (endTimer != null) {
            endTimer.setOnClickListener(v -> showEndTimerDialog());
        } else {
            Log.w("QueueFragment", "endTimer Text View not found!");
        }

        return binding.getRoot();
    }

    private void showEndTimerDialog() {
        if (endTimerDialog == null) {
            // Inflate the end_timer_dialog layout only once
            endTimerDialog = new Dialog(getContext());
            endTimerDialog.setContentView(R.layout.end_timer_dialog); // Update with your layout resource ID

            // Find FrameLayouts by ID and set click listeners
            FrameLayout endTimerCancel = endTimerDialog.findViewById(R.id.endTimer_cancel);
            FrameLayout endTimerContinue = endTimerDialog.findViewById(R.id.endTimer_continue);

            endTimerCancel.setOnClickListener(v -> {
                // Handle cancel button click (e.g., dismiss dialog)
                Log.d("QueueFragment", "End timer cancelled");
                endTimerDialog.dismiss();
            });

            endTimerContinue.setOnClickListener(v -> {
                // Handle continue button click (e.g., perform actions and dismiss dialog)
                Log.d("QueueFragment", "End timer continued");
                // Add your logic for continuing the timer here (if applicable)
                endTimerDialog.dismiss();
            });
        }
        endTimerDialog.show(); // Show the inflated dialog
    }
}
