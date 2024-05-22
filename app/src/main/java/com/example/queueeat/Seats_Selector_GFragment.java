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

public class Seats_Selector_GFragment extends Fragment {

    private List<CheckBox> checkBoxList = new ArrayList<>();
    int currSeat;
    public Seats_Selector_GFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seats__selector__g, container, false);

        // Populate checkbox list
        populateCheckBoxList(v);

        // Attach listener to checkboxes
        attachCheckBoxListener();

        for (int i = 49; i <= 56; i++) {
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
        FirebaseUtils.setDataToButtons(FirebaseFirestore.getInstance(), 49, 56, v);
        return v;
    }

    // Method to populate the checkbox list
    private void populateCheckBoxList(View rootView) {
        checkBoxList.clear(); // Clear previous entries if any

        // Add checkboxes to the list
        checkBoxList.add(rootView.findViewById(R.id.checkbox49));
        checkBoxList.add(rootView.findViewById(R.id.checkbox50));
        checkBoxList.add(rootView.findViewById(R.id.checkbox51));
        checkBoxList.add(rootView.findViewById(R.id.checkbox52));
        checkBoxList.add(rootView.findViewById(R.id.checkbox53));
        checkBoxList.add(rootView.findViewById(R.id.checkbox54));
        checkBoxList.add(rootView.findViewById(R.id.checkbox55));
        checkBoxList.add(rootView.findViewById(R.id.checkbox56));
        // Add more checkboxes if needed
    }

    // Method to attach listener to checkboxes
    private void attachCheckBoxListener() {
        CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Uncheck all other CheckBoxes
                    for (CheckBox checkBox : checkBoxList) {
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
