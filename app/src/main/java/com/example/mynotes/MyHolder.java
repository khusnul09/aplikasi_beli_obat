package com.example.mynotes;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView mImaeView;
    TextView mTitle, mHarga, mSatuan;
    int imageViewResource;
    ItemClickListener itemClickListener;

    ImageButton tambah;
    ImageButton kurang;
    TextView jumlah;
    public int jumlahAngka=0;

    MyHolder(@NonNull View itemView) {
        super(itemView);

        this.mSatuan = itemView.findViewById(R.id.tv_satuan);
        this.mImaeView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleTv);
        this.mHarga = itemView.findViewById(R.id.tv_harga_obat);
        this.jumlah = itemView.findViewById(R.id.jumlah);
        this.tambah = itemView.findViewById(R.id.tambah);
        kurang = itemView.findViewById(R.id.kurang);
        imageViewResource = 0;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        this.itemClickListener.onItemClickListener(v,getLayoutPosition());

    }

    public void setItemClickListener(ItemClickListener ic){

        this.itemClickListener = ic;
    }
}
