package com.example.mynotes;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ImageView mImaeView;
    TextView mTitle, mDes;
    ItemClickListener itemClickListener;

    Button tambah;
    Button kurang;
    TextView jumlah;
    int jumlahAngka=0;

    MyHolder(@NonNull View itemView) {
        super(itemView);

        this.mImaeView = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleTv);
        this.mDes = itemView.findViewById(R.id.descriptionTv);
        this.jumlah = itemView.findViewById(R.id.jumlah);
        this.tambah = itemView.findViewById(R.id.tambah);
        kurang = itemView.findViewById(R.id.kurang);

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
