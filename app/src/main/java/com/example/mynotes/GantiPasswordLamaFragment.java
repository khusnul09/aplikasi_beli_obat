package com.example.mynotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GantiPasswordLamaFragment extends Fragment {

    Button lanjutkan;
    TextInputEditText textInputEditText;

    String urlCekPassLama = "https://obats.000webhostapp.com/api/user/cekpassword";
    String email, passlama;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewPasswordLama = inflater.inflate(R.layout.fragment_ganti_password_lama, container, false);

        email = SharedPreferenceManager.getStringPreferences(getContext(), "user_email");

        textInputEditText = viewPasswordLama.findViewById(R.id.til_password_lama);
        lanjutkan = viewPasswordLama.findViewById(R.id.btn_password_lama);

        lanjutkan.setOnClickListener(view -> cekPasswordLama());

        return viewPasswordLama;
    }

    public void cekPasswordLama() {
        passlama = textInputEditText.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCekPassLama, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("khatima", response);
                Log.d("khatima", passlama);
                if (jsonObject.getString("status").equals("benar")) {
                    GantiPasswordBaruFragment passwordBaruFragment = new GantiPasswordBaruFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_ganti_password, passwordBaruFragment);
                    fragmentTransaction.commit();
                } else if (jsonObject.getString("status").equals("salah")) {
                    Toast.makeText(getContext(), "Password Salah", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {}) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("password_lama", passlama);
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }
}