package com.example.mynotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartHolder> {

    Context c;
    ArrayList<Model> models, filterList; //this array lis create a list of array which parameter defice in our model class
    int gJumlah;
    boolean canModifyQuantity;
    ICart iCart;

    // now create a parameterized constructor, press alt+insert

    public MyCartAdapter(Context c, ArrayList<Model> models, boolean canModifyQuantity, ICart iCart) {
        this.c = c;
        this.models = models;
        this.filterList = models;
        this.canModifyQuantity = canModifyQuantity;
        this.iCart = iCart;
    }

    @NonNull
    @Override
    public MyCartHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowcart, null, false);// this line inflate our row

        return new MyCartHolder(view); // this vill return our view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartHolder myHolder, int i) {

        Model currentItem = models.get(i);

        myHolder.mTitle.setText(currentItem.getNamaObat()); //here i is position
//        myHolder.mDes.setText(currentItem.getHargaJual() +",-" +"");
        myHolder.mDes.setText(Rupiah.formatUangId(c, Double.parseDouble(String.valueOf(currentItem.getHargaJual()))));
        myHolder.jumlah.setText("x"+ currentItem.getQuantity() + "");
        myHolder.jumlahAngka = 0;

        if (currentItem.getGambar() != "") {
            //myHolder.mImaeView.setImageResource(currentItem.getImage());//here we used image resource because we will use images in our
            //myHolder.imageViewResource = currentItem.getImage();
            Glide.with(c).load(currentItem.getGambar()).into(myHolder.mImaeView);
        } else {
            myHolder.mImaeView.setImageResource(R.drawable.ic_drugs);//here we used image resource because we will use images in our
            myHolder.imageViewResource = R.drawable.ic_drugs;
        }

        myHolder.tambah.setOnClickListener(v -> {
            myHolder.jumlahAngka++;
            myHolder.jumlah.setText(myHolder.jumlahAngka + "");
            gJumlah = myHolder.jumlahAngka;
            iCart.onItemSelected(currentItem.getNamaObat(), myHolder.jumlahAngka);
        });
        myHolder.kurang.setOnClickListener(v -> {
            if (myHolder.jumlahAngka > 0) {
                myHolder.jumlahAngka--;
            }
            myHolder.jumlah.setText(myHolder.jumlahAngka + "");
            gJumlah = myHolder.jumlahAngka;
            iCart.onItemSelected(currentItem.getNamaObat(), myHolder.jumlahAngka);
        });

        myHolder.buttonHapus.setOnClickListener(v -> {
            removeAt(i);
            iCart.onItemSelected("delete", i);
        });


        //friends this method is than you can use when you want to use one activity
        myHolder.setItemClickListener((v, position) -> {
            String gTitle = models.get(position).getNamaObat();
            String gDesc = models.get(position).getHargaJual()+""; //this object our data from previous activity
            BitmapDrawable bitmapDrawable = (BitmapDrawable) myHolder.mImaeView.getDrawable(); //this will get image from drawable
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream(); //image will get stream and bytes
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); //it will compress our image
            byte[] bytes = stream.toByteArray();

            //get our data with intent
            Intent intent = new Intent(c, AnotherActivity.class);
            intent.putExtra("iTitle", gTitle);
            intent.putExtra("iDesc", gDesc); //get data add put in intent
            intent.putExtra("iImage", bytes);
            intent.putExtra("jumlah", gJumlah);
            intent.putExtra("gambar", models.get(position).getGambar());
            c.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface ICart {
        void onItemSelected(String title, Integer quantity);
    }

    public void removeAt(int position) {
        models.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, models.size());
    }
}