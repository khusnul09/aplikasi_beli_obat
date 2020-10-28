package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class PengirimanActivity extends AppCompatActivity implements MyAdapter.ICart{
    Button buatPesanan;
    RecyclerView mRecyclerViewCart;
    PengirimanAdapter myAdapter;
    ImageView kembali;

    ArrayList<Model> listObatToCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengiriman);

        buatPesanan = findViewById(R.id.btn_buat_pesanan);
        buatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PembayaranActivity.class);
                startActivity(intent);
            }
        });

//        kembali = findViewById(R.id.iv_kembali7);
//        kembali.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), KeranjangActivity.class);
//                startActivity(intent);
//            }
//        });

        listObatToCart = (ArrayList<Model>) getIntent().getSerializableExtra("CART");
        mRecyclerViewCart = findViewById(R.id.recyclerViewCart);
        mRecyclerViewCart.setLayoutManager(new LinearLayoutManager(mRecyclerViewCart.getContext(), RecyclerView.VERTICAL, false)); // i will create in linearlayout
        myAdapter = new PengirimanAdapter(this, listObatToCart, false, this::onItemSelected);
        mRecyclerViewCart.setAdapter(myAdapter);
        //mRecyclerViewCart.setItemViewCacheSize(listObatToCart.size());
    }

    @Override
    public void onItemSelected(String title, Integer quantity) {

    }
}