package com.example.mynotes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterPesananResepAdmin extends RecyclerView.Adapter<AdapterPesananResepAdmin.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ModelPesananResepAdmin> listpesananresep;
    private final Context context;
    private OnItemClick onItemCLick;

    public void setClickListener(OnItemClick onItemClick) {
        this.onItemCLick = onItemClick;
    }

    public AdapterPesananResepAdmin(Context context) {
        this.listpesananresep = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AdapterPesananResepAdmin.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowpesananresepadmin, parent, false);
        return new ViewHolder(v);

    }

    void add(ModelPesananResepAdmin item) {
        listpesananresep.add(item);
        notifyItemInserted(listpesananresep.size());

    }
    void addAll(List<ModelPesananResepAdmin> list) {
        for (ModelPesananResepAdmin number : list) {
            add(number);
        }
}

    void clear(){ //refresh
        listpesananresep.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AdapterPesananResepAdmin.ViewHolder holder, int position) {
        ModelPesananResepAdmin modelPesananResepAdmin = listpesananresep.get(position);
        if (modelPesananResepAdmin.getStatus() == 4) {
            holder.rowPesananResep.setBackgroundColor(Color.parseColor("#FAF0E6")); //warna pada listpesanan yg sdh diterima user
        } else {
            holder.rowPesananResep.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }


        holder.waktu.setText(modelPesananResepAdmin.getWaktu());
        holder.invoice.setText("#"+ modelPesananResepAdmin.getInvoice());
        holder.nama.setText(modelPesananResepAdmin.getNama_penerima());

        Picasso.get()
                .load(modelPesananResepAdmin.getGambar_resep())
                .into(holder.gambar);

        holder.lihatDetailPesananResep.setOnClickListener(v -> onItemCLick.onItemClick(listpesananresep.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() { return listpesananresep.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView waktu, invoice, nama;
        ImageView gambar;
        TextView lihatDetailPesananResep;
        RelativeLayout rowPesananResep;

        public ViewHolder(View itemView) {
            super(itemView);
            waktu = itemView.findViewById(R.id.tv_waktu_pesanan_resep);
            lihatDetailPesananResep = itemView.findViewById(R.id.tv_lihat_detail_pesanan_resep_admin);
            invoice = itemView.findViewById(R.id.tv_invoice_resep_admin);
            gambar = itemView.findViewById(R.id.iv_gambar_pesanan_resep_admin);
            nama = itemView.findViewById(R.id.tv_nama_pengirim_resep_admin);
            rowPesananResep = itemView.findViewById(R.id.rl_pesanan_resep);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelPesananResepAdmin data);
    }
}
