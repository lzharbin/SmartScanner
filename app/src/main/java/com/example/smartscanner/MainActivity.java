package com.example.smartscanner;

import com.google.android.material.snackbar.Snackbar;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.smartscanner.view.ScanHistoryActivity;


public class MainActivity extends AppCompatActivity {

    private Button btnOpenScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnOpenScanner = findViewById(R.id.btnOpenScanner);

        btnOpenScanner = findViewById(R.id.btnOpenScanner);

        btnOpenScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the QR scanner activity
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });



        Button btnOpenHistory = findViewById(R.id.btnOpenHistory);
        btnOpenHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ScanHistoryActivity.class))
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {

            String authors = getString(R.string.help_authors);
            String version = "Version: " + BuildConfig.VERSION_NAME;
            String instructions = getString(R.string.help_instructions);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.help_title)
                    .setMessage(authors + "\n\n" + version + "\n\n" + instructions)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }

        if (id == R.id.action_history) {
            startActivity(new Intent(this, ScanHistoryActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
