package com.example.mynotes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPesananObatAdmin extends RecyclerView.Adapter<AdapterPesananObatAdmin.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ModelPesananObatAdmin> listpesananobat;
    private final Context context;
    private OnItemClick onItemClick;

    public void setClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public AdapterPesananObatAdmin(Context context) {
        this.listpesananobat = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterPesananObatAdmin.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowpesananobatadmin, parent, false);
        return new ViewHolder(v);

    }

    void add(ModelPesananObatAdmin item) {
        listpesananobat.add(item);
        notifyItemInserted(listpesananobat.size());

    }
    void addAll(List<ModelPesananObatAdmin> list) {
        for (ModelPesananObatAdmin number : list) {
            add(number);
        }
    }

    void clear(){ //refresh
        listpesananobat.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterPesananObatAdmin.ViewHolder holder, int position) {
        ModelPesananObatAdmin modelPesananObatAdmin = listpesananobat.get(position);
        if (modelPesananObatAdmin.getStatus().equals("4")) {
            holder.rowPesananObat.setBackgroundColor(Color.parseColor("#FAF0E6"));
        } else {
            holder.rowPesananObat.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.waktu.setText(modelPesananObatAdmin.getWaktu());
        holder.invoice.setText("#"+ modelPesananObatAdmin.getInvoice());
        holder.nama.setText(modelPesananObatAdmin.getNama_penerima());


        holder.lihatDetailPesananObat.setOnClickListener(v -> onItemClick.onItemClick(listpesananobat.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() { return listpesananobat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView waktu, invoice, nama;
        TextView lihatDetailPesananObat;
        RelativeLayout rowPesananObat;

        public ViewHolder(View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.tv_waktu_pesan_pesanan_obat);
            lihatDetailPesananObat = itemView.findViewById(R.id.tv_lihat_detail_pesanan_obat);
            invoice = itemView.findViewById(R.id.tv_invoice_pesanan_obat);
            nama = itemView.findViewById(R.id.tv_nama_penerima_pesanan_obat);
            rowPesananObat = itemView.findViewById(R.id.rl_pesanan_obat);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelPesananObatAdmin data);
    }
}
