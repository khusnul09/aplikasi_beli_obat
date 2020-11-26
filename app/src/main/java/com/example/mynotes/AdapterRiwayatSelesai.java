package com.example.mynotes;

import android.content.Context;
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

public class AdapterRiwayatSelesai extends RecyclerView.Adapter<AdapterRiwayatSelesai.ViewHolder> {
    private LayoutInflater inflater;
    private List<ModelRiwayatSelesai> modelRiwayatSelesaiList;
    private Context context;
    private AdapterRiwayatSelesai.OnItemClick onItemCLick;

    public void setClickListener(AdapterRiwayatSelesai.OnItemClick onItemClick) {
        this.onItemCLick = onItemClick;
    }

    public AdapterRiwayatSelesai(Context context) {
        this.modelRiwayatSelesaiList = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public AdapterRiwayatSelesai.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowriwayatselesai, parent, false);
        return new AdapterRiwayatSelesai.ViewHolder(v);
    }

    void add(ModelRiwayatSelesai item) {
        modelRiwayatSelesaiList.add(item);
        notifyItemInserted(modelRiwayatSelesaiList.size());
    }

    void addAll(List<ModelRiwayatSelesai> list) {
        for (ModelRiwayatSelesai number : list) {
            add(number);
        }
    }

    @Override
    public void onBindViewHolder(AdapterRiwayatSelesai.ViewHolder holder, int position) {
        ModelRiwayatSelesai modelRiwayatSelesai = modelRiwayatSelesaiList.get(position);

        switch (modelRiwayatSelesai.getJenis()) {
            case "1" :
                holder.jenis.setText("RESEP");
                Picasso.get().load(modelRiwayatSelesai.getGambar_resep()).into(holder.gambar);
                //Log.i("khatima", modelRiwayatSelesai.getGambar_resep());
                break;
            case "2":
                holder.jenis.setText("PAKET");
                holder.gambar.setVisibility(View.GONE);
                break;
        }

        holder.invoice.setText("#"+modelRiwayatSelesai.getInvoice());
        holder.nama.setText(modelRiwayatSelesai.getNama_penerima());

        switch (modelRiwayatSelesai.getStatus()) {
            case "1":
                holder.status.setText("Menunggu Pembayaran");
                break;
            case "2":
                holder.status.setText("Pesanan dikemas");
                break;
            case "3":
                holder.status.setText("Pesanan Dikirim");
                break;
            case "4":
                holder.status.setText("Pesanan Diterima");
                break;
        }

        holder.waktu.setText(modelRiwayatSelesai.getWaktu());
        holder.harga.setText(modelRiwayatSelesai.getTotalHarga()+",-");

        holder.detail.setOnClickListener(v -> {
            onItemCLick.onItemClick(modelRiwayatSelesaiList.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() { return modelRiwayatSelesaiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jenis, invoice, nama, status, waktu, harga, detail;
        ImageView gambar;

        public ViewHolder(View itemView) {
            super(itemView);
            jenis = itemView.findViewById(R.id.tv_resep_selesai);
            invoice = itemView.findViewById(R.id.tv_invoice_resep_selesai);
            nama = itemView.findViewById(R.id.tv_nama_penerima_selesai);
            status = itemView.findViewById(R.id.tv_statusnya_resep_selesai);
            waktu = itemView.findViewById(R.id.tv_waktu_selesai_resep);
            harga = itemView.findViewById(R.id.tv_total_byr_resep_selesai);
            detail = itemView.findViewById(R.id.tv_lihat_detail_resep_selesai);
            gambar = itemView.findViewById(R.id.iv_gambar_resep_selesai);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelRiwayatSelesai data);
    }
}
