package com.example.queueeat;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminProfileFragment extends Fragment {

    private Button logoutButton;

    public AdminProfileFragment() {
        // Required empty public constructor
    }

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        // Find the logout button
        logoutButton = view.findViewById(R.id.btn_logout_admin);

        // Set onClickListener for logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignInActivity
                Intent intent = new Intent(getActivity(), SigninPage.class);
                startActivity(intent);

                // Perform logout
                SharedPrefUtils.logout(getContext());
            }
        });

        return view;
    }
}
