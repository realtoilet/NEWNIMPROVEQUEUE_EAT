package com.example.queueeat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Seats_Selector_HFragment extends Fragment {

    private List<CheckBox> checkBoxList = new ArrayList<>();
    int currSeat;
    static View v;
    public Seats_Selector_HFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.fragment_seats__selector__h, container, false);

        // Populate checkbox list
        populateCheckBoxList(v);

        // Attach listener to checkboxes
        attachCheckBoxListener();

        for (int i = 57; i <= 64; i++) {
            String checkBoxID = "checkbox" + i;
            int resID = getResources().getIdentifier(checkBoxID, "id", getContext().getPackageName());
            CheckBox checkBox = v.findViewById(resID);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    String idString = cb.getResources().getResourceEntryName(cb.getId());
                    currSeat = Integer.parseInt(idString.replace("checkbox", ""));
                    ListOfOrders.currSeat = currSeat;
                    Toast.makeText(getContext(), "Clicked Checkbox: " + ListOfOrders.currSeat, Toast.LENGTH_SHORT).show();

                }
            });
        }
        FirebaseUtils.setDataToButtons(FirebaseFirestore.getInstance(), 57, 64, v);
        return v;
    }
    public static void uncheckSeat(){
        FirebaseUtils.uncheckButtonOnceTimerIsDone(FirebaseFirestore.getInstance(), ListOfOrders.currSeat, v);
    }
    // Method to populate the checkbox list
    private void populateCheckBoxList(View rootView) {
        checkBoxList.clear(); // Clear previous entries if any

        // Add checkboxes to the list
        checkBoxList.add(rootView.findViewById(R.id.checkbox57));
        checkBoxList.add(rootView.findViewById(R.id.checkbox58));
        checkBoxList.add(rootView.findViewById(R.id.checkbox59));
        checkBoxList.add(rootView.findViewById(R.id.checkbox60));
        checkBoxList.add(rootView.findViewById(R.id.checkbox61));
        checkBoxList.add(rootView.findViewById(R.id.checkbox62));
        checkBoxList.add(rootView.findViewById(R.id.checkbox63));
        checkBoxList.add(rootView.findViewById(R.id.checkbox64));
        // Add more checkboxes if needed

        ListOfOrders.checkboxlist.addAll(checkBoxList);

    }

    // Method to attach listener to checkboxes
    private void attachCheckBoxListener() {
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Uncheck all other CheckBoxes
                    for (CheckBox checkBox : ListOfOrders.checkboxlist) {
                        if (checkBox != buttonView) {
                            checkBox.setChecked(false);
                        }
                    }
                }
            }
        };

        // Set listener for each checkbox
        for (CheckBox checkBox : checkBoxList) {
            checkBox.setOnCheckedChangeListener(checkBoxListener);
        }
    }
}
