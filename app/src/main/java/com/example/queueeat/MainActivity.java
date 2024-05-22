package com.example.queueeat;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.os.Build;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.queueeat.HomePage; // Replace this with the correct import statement for your HomePage class

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delayed redirection to HomePage
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, SigninPage.class)); // Replace HomePage.class with the correct class name
            finish(); // Optional, if you want to close MainActivity after redirecting
        }, 3000); // 3000 milliseconds = 3 seconds




    }
}
