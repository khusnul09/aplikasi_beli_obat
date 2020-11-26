package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class KeranjangActivity extends AppCompatActivity implements MyCartAdapter.ICart {
    RecyclerView mRecyclerView;
    MyCartAdapter myAdapter;
    TextView tambahProduk;
    Button lanjutBayar;
    ImageView kembali;
    TextView totalTV;

    ArrayList<Model> listObatToCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        lanjutBayar = findViewById(R.id.btn_lanjut_bayar);
        lanjutBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PengirimanActivity.class);
                intent.putExtra("CART", listObatToCart);
                startActivity(intent);
            }
        });

        kembali = findViewById(R.id.iv_kembali6);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), PesanTanpaResepActivity.class);
                startActivity(intent);

            }
        });

        tambahProduk = findViewById(R.id.tv_tambah_produk_lain);
        tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //kembali ke pilih obat
            }
        });

        listObatToCart = (ArrayList<Model>) getIntent().getSerializableExtra("CART");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), RecyclerView.VERTICAL, false)); // i will create in linearlayout
        myAdapter = new MyCartAdapter(this, listObatToCart, false, this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemViewCacheSize(listObatToCart.size());

        totalTV = findViewById(R.id.tv_nominal_total);

        int totalInt = 0;
        for (Model model : listObatToCart) {
            totalInt+=(model.getHargaJual()*model.getQuantity());
        }

        totalTV.setText(totalInt+ "-,");
    }


    @Override
    public void onItemSelected(String title, Integer quantity) {
        if (title.equals("delete")) {
            listObatToCart.remove(quantity);
            int totalInt = 0;
            for (Model model : listObatToCart) {
                totalInt+=(model.getHargaJual()*model.getQuantity());
            }

            totalTV.setText(totalInt+ "");
        }
    }
}
