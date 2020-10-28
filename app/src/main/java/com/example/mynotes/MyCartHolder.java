package com.example.mynotes;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView mImaeView;
    TextView mTitle, mDes;
    int imageViewResource;
    ItemClickListener itemClickListener;

    Button tambah;
    Button kurang;
    TextView jumlah;
    public int jumlahAngka=0;

    MyCartHolder(@NonNull View itemView) {
        super(itemView);

        this.mImaeView = itemView.findViewById(R.id.iv_image);
        this.mTitle = itemView.findViewById(R.id.tv_title);
        this.mDes = itemView.findViewById(R.id.tv_harga);
        this.jumlah = itemView.findViewById(R.id.btn_jumlah);
        this.tambah = itemView.findViewById(R.id.btn_tambah);
        kurang = itemView.findViewById(R.id.btn_kurang);
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
