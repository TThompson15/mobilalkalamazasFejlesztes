package com.example.gazorajelento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gazorajelento.model.GasMeterInfo;

import java.util.List;

public class GasMeterDataAdapter extends RecyclerView.Adapter<GasMeterDataAdapter.ViewHolder> {

    private final List<GasMeterInfo> gasMeterInfoList;

    public GasMeterDataAdapter(List<GasMeterInfo> gasMeterInfoList) {
        this.gasMeterInfoList = gasMeterInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GasMeterInfo gasMeterInfo = gasMeterInfoList.get(position);
        holder.tvDateTime.setText(gasMeterInfo.getActualDateTime());
        holder.tvValue.setText(String.valueOf(gasMeterInfo.getCurrentAmount()));
    }

    @Override
    public int getItemCount() {
        return gasMeterInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDateTime;
        TextView tvValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }
}
