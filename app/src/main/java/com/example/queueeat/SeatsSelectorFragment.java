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

public class SeatsSelectorFragment extends Fragment {

    static List<CheckBox> checkBoxList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();
    private CheckBox checkedCheckbox; // Store reference to the currently checked checkbox
    int currSeat = 0;

    public SeatsSelectorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seats_selector, container, false);
        populateCheckBoxList(v);
        attachCheckBoxListener();

        for (int i = 1; i <= 8; i++) {
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

        FirebaseUtils.setDataToButtons(FirebaseFirestore.getInstance(), 1, 8, v);
        return v;
    }

    // Method to populate the checkbox list
    private void populateCheckBoxList(View rootView) {
        checkBoxList.clear(); // Clear previous entries if any

        // Add checkboxes to the list
        checkBoxList.add(rootView.findViewById(R.id.checkbox1));
        checkBoxList.add(rootView.findViewById(R.id.checkbox2));
        checkBoxList.add(rootView.findViewById(R.id.checkbox3));
        checkBoxList.add(rootView.findViewById(R.id.checkbox4));
        checkBoxList.add(rootView.findViewById(R.id.checkbox5));
        checkBoxList.add(rootView.findViewById(R.id.checkbox6));
        checkBoxList.add(rootView.findViewById(R.id.checkbox7));
        checkBoxList.add(rootView.findViewById(R.id.checkbox8));


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

    // Method to get the list of checkboxes
    public List<CheckBox> getCheckBoxList() {
        return checkBoxList;
    }

    // Method to clear the checked checkbox
    public void clearCheckedCheckbox() {
        if (checkedCheckbox != null) {
            checkedCheckbox.setChecked(false);
        }
    }
}
