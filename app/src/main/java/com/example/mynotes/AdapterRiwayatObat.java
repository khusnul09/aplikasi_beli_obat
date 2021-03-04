package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterRiwayatObat extends RecyclerView.Adapter<AdapterRiwayatObat.ViewHolder> {
    private LayoutInflater inflater;
    private List<ModelRiwayatObat> listriwayatobat;
    private Context context;
    private AdapterRiwayatObat.OnItemClick onItemCLick;

    public void setClickListener(AdapterRiwayatObat.OnItemClick onItemClick) {
        this.onItemCLick = onItemClick;
    }

    public AdapterRiwayatObat(Context context) {
        this.listriwayatobat = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public AdapterRiwayatObat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowriwayatbelumbayar, parent, false);
        return new AdapterRiwayatObat.ViewHolder(v);
    }

    void add(ModelRiwayatObat item) {
        listriwayatobat.add(item);
        notifyItemInserted(listriwayatobat.size());
    }

    void addAll(List<ModelRiwayatObat> list) {
        for (ModelRiwayatObat number : list) {
            add(number);
        }
    }

    void clear(){
        listriwayatobat.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterRiwayatObat.ViewHolder holder, int position) {
        ModelRiwayatObat modelRiwayatObat = listriwayatobat.get(position);
        holder.total.setText(Rupiah.formatUangId(context, Double.parseDouble(String.valueOf(modelRiwayatObat.getTotal_harga()))));
        holder.invoice.setText("#"+ modelRiwayatObat.getInvoice());
        holder.nama.setText(modelRiwayatObat.getNama_penerima());
        switch (modelRiwayatObat.getStatus()) {
            case "1":
                holder.status.setText("Menunggu Pembayaran");
                break;
            case "2":
                holder.status.setText("Pesanan dikemas");
                holder.bayar.setVisibility(View.GONE);
                break;
            case "3":
                holder.status.setText("Pesanan Dikirim");
                holder.bayar.setVisibility(View.GONE);
                break;
            case "4":
                holder.status.setText("Pesanan Diterima");
                break;
        }
        holder.waktu.setText(modelRiwayatObat.getWaktu());
        holder.bayar.setOnClickListener(v -> {
            Intent intent = new Intent(context, BayarActivity.class);
            intent.putExtra("invoice", modelRiwayatObat.getInvoice());
            intent.putExtra("total_harga", modelRiwayatObat.getTotal_harga());
            intent.putExtra("status", modelRiwayatObat.getStatus());

            context.startActivity(intent);
        });
        holder.lihatDetailObat.setOnClickListener(v -> onItemCLick.onItemClick(listriwayatobat.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() { return listriwayatobat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView total, invoice, status, waktu, nama;
        ImageView lihatDetailObat;
        Button bayar;

        public ViewHolder(View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.tv_total_harus_bayar_robb);
            invoice = itemView.findViewById(R.id.tv_invoice);
            waktu = itemView.findViewById(R.id.tv_waktu_pesan);
            status = itemView.findViewById(R.id.tv_statusnya);
            bayar = itemView.findViewById(R.id.btn_bayar_robb);
            lihatDetailObat = itemView.findViewById(R.id.iv_lihat_det_robb);
            nama = itemView.findViewById(R.id.tv_nama_penerima_paket_obb);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelRiwayatObat data);
    }

}
