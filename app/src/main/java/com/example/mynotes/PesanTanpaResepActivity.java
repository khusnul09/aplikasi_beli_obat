package com.example.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mynotes.api.ApiConfig;
import com.example.mynotes.api.ApiInterface;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.mynotes.R.id.search;
import static java.security.AccessController.getContext;

public class PesanTanpaResepActivity extends AppCompatActivity implements MyAdapter.ICart {

    private static final String urlKeranjang = "https://obats.000webhostapp.com//api/user/keranjang";

    // String url = "https://obats.000webhostapp.com/api/obats";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    Button btnKeranjang;
    ArrayList<Model> listNewObat = new ArrayList<>();
    HashMap<String, Integer> itemsCart = new HashMap<>();
    ArrayList<Model> models = new ArrayList<>();
    String email_user;

    SharedPreferences preferences;

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesan_tanpa_resep);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#D9303E"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Pilih Obat"); //color and title actionbar
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Pilih Obat</font>"));

        email_user = SharedPreferenceManager.getStringPreferences(getApplicationContext(), "user_email");

        gson = new Gson();

        btnKeranjang = findViewById(R.id.btn_tambah_keranjang);
        btnKeranjang.setOnClickListener(v -> {
            setModelDataToCart();
            simpanKeranjang();
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        //getMyList();
        getListObat();
    }

    @Override
    public void onBackPressed() {
        Intent kembali = new Intent(this, PesanObatActivity.class);
        startActivity(kembali);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*private void getMyList() {

        Model m = new Model();
        m.setNamaObat("Acyclovir 200 mg tab");
        m.setHargaJual(655);
        m.setImage(R.drawable.acyclovir_200_mg_tab);
        m.setSatuan("tablet");
        models.add(m);

        m = new Model();
        m.setNamaObat("Acyclovir 400 mg tab");
        m.setHargaJual(833);
        m.setImage(R.drawable.acyclovir_400_mg_tab);
        m.setSatuan("tablet");
        models.add(m);

        m = new Model();
        m.setSatuan("ampul");
        m.setNamaObat("Amikasin 250 mg/2 ml inj");
        m.setHargaJual(35350);
        m.setImage(R.drawable.amikasin_250_mg_2_ml_inj);
        models.add(m);

        m = new Model();
        m.setNamaObat("Amoxicillin 500 mg tab");
        m.setHargaJual(258);
        m.setSatuan("tablet");
        m.setImage(R.drawable.amoxicillin_500_mg_tab);
        models.add(m);

        m = new Model();
        m.setSatuan("botol");
        m.setNamaObat("Amoxicillin syr botol");
        m.setHargaJual(5349);
        m.setImage(R.drawable.amoxicillin_syr_botol);
        models.add(m);

        m = new Model();
        m.setSatuan("vial");
        m.setNamaObat("Ampicillin 1000 mg inj vial");
        m.setHargaJual(9474);
        m.setImage(R.drawable.ampicillin_1000_mg_inj_vial);
        models.add(m);

        m = new Model();
        m.setSatuan("tablet");
        m.setNamaObat("Ampicillin 500 mg tab");
        m.setHargaJual(468);
        m.setImage(R.drawable.ampicillin_500_mg_tab);
        models.add(m);

        m = new Model();
        m.setSatuan("botol");
        m.setNamaObat("Ceftazidim 1 g inj vial");
        m.setHargaJual(5412);
        m.setImage(R.drawable.ceftazidim_1_g_inj_vial);
        models.add(m);

        m = new Model();
        m.setSatuan("kapsul");
        m.setNamaObat("Cefadroxil 500 mg kapsul");
        m.setHargaJual(792);
        m.setImage(R.drawable.cefadroxil_500_mg_kapsul);
        models.add(m);

        m = new Model();
        m.setSatuan("vial");
        m.setNamaObat("Cefepime 1 gr inj");
        m.setHargaJual(124782);
        m.setImage(R.drawable.cefixime_100_mg_kapsul);
        models.add(m);

        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2, GridLayoutManager.VERTICAL, false)); // i will create in linearlayout

        myAdapter = new MyAdapter(this, models, true, this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setItemViewCacheSize(models.size());
    }*/
    //first create an interface class
    //SORT

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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

    void setModelDataToCart() {
        listNewObat.clear();
        for (Model model : models) {
            for (Map.Entry<String, Integer> data : itemsCart.entrySet()) {
                if (model.getNamaObat().toLowerCase().equals(data.getKey().toLowerCase())) {
                    model.setQuantity(data.getValue());
                    listNewObat.add(model);
                }
            }
        }
    }

    @Override
    public void onItemSelected(String title, Integer quantity) {
        itemsCart.put(title, quantity);
        System.out.println("Title: " + title + ", " + "Quantity: " + quantity);
        // System.out.println("Test");
        // System.out.println("Model size: " + models.size());
        for (Model model: models) {
            // System.out.println("Iteration");
            // System.out.println("Item title: " + model.getNamaObat());
            if (model.getNamaObat().equals(title)) {
                System.out.println("Added Title: " + title + ", " + "Quantity: " + quantity);
                model.setQuantity(quantity);
            }
        }
        System.out.println(listNewObat.toString());
    }

    public void getListObat() {
        ApiInterface api = new ApiConfig().instance();
        Call<ArrayList<Model>> call = api.getListObat();

        ProgressDialog progressDialog = new ProgressDialog(PesanTanpaResepActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage("Silahkan tunggu..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        call.enqueue(new Callback<ArrayList<Model>>() {
            @Override
            public void onResponse(Call<ArrayList<Model>> call, Response<ArrayList<Model>> response) {
                models.clear();
                models = response.body();
                mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2, GridLayoutManager.VERTICAL, false)); // i will create in linearlayout
                myAdapter = new MyAdapter(PesanTanpaResepActivity.this, models, true, PesanTanpaResepActivity.this::onItemSelected);
                mRecyclerView.setAdapter(myAdapter);
                mRecyclerView.setItemViewCacheSize(models.size());

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Model>> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void simpanKeranjang() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mengirim...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String itemJson = gson.toJson(listNewObat);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlKeranjang,
                response -> {
                    Log.i("khatima", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String respon = jsonObject.getString("status");
                        Log.i("Khatima", respon);
                        if (jsonObject.getString("status").equals("berhasil")) {
                            System.out.println("Data berhasil dikirim");
                        }
                        Toast.makeText(getApplicationContext(), respon, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                    listNewObat.clear();
                    itemsCart.clear();
                    Intent intent = new Intent(PesanTanpaResepActivity.this, KeranjangActivity.class);
                    startActivity(intent);
                }, error -> {
            Toast.makeText(getApplicationContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id_user", email_user);
                params.put("list_obat", itemJson);
                return  params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PesanTanpaResepActivity.this);
        requestQueue.add(stringRequest);
    }
}