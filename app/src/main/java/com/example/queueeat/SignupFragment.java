package com.example.queueeat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupFragment extends Fragment {

    ViewPager2 vp2;
    FirebaseFirestore db;
    public SignupFragment(ViewPager2 vp2) {
        this.vp2 = vp2;
    }

    public SignupFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        db = FirebaseFirestore.getInstance();

        EditText newaccpass = view.findViewById(R.id.newAccPass);
        EditText newaccname = view.findViewById(R.id.newAccName);
        EditText newaccemail = view.findViewById(R.id.newAccEmail);
        EditText newaccconpass = view.findViewById(R.id.newAccConfirmPass);

        // Set the EditTexts to single line
        newaccpass.setSingleLine(true);
        newaccname.setSingleLine(true);
        newaccemail.setSingleLine(true);
        newaccconpass.setSingleLine(true);

        TextView loginRedirectTextView = view.findViewById(R.id.login_redirect);

        // Set OnClickListener to the TextView
        loginRedirectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp2.setCurrentItem(vp2.getCurrentItem() - 1);

            }
        });

// Get the EditText references
        EditText newAccNameEditText = view.findViewById(R.id.newAccName);
        EditText newAccEmailEditText = view.findViewById(R.id.newAccEmail);
        EditText newAccPassEditText = view.findViewById(R.id.newAccPass);
        EditText newAccConfirmPassEditText = view.findViewById(R.id.newAccConfirmPass);






        Button registerButton = view.findViewById(R.id.register_button);

        // Set OnClickListener to the login button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils fu = new FirebaseUtils();

                if(newAccNameEditText.getText().toString().isEmpty() || newAccConfirmPassEditText.getText().toString().isEmpty() || newAccEmailEditText.getText().toString().isEmpty() || newAccEmailEditText.getText().toString().isEmpty() || newAccPassEditText.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill up all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    if (newAccPassEditText.getText().toString().equals(newAccConfirmPassEditText.getText().toString())) {
                        fu.addAccount(newAccNameEditText.getText().toString(), newAccPassEditText.getText().toString(), "student", newAccEmailEditText.getText().toString(), db, getContext());
                        Toast.makeText(getContext(), "Account Created", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getContext(),HomePage.class);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Passwords dont match.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        return view;
    }


}
