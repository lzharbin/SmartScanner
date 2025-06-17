package com.example.smartscanner.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartscanner.R;
import com.example.smartscanner.model.ScanResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ScanViewHolder> {

    private List<ScanResult> scanList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScanResult scanResult);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setScanList(List<ScanResult> scans) {
        this.scanList = scans;
        notifyDataSetChanged();
    }

    public ScanResult getScanAt(int position) {
        return scanList.get(position);
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_result, parent, false);
        return new ScanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanViewHolder holder, int position) {
        ScanResult currentScan = scanList.get(position);

        holder.textContent.setText(currentScan.getContent());
        holder.textType.setText(currentScan.getType().name());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        String dateStr = sdf.format(currentScan.getTimestamp());
        holder.textTimestamp.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }

    class ScanViewHolder extends RecyclerView.ViewHolder {
        private TextView textContent;
        private TextView textType;
        private TextView textTimestamp;

        public ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            textContent = itemView.findViewById(R.id.textContent);
            textType = itemView.findViewById(R.id.textType);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(scanList.get(position));
                }
            });
        }
    }
}
