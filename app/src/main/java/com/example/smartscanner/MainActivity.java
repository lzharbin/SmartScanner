package com.example.smartscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnOpenScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpenScanner = findViewById(R.id.btnOpenScanner);

        btnOpenScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the QR scanner activity
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
    }
}
