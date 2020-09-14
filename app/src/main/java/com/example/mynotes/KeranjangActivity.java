package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class KeranjangActivity extends AppCompatActivity implements MyAdapter.ICart {
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    ArrayList<Model> listObatToCart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        listObatToCart = (ArrayList<Model>) getIntent().getSerializableExtra("CART");

        Log.d("CART", listObatToCart.size() + " items");
        Toast.makeText(KeranjangActivity.this, listObatToCart.size() + " itm", Toast.LENGTH_SHORT).show();

        for (Model model : listObatToCart) {
            if (model.getQuantity()==0) {
                listObatToCart.remove(model);
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 2, GridLayoutManager.VERTICAL, false)); // i will create in linearlayout

        myAdapter = new MyAdapter(this, listObatToCart, false, this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemViewCacheSize(listObatToCart.size());
    }

    @Override
    public void onItemSelected(String title, Integer quantity) {

    }
}
