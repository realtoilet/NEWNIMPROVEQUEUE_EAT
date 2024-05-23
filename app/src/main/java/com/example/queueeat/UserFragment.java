package com.example.queueeat;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFragment extends Fragment {

    private Button logoutButton;
    private TextView userEmailTextView, tv_user, tv_gmail;
    private FirebaseFirestore db;
    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        tv_user = view.findViewById(R.id.username);
        tv_gmail = view.findViewById(R.id.userGmail);
        // Find the logout button
        logoutButton = view.findViewById(R.id.btn_logout_user);


        // Set onClickListener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the cart is not empty
                if (!ListOfOrders.orderList.isEmpty()) {
                    // Clear the cart lists
                    ListOfOrders.orderList.clear();
                    ListOfOrders.checkoutList.clear();

                    // Notify the adapter if it exists
                    if (CartFragment.adapter != null) {
                        CartFragment.adapter.notifyDataSetChanged();
                    }

                    // Update the UI if the CartFragment is active
                    CartFragment.updateButton();
                    CartFragment.updateTextview();
                }

                // Perform logout
                SharedPrefUtils.logout(getContext());

                // Navigate to SignInActivity
                Intent intent = new Intent(getActivity(), SigninPage.class);
                startActivity(intent);
            }
        });


        // Get the current user from Firebase Authentication

        tv_user.setText(SharedPrefUtils.returnUsernameForData(getContext()));
        tv_gmail.setText(SharedPrefUtils.returnEmailData(getContext()));
        return view;
    }
}
