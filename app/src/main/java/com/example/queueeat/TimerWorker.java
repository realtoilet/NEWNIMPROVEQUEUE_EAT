package com.example.queueeat;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TimerWorker extends Worker {

    public TimerWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Get the end time from input data
        long endTimeMillis = getInputData().getLong("endTimeMillis", 0);

        // Check if the end time has passed
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis >= endTimeMillis) {
            // Timer has finished
            return Result.success();
        } else {
            // Timer is still running
            long remainingTimeMillis = endTimeMillis - currentTimeMillis;

            // Sleep for the remaining time
            try {
                Thread.sleep(remainingTimeMillis);
            } catch (InterruptedException e) {
                return Result.failure();
            }

            // Timer has finished
            return Result.success();
        }
    }
}

