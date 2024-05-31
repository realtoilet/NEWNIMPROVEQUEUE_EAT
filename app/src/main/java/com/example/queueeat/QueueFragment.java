package com.example.queueeat;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queueeat.databinding.FragmentQueueBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.concurrent.TimeUnit;


public class QueueFragment extends Fragment {

    private FirebaseFirestore firestore;
    private TextView queueNumber;
    private FragmentQueueBinding binding;
    private String user;
    CountDownTimer timer;
    private TextView receiptUserName;
    private Dialog endTimerDialog; // Reference to hold the inflated dialog instance
    private TextView timerTextView; // TextView to display the timer
    private CountDownTimer countDownTimer;
    private SharedViewModel sharedViewModel;
    long timeLeftInMillis = 2400000;

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
        firestore = FirebaseFirestore.getInstance();
        binding.queueNumber.setText("");
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseUtils.getCurrentQueue(FirebaseFirestore.getInstance(), user, (queueNumber, list) -> { // pag nag error check list agad
            if(ListOfOrders.state.equals("ownmeal")){
                binding.queueNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                binding.queueNumber.setText("OWN");
                startTimer();
                binding.endTimer.setOnClickListener(v->{
                    openDiag();
                });
            } else {
                if (queueNumber == -1) {
                    binding.reciptTotal.setVisibility(View.VISIBLE);
                    binding.queueNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    binding.queueNumber.setText("DONE");
                    Toast.makeText(getContext(), "Queue is done", Toast.LENGTH_SHORT).show();
                    startTimer();

                    binding.endTimer.setOnClickListener(v->{
                        openDiag();
                    });

                } else if (queueNumber == -2) {
                    binding.queueNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    binding.queueNumber.setText("NONE");
                    binding.reciptTotal.setVisibility(View.INVISIBLE);
                } else {
                    binding.reciptTotal.setVisibility(View.VISIBLE);
                    binding.queueNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 100);
                    binding.queueNumber.setText(String.valueOf(queueNumber));
                    binding.rv.setAdapter(new rv_receipt(getContext(), list));
                    double total = 0;
                    for(ForOrderClass c : list){
                        total += c.getItemPrice() * c.getItemQuantity();
                    }
                    binding.reciptTotal.setText("Total: " + total);
                }
            }
        });

        binding.receiptUserName.setText(SharedPrefUtils.returnUsernameForData(getContext()));

        return rootView;
    }


    public void openDiag() {
        Dialog d = new Dialog(getContext());
        d.setContentView(R.layout.end_timer_dialog);
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.getWindow().setGravity(Gravity.CENTER);
        d.setCancelable(true);
        d.show();

        FrameLayout end = d.findViewById(R.id.endTimer_continue);
        FrameLayout cancel = d.findViewById(R.id.endTimer_cancel);

        cancel.setOnClickListener(c->{
            d.dismiss();
        });

        end.setOnClickListener(e->{
            countDownTimer.onFinish();
            d.dismiss();
        });

    }
    private void startTimer() {
         timeLeftInMillis = 2400000;
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                binding.timernabaog.setText("00:00");
                SeatsSelectorFragment.uncheckSeat();
                SeatsSelector_BFragment.uncheckSeat();
                Seats_Selector_CFragment.uncheckSeat();
                Seats_Selector_DFragment.uncheckSeat();
                Seats_Selector_EFragment.uncheckSeat();
                Seats_Selector_FFragment.uncheckSeat();
                Seats_Selector_GFragment.uncheckSeat();
                Seats_Selector_HFragment.uncheckSeat();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        binding.timernabaog.setText(timeLeftFormatted);
    }
}


