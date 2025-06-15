package com.example.smartscanner;

import com.example.smartscanner.db.AppDatabase;
import com.example.smartscanner.db.ScanResultDAO;
import com.example.smartscanner.model.ScanResult;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

import com.example.smartscanner.util.ContentUtil;
import com.example.smartscanner.util.ContentType;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.smartscanner.viewmodel.ScanHistoryViewModel;
import com.google.common.util.concurrent.ListenableFuture;
//import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanActivity extends AppCompatActivity {
    private List<ScanResult> scanResults = new ArrayList<>();
    private PreviewView previewView;
    private ProgressBar progressBar;
    private ExecutorService cameraExecutor;
    private BarcodeScanner scanner;

    private ScanResultDAO scanResultDao;
    private ScanHistoryViewModel scanHistoryViewModel;
    private Executor databaseWriteExecutor;

    private static final String TAG = ScanActivity.class.getSimpleName();

    private static final int CAMERA_PERMISSION_REQUEST = 200;

    private boolean scanned = false;
    private EditText editTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        previewView = findViewById(R.id.previewView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        editTextTitle = findViewById(R.id.editTextTitle);

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        scanResultDao = db.scanResultDao();

        // Initialize the ScanHistoryViewModel
        scanHistoryViewModel = new ViewModelProvider(this).get(ScanHistoryViewModel.class);

        // Create an executor for database writes (or use cameraExecutor if appropriate)
        databaseWriteExecutor = Executors.newSingleThreadExecutor(); // Or use a shared one

        scanner = BarcodeScanning.getClient();
        cameraExecutor = Executors.newSingleThreadExecutor();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                ImageAnalysis analysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                analysis.setAnalyzer(cameraExecutor, imageProxy -> {
                    if (scanned) {
                        imageProxy.close();
                        return; // ✅ Skip analysis after first scan
                    }

                    runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

                    Image mediaImage = imageProxy.getImage();
                    if (mediaImage != null) {
                        InputImage image = InputImage.fromMediaImage(mediaImage,
                                imageProxy.getImageInfo().getRotationDegrees());

                        scanner.process(image)
                                .addOnSuccessListener(barcodes -> {
                                    if (!barcodes.isEmpty()) {
                                        scanned = true;
                                        runOnUiThread(() -> progressBar.setVisibility(View.GONE));


                                        for (Barcode barcode : barcodes) {
                                            Log.d("ScanActivity", "Barcode detected - display value: " + barcode.getDisplayValue());
                                            String rawValue = barcode.getRawValue();
                                            ContentType type = ContentUtil.detectContentType(rawValue);
                                            Log.d("ScanActivity", "Raw value and type gotten: " + rawValue + " (" + type + ")");

                                            String userTitle = ""; // Initialize
                                            ScanResult result = new ScanResult(
                                                    rawValue,
                                                    type,
                                                    new Date(),
                                                    null, // imagePath, set if you have it
                                                    userTitle.isEmpty() ? "Scan - " + rawValue.substring(0, Math.min(rawValue.length(), 10)) : userTitle // Default title
                                            );

                                            if (isValidResult(result)) {
                                                String newTitle = editTextTitle.getText().toString();
                                                if (newTitle.length() > 0) {
                                                    userTitle = editTextTitle.getText().toString();
                                                }
                                                result.setTitle(userTitle);

                                                // Use the scanHistoryViewModel to insert the result into the RoomDB
                                                scanHistoryViewModel.insert(result);
                                                runOnUiThread(() ->
                                                        Toast.makeText(this, "Scanned & Saved: " + result.getTitle(), Toast.LENGTH_SHORT).show());
                                                Log.d("ScanActivity", "Valid result added: " + rawValue + " (" + type + ")");
                                            } else {
                                                Log.d("ScanActivity", "Invalid result ignored: " + rawValue + " (" + type + ")");
                                                runOnUiThread(() ->
                                                        Toast.makeText(this, "Ignored: " + rawValue, Toast.LENGTH_SHORT).show());
                                            }


                                        }
                                    }

                                })
                                .addOnFailureListener(e -> {
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show();
                                    });
                                })
                                .addOnCompleteListener(task -> imageProxy.close());
                    } else {
                        imageProxy.close();
                    }
                });
                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, analysis);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private boolean isValidResult(ScanResult result) {
        switch (result.getType()) {
            case URL:
                return result.getContent().startsWith("http");
            case PRODUCT:
                return result.getContent().matches("\\d{12,13}");
            case CONTACT:
            case TEXT:
                return !result.getContent().trim().isEmpty();
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (databaseWriteExecutor instanceof ExecutorService) { // Shutdown if it's an ExecutorService
            ((ExecutorService) databaseWriteExecutor).shutdown();
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
