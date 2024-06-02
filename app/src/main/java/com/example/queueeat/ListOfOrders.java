package com.example.queueeat;

import android.widget.CheckBox;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListOfOrders {
    public static Map<String, List<Map<String, Object>>> orderMap = new HashMap<>();
    public static List<ProductClass> orderList = new ArrayList<>();
    public static List<ProductClass> checkoutList = new ArrayList<>();
    public static int currSeat = 0;
    public static List<CheckBox> checkboxlist = new ArrayList<>();
    public static MutableLiveData<String> state = new MutableLiveData<>();
}
