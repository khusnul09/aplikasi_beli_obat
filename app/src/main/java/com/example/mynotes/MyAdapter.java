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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {

    Context c;
    ArrayList<Model> models, filterList; //this array lis create a list of array which parameter defice in our model class
    CustomFilter filter;
    int gJumlah;

    // now create a parameterized constructor, press alt+insert

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, null, false);// this line inflate our row

        return new MyHolder(view); // this vill return our view to holder class
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, int i) {

        myHolder.mTitle.setText(models.get(i).getTitle()); //here i is position
        myHolder.mDes.setText(models.get(i).getDescription());
        myHolder.mImaeView.setImageResource(models.get(i).getImg());//here we used image resource because we will use images in our
        //resource folder which is drawable

        myHolder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHolder.jumlahAngka++;
                myHolder.jumlah.setText(myHolder.jumlahAngka+"");
                gJumlah = myHolder.jumlahAngka;
            }
        });
        myHolder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myHolder.jumlahAngka>0) {
                    myHolder.jumlahAngka--;
                }
                myHolder.jumlah.setText(myHolder.jumlahAngka+"");
                gJumlah = myHolder.jumlahAngka;
            }
        });


        //friends this method is than you can use when you want to use one activity
        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                String gTitle = models.get(position).getTitle();
                String gDesc = models.get(position).getDescription(); //this object our data from previous activity
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
                c.startActivity(intent);

            }
        });

        //if you want to use different activities than you can use this logic

        /*myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                if (models.get(position).getTitle().equals("Panadol")){
                    //then you can move another activity from if body
                }
                if (models.get(position).getTitle().equals("OBH")){
                    //then you can move another activity from if body
                }
                if (models.get(position).getTitle().equals("Combi")){
                    //then you can move another activity from if body
                }
                if (models.get(position).getTitle().equals("Mylanta")){
                    //then you can move another activity from if body
                }
                if (models.get(position).getTitle().equals("Woods")){
                    //then you can move another activity from if body
                }
            }
        });*/

        // but still we are using one activity so comment it..

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }
}