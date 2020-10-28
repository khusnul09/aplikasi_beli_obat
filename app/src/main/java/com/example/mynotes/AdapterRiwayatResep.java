package com.example.mynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterRiwayatResep extends RecyclerView.Adapter<AdapterRiwayatResep.ViewHolder> {
    private LayoutInflater inflater;
    private List<ModelRiwayatResep> listriwayatresep;
    private Context context;
    private OnItemClick onItemCLick;

    public void setClickListener(OnItemClick onItemClick) {
        this.onItemCLick = onItemClick;
    }

    public AdapterRiwayatResep(Context context) {
        this.listriwayatresep = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowriwayatresep, parent, false);
        return new ViewHolder(v);
    }

    void add(ModelRiwayatResep item) {
        listriwayatresep.add(item);
        notifyItemInserted(listriwayatresep.size());
    }

    void addAll(List<ModelRiwayatResep> list) {
        for (ModelRiwayatResep number : list) {
            add(number);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelRiwayatResep modelRiwayatResep = listriwayatresep.get(position);
        holder.waktu.setText(modelRiwayatResep.getWaktu());

        holder.lihatDetailResep.setOnClickListener(v -> onItemCLick.onItemClick(listriwayatresep.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() { return listriwayatresep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView waktu;
        TextView lihatDetailResep;

        public ViewHolder(View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.tv_waktu);
            lihatDetailResep = itemView.findViewById(R.id.tv_lihat_detail_resep);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelRiwayatResep data);
    }
}
