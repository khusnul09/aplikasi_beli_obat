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

public class AdapterKeranjang extends RecyclerView.Adapter<AdapterKeranjang.ViewHolder>{

    Context c;
    private final LayoutInflater inflater;
    private final ArrayList<ModelKeranjang> listKeranjang;
    private AdapterKeranjang.OnItemClick onItemCLick;

    public void setClickListener(OnItemClick onItemClick) {
        this.onItemCLick = onItemClick;
    }

    public AdapterKeranjang(Context context, ArrayList<ModelKeranjang> listCart) {
        this.c = context;
        this.listKeranjang = listCart;
        this.inflater = LayoutInflater.from(context);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View v = inflater.inflate(R.layout.rowcart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelKeranjang model = listKeranjang.get(position);
        holder.title.setText(model.getNama_obat());
        holder.price.setText(Rupiah.formatUangId(c, Double.parseDouble(String.valueOf(model.getHarga()))));
        holder.quantity.setText("x"+ model.getQuantity() + "");
        Picasso.get()
                .load(model.getGambar())
                .into(holder.image);

        holder.delete.setOnClickListener(v -> {
            onItemCLick.onItemClick(listKeranjang.get(holder.getAdapterPosition()), holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() { return listKeranjang.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, price, quantity;
        ImageView image, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            price = itemView.findViewById(R.id.tv_harga);
            quantity = itemView.findViewById(R.id.tv_jumlah);
            image = itemView.findViewById(R.id.iv_image);
            delete = itemView.findViewById(R.id.hapus);
        }
    }

    public interface OnItemClick {
        void onItemClick(ModelKeranjang data, int position);
    }
}
