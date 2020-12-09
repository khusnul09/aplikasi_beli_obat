package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    void clear(){ //refresh
        listriwayatresep.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterRiwayatResep.ViewHolder holder, int position) {
        ModelRiwayatResep modelRiwayatResep = listriwayatresep.get(position);
        holder.waktu.setText(modelRiwayatResep.getWaktu());
        holder.invoice.setText("#"+ modelRiwayatResep.getInvoice());
        holder.status.setText(modelRiwayatResep.getStatusAsString());
        holder.nama.setText(modelRiwayatResep.getNama_penerima());

        Picasso.get()
                .load(modelRiwayatResep.getGambar_resep())
                .into(holder.gambar);

        holder.lihatDetailResep.setOnClickListener(v -> onItemCLick.onItemClick(listriwayatresep.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() { return listriwayatresep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView waktu, status, invoice, nama;
        ImageView gambar;
        TextView lihatDetailResep;

        public ViewHolder(View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.tv_waktu);
            lihatDetailResep = itemView.findViewById(R.id.tv_lihat_detail_resep);
            status = itemView.findViewById(R.id.tv_statusnya_resep);
            invoice = itemView.findViewById(R.id.tv_kode);
            gambar = itemView.findViewById(R.id.iv_gambar_resep);
            nama = itemView.findViewById(R.id.tv_nama_penerima_resep);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelRiwayatResep data);
    }
}
