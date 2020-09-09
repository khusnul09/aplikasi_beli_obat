package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class PesanTanpaResepActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    ImageButton btnKeranjang;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tanpa_resep);

        mRecyclerView = findViewById(R.id.recyclerView);
        getMyList();
    }

    private void getMyList() {

        ArrayList<Model> models = new ArrayList<>();

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


        //mengubah jadi 2
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2, GridLayoutManager.VERTICAL, false)); // i will create in linearlayout

        myAdapter = new MyAdapter(this, models);
        mRecyclerView.setAdapter(myAdapter);

        //..............................video 1.................
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
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


