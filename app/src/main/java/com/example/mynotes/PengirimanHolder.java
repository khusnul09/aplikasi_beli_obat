package com.example.mynotes;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PengirimanHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView mImaeView;
    TextView mTitle, mDes;
    int imageViewResource;
    ItemClickListener itemClickListener;

    TextView jumlah;
    public int jumlahAngka=0;

    PengirimanHolder(@NonNull View itemView) {
        super(itemView);

        this.mImaeView = itemView.findViewById(R.id.iv_image_pengiriman);
        this.mTitle = itemView.findViewById(R.id.tv_title_pengiriman);
        this.mDes = itemView.findViewById(R.id.tv_harga_pengiriman);
        this.jumlah = itemView.findViewById(R.id.tv_total_pengiriman);
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
