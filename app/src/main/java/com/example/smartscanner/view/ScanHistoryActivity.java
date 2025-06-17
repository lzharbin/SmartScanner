package com.example.smartscanner.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartscanner.R;
import com.example.smartscanner.model.ScanResult;
import com.example.smartscanner.viewmodel.ScanHistoryViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ScanHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScanHistoryViewModel viewModel;
    private ScanHistoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ScanHistoryAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ScanHistoryViewModel.class);
        viewModel.getAllResults().observe(this, scanResults -> {
            adapter.setScanList(scanResults);
            if (scanResults.isEmpty()) {
                Toast.makeText(this,
                        getString(R.string.no_history),
                        Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickListener(scanResult -> {
            Toast.makeText(this, getString(R.string.clicked_prefix, scanResult.getContent()), Toast.LENGTH_SHORT).show();
            // TODO: Replace with detailed view or editing screen
        });

        // Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; // no drag-drop
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // ScanResult deletedItem = adapter.getScanAt(viewHolder.getAdapterPosition());
                //viewModel.delete(deletedItem);
                //Toast.makeText(ScanHistoryActivity.this, "Deleted scan", Toast.LENGTH_SHORT).show();
                // }
                //}).attachToRecyclerView(recyclerView);
                int pos = viewHolder.getAdapterPosition();
                ScanResult item = adapter.getScanAt(pos);

                new AlertDialog.Builder(ScanHistoryActivity.this)
                        .setTitle(R.string.dialog_confirm_title)              // "Confirm Deletion"
                        .setMessage(R.string.dialog_confirm_message)          // "Are you sure…?"
                        .setPositiveButton(R.string.delete, (dlg, which) -> {

                            viewModel.delete(item);


                            Toast.makeText(
                                    ScanHistoryActivity.this,
                                    R.string.toast_deleted,   // "Scan deleted"
                                    Toast.LENGTH_SHORT
                            ).show();


                            Snackbar.make(
                                            recyclerView,
                                            R.string.snackbar_deleted, // "Scan deleted"
                                            Snackbar.LENGTH_LONG
                                    )
                                    .setAction(R.string.undo, v -> {
                                        viewModel.insert(item);
                                    })
                                    .show();
                        })
                        .setNegativeButton(R.string.cancel, (dlg, which) -> {

                            adapter.notifyItemChanged(pos);
                        })
                        .setCancelable(false)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}

