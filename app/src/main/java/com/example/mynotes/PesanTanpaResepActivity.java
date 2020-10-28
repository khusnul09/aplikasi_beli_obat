package com.example.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.mynotes.R.id.search;

public class PesanTanpaResepActivity extends AppCompatActivity implements MyAdapter.ICart {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    Button btnKeranjang;

    ArrayList<Model> listObatToCart = new ArrayList<>();
    HashMap<String, Integer> itemsCart = new HashMap<>();

    ArrayList<Model> models = new ArrayList<>();

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tanpa_resep);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#EF3D3D"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Pilih Obat"); //color and title actionbar
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Pilih Obat</font>"));

        btnKeranjang = findViewById(R.id.btn_tambah_keranjang);
        btnKeranjang.setOnClickListener(v -> {
            Intent intent = new Intent(PesanTanpaResepActivity.this, KeranjangActivity.class);
            setModelDataToCart();
            intent.putExtra("CART", listObatToCart);
            startActivity(intent);
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        getMyList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getMyList() {
        Model m = new Model();
        m.setTitle("Acyclovir 200 mg tab");
        m.setDescription("Rp.655,-/tablet");
        m.setImg(R.drawable.acyclovir_200_mg_tab);
        models.add(m);

        m = new Model();
        m.setTitle("Acyclovir 400 mg tab");
        m.setDescription("Rp.833,-/tablet");
        m.setImg(R.drawable.acyclovir_400_mg_tab);
        models.add(m);

        m = new Model();
        m.setTitle("Amikasin 250 mg/2 ml inj");
        m.setDescription("Rp.35.350,-/ampul");
        m.setImg(R.drawable.amikasin_250_mg_2_ml_inj);
        models.add(m);

        m = new Model();
        m.setTitle("Amoxicillin 500 mg tab");
        m.setDescription("Rp.258,-/tablet");
        m.setImg(R.drawable.amoxicillin_500_mg_tab);
        models.add(m);

        m = new Model();
        m.setTitle("Amoxicillin syr botol");
        m.setDescription("Rp.5.349,-/botol");
        m.setImg(R.drawable.amoxicillin_syr_botol);
        models.add(m);

        m = new Model();
        m.setTitle("Ampicillin 1000 mg inj vial");
        m.setDescription("Rp.9.474,-/psc");
        m.setImg(R.drawable.ampicillin_1000_mg_inj_vial);
        models.add(m);

        m = new Model();
        m.setTitle("Ampicillin 500 mg tab");
        m.setDescription("Rp.468,-/tablet");
        m.setImg(R.drawable.ampicillin_500_mg_tab);
        models.add(m);

        m = new Model();
        m.setTitle("Cefadroxil 125 mg/5 ml syr");
        m.setDescription("Rp.5.412,-/botol");
        m.setImg(R.drawable.cefadroxil_125_mg_5_ml_syr);
        models.add(m);

        m = new Model();
        m.setTitle("Cefadroxil 500 mg kapsul");
        m.setDescription("Rp.792,-/");
        m.setImg(R.drawable.cefadroxil_500_mg_kapsul);
        models.add(m);

        m = new Model();
        m.setTitle("Cefepime 1 gr inj");
        m.setDescription("Rp.124.782,-/");
        m.setImg(R.drawable.cefixime_100_mg_kapsul);
        models.add(m);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2, GridLayoutManager.VERTICAL, false)); // i will create in linearlayout

        myAdapter = new MyAdapter(this, models, true, this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemViewCacheSize(models.size());

    }
    //first create an interface class
    //SORT

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        /*MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                myAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                myAdapter.getFilter().filter(newText);
                return false;
            }
        });*/
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        myAdapter.getFilter().filter(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        myAdapter.getFilter().filter(newText);
                        return false;
                    }
                });
                return true;

            case R.id.cart_count_menu_item:
                Intent intent = new Intent(PesanTanpaResepActivity.this, KeranjangActivity.class);
                setModelDataToCart();
                intent.putExtra("CART", listObatToCart);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setModelDataToCart() {
        /*for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            MyHolder holder = (MyHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
            assert holder != null;
            try {
                if (holder.jumlahAngka>0) {
                    Model obat = new Model();
                    obat.setImg(holder.imageViewResource);
                    obat.setTitle(holder.mTitle.getText().toString());
                    obat.setDescription(holder.mDes.getText().toString());
                    obat.setQuantity(holder.jumlahAngka);
                    listObatToCart.add(obat);
                }
            } catch (Exception ignored) {

            }
        }*/
        listObatToCart.clear();
        for (Model model : models) {
            for (Map.Entry<String, Integer> data : itemsCart.entrySet()) {
                if (model.getTitle().toLowerCase().equals(data.getKey().toLowerCase())) {
                    model.setQuantity(data.getValue());
                    listObatToCart.add(model);
                }
            }
        }
    }

    @Override
    public void onItemSelected(String title, Integer quantity) {
        /*Log.e("CART", "TRIGGERED");
        if (listObatToCart.size()>0) {
            for (Model element : listObatToCart) {
                if (element.getTitle().toLowerCase().equals(model.getTitle().toLowerCase())) {
                    element.setQuantity(model.getQuantity());
                } else {
                    listObatToCart.add(model);
                }
            }
        } else {
            listObatToCart.add(model);
        }*/
        itemsCart.put(title, quantity);
    }
}


