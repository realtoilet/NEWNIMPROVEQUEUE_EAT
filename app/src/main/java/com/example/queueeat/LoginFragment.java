package com.example.queueeat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {

    ViewPager2 vp2;
    FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private CheckBox rememberMeCheckBox;

    public LoginFragment() {
    }

    public LoginFragment(ViewPager2 vp2) {
        this.vp2 = vp2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        db = FirebaseFirestore.getInstance();

        EditText accpass = view.findViewById(R.id.AccPass);
        EditText accname = view.findViewById(R.id.AccName);
        accpass.setSingleLine(true);
        accname.setSingleLine(true);

        rememberMeCheckBox = view.findViewById(R.id.cbox_rememberMe);

        Button loginButton = view.findViewById(R.id.login_button);

        TextView loginRedirectTextView = view.findViewById(R.id.signup_redirect);
        loginRedirectTextView.setOnClickListener(v-> vp2.setCurrentItem(vp2.getCurrentItem() - 1));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = accname.getText().toString();
                String password = accpass.getText().toString();

                db.collection("USERS").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String email = null;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot post : task.getResult()) {
                                String childusername = post.getString("username");
                                if (username.equals(childusername)) {
                                    email = post.getString("email");
                                    break;
                                }
                            }
                            if(email == null) {
                                Log.d("aha","raha");
                            }
                            Log.d("email", String.valueOf(email));
                            if (username.isEmpty() || password.isEmpty()) {
                                Toast.makeText(getContext(), "Please fill up all the fields.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (username.equals("admin") && password.equals("adminadmin123")) {
                                    SharedPrefUtils.forDataUsage(getContext(), username, email.toString());
                                    startActivity(new Intent(getActivity(), AdminHomePage.class).putExtra("user", SharedPrefUtils.returnEmailData(getContext())).putExtra("email",email.toString()));
                                    if (rememberMeCheckBox.isChecked()) {
                                        SharedPrefUtils.saveAccount(getContext(), username, password, "admin", email.toString());
                                    }
                                } else {
                                    FirebaseUtils fu = new FirebaseUtils();
                                    String finalEmail = email;
                                    fu.loginAccount(username, password, db, new FirebaseUtils.LoginCallback() {
                                        @Override
                                        public void loginResult(boolean result) {
                                            if (result) {
                                                SharedPrefUtils.forDataUsage(getContext(), username, finalEmail.toString());
                                                startActivity(new Intent(getActivity(), HomePage.class).putExtra("user", SharedPrefUtils.returnEmailData(getContext())).putExtra("email", finalEmail.toString()));
                                                if (rememberMeCheckBox.isChecked()) {
                                                    SharedPrefUtils.saveAccount(getContext(), username, password, "buyer", finalEmail.toString());
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Account not found.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        });
        return view;
//        if(email == null) {
//            Log.d("aha","raha");
//        }
//        Log.d("email", String.valueOf(email));
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(getContext(), "Please fill up all the fields.", Toast.LENGTH_SHORT).show();
//        } else {
//            if (username.equals("admin") && password.equals("adminadmin123")) {
//                SharedPrefUtils.forDataUsage(getContext(), username, email.toString());
//                startActivity(new Intent(getActivity(), AdminHomePage.class).putExtra("user", SharedPrefUtils.returnEmailData(getContext())).putExtra("email",email.toString()));
//                if (rememberMeCheckBox.isChecked()) {
//                    SharedPrefUtils.saveAccount(getContext(), username, password, "admin", email.toString());
//                }
//            } else {
//                FirebaseUtils fu = new FirebaseUtils();
//                fu.loginAccount(username, password, db, new FirebaseUtils.LoginCallback() {
//                    @Override
//                    public void loginResult(boolean result) {
//                        if (result) {
//                            SharedPrefUtils.forDataUsage(getContext(), username, email.toString());
//                            startActivity(new Intent(getActivity(), HomePage.class).putExtra("user", SharedPrefUtils.returnEmailData(getContext())).putExtra("email", email.toString()));
//                            if (rememberMeCheckBox.isChecked()) {
//                                SharedPrefUtils.saveAccount(getContext(), username, password, "buyer", email.toString());
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "Account not found.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }
//    }
    }
}

