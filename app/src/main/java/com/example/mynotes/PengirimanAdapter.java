package com.example.mynotes;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PengirimanAdapter extends RecyclerView.Adapter<PengirimanHolder> {

    Context c;
    ArrayList<Model> models, filterList; //this array lis create a list of array which parameter defice in our model class
    int gJumlah;
    boolean canModifyQuantity;
    ICart iCart;

    // now create a parameterized constructor, press alt+insert

    public PengirimanAdapter(Context c, ArrayList<Model> models, boolean canModifyQuantity, ICart iCart) {
        this.c = c;
        this.models = models;
        this.filterList = models;
        this.canModifyQuantity = canModifyQuantity;
        this.iCart = iCart;
    }

    @NonNull
    @Override
    public PengirimanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowpengiriman, null, false);// this line inflate our row

        return new PengirimanHolder(view); // this vill return our view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull final PengirimanHolder myHolder, int i) {

        Model currentItem = models.get(i);

        myHolder.mTitle.setText(currentItem.getNamaObat()); //here i is position
        myHolder.mDes.setText(currentItem.getHargaJual() +",-"+ "");
        myHolder.jumlah.setText("x"+currentItem.getQuantity() + "");
        myHolder.jumlahAngka = 0;

        if (currentItem.getGambar() != "") {
            //myHolder.mImaeView.setImageResource(currentItem.getImage());//here we used image resource because we will use images in our
            //myHolder.imageViewResource = currentItem.getImage();
            Glide.with(c).load(currentItem.getGambar()).into(myHolder.mImaeView); //tampilkan gambar obat
        } else {
            myHolder.mImaeView.setImageResource(R.drawable.ic_drugs);//here we used image resource because we will use images in our
            myHolder.imageViewResource = R.drawable.ic_drugs;
        }


        //friends this method is than you can use when you want to use one activity
        myHolder.setItemClickListener((v, position) -> {


        });


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface ICart {
        void onItemSelected(String title, Integer quantity);
    }
}