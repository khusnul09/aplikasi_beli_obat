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

public class AdapterDetailTanpaResep extends RecyclerView.Adapter<AdapterDetailTanpaResep.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<ModelDetailTanpaResep> listDetailTanpaResep;
    Context context;

    public AdapterDetailTanpaResep(Context context) {
        this.listDetailTanpaResep = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public AdapterDetailTanpaResep.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowpengiriman, parent, false);
        return new ViewHolder(v);
    }

    void add(ModelDetailTanpaResep item) {
        listDetailTanpaResep.add(item);
        notifyItemInserted(listDetailTanpaResep.size());
    }

    void addAll(List<ModelDetailTanpaResep> list) {
        for (ModelDetailTanpaResep number : list) {
            add(number);
        }
    }

    @Override
    public void onBindViewHolder(AdapterDetailTanpaResep.ViewHolder holder, int position) {
        ModelDetailTanpaResep modelDetailTanpaResep = listDetailTanpaResep.get(position);
        holder.nama_obat.setText(modelDetailTanpaResep.getNamaObat());
        holder.kuantitas.setText("x"+ modelDetailTanpaResep.getKuantitasObat());
//        holder.harga.setText(modelDetailTanpaResep.getHargaObat()+",-");
        holder.harga.setText(Rupiah.formatUangId(context, Double.parseDouble(String.valueOf(modelDetailTanpaResep.getHargaObat()))));
        Picasso.get().load(modelDetailTanpaResep.getGambar()).into(holder.gambar); //menampilkan gambar dari db
    }

    @Override
    public int getItemCount() { return listDetailTanpaResep.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nama_obat, kuantitas, harga;
        ImageView gambar;

        public ViewHolder(View itemView) {
            super(itemView);
            nama_obat = itemView.findViewById(R.id.tv_title_pengiriman);
            kuantitas = itemView.findViewById(R.id.tv_total_pengiriman);
            harga = itemView.findViewById(R.id.tv_harga_pengiriman);
            gambar = itemView.findViewById(R.id.iv_image_pengiriman);
        }
    }
}
