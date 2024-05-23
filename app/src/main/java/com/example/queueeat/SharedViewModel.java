package com.example.queueeat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Long> timerDuration = new MutableLiveData<>();

    public void setTimerDuration(long duration) {
        timerDuration.setValue(duration);
    }

    public MutableLiveData<Long> getTimerDuration() {
        return timerDuration;
    }
}

