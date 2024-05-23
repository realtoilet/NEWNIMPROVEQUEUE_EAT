package com.example.queueeat;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.queueeat.databinding.FragmentQueueBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.TimeUnit;


public class QueueFragment extends Fragment {

    private FirebaseFirestore firestore;
    private TextView queueNumber;
    private FragmentQueueBinding binding;
    private String user;
    private TextView receiptUserName;
    private Dialog endTimerDialog; // Reference to hold the inflated dialog instance
    private TextView timerTextView; // TextView to display the timer
    private CountDownTimer countDownTimer;
    private SharedViewModel sharedViewModel;

    // Constructor with user parameter
    public QueueFragment(String user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout using data binding
        binding = FragmentQueueBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        queueNumber = binding.queueNumber;
        firestore = FirebaseFirestore.getInstance();
        queueNumber.setText("");

        timerTextView = binding.timernabaog;

        FirebaseUtils.getCurrentQueue(FirebaseFirestore.getInstance(), user, queueNumber -> {
            Log.d("QueueFragment", "Current Queue Number: " + queueNumber);
            if (queueNumber != -1) {
                this.queueNumber.setText(String.valueOf(queueNumber));
            } else {
                this.queueNumber.setText("0");
            }
        });

        binding.receiptUserName.setText(SharedPrefUtils.returnUsernameForData(getContext()));

        TextView endTimer = binding.endTimer;
        if (endTimer != null) {
            endTimer.setOnClickListener(v -> showEndTimerDialog());
        } else {
            Log.w("QueueFragment", "endTimer Text View not found!");
        }

        // Observe timer status
        WorkManager.getInstance(getContext())
                .getWorkInfosByTagLiveData("timerWorkRequest")
                .observe(getViewLifecycleOwner(), workInfos -> {
                    if (workInfos == null || workInfos.isEmpty()) {
                        return;
                    }

                    WorkInfo workInfo = workInfos.get(0);
                    if (workInfo.getState().isFinished()) {
                        timerTextView.setText("00:00");
                    } else {
                        long endTimeMillis = workInfo.getOutputData().getLong("endTimeMillis", 0);
                        long remainingTimeMillis = endTimeMillis - System.currentTimeMillis();

                        startCountdownTimer(remainingTimeMillis);
                    }
                });

        return rootView;
    }

    public void startCountdownTimer(long durationInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
            }
        }.start();
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
        endTimerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        endTimerDialog.getWindow().setGravity(Gravity.CENTER);
        endTimerDialog.show(); // Show the inflated dialog
    }
}


