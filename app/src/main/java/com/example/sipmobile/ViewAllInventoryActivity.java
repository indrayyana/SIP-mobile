package com.example.sipmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAllInventoryActivity extends AppCompatActivity {

    ListView lvInventaris;
    FloatingActionButton fabAdd;
    ProgressBar progressBar;
    private ArrayList<Inventaris> arrInventaris = new ArrayList<Inventaris>();
    int currpos = 0;
    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueueImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_inventory);

        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lvInventaris = (ListView) findViewById(R.id.ListViewInventaris);

        lvInventaris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ViewAllInventoryActivity.this, arrInventaris.get(i).getKode(),
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ViewAllInventoryActivity.this, UpdateOrDeleteInventarisActivity.class);
                startActivity(intent);
            }
        });

        insertDataJSONtoList();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllInventoryActivity.this, InputInventarisActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isInternetAvailable() {
        // Dapatkan instance dari ConnectivityManager
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Dapatkan informasi tentang koneksi aktif
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Periksa apakah ada koneksi internet
        return networkInfo != null && networkInfo.isConnected();
    }

    private void insertDataJSONtoList() {
        // membuat object untuk request volley
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueueImage = Volley.newRequestQueue(this);

        // Buat paket perintah untuk mendapatkan data dengan metode POST pada alamat URL Load Data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOAD_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // membuat var obj untuk menampung object
                            JSONObject obj = new JSONObject(response);

                            // cek nilai dari object error
                            if (!obj.getBoolean("error")) {
                                // mendapatkan nilai dari array dengan nama "data"
                                JSONArray dataInventarisArray = obj.getJSONArray("data");

                                for (int i = 0; i < dataInventarisArray.length(); i++) {
                                    // mendapatkan object dari array
                                    JSONObject dtobjInventaris = dataInventarisArray.getJSONObject(i);
                                    arrInventaris.add(
                                            new Inventaris(
                                                    // mendapatkan data dari masing-masing object di array
                                                    dtobjInventaris.getString("Kode"),
                                                    dtobjInventaris.getString("Nama"),
                                                    dtobjInventaris.getString("Kategori"),
                                                    dtobjInventaris.getString("Tipe"),
                                                    dtobjInventaris.getString("Foto"),
                                                    dtobjInventaris.getInt("Jumlah"),
                                                    dtobjInventaris.getInt("HargaBeli"),
                                                    dtobjInventaris.getInt("TahunBeli")
                                            ));
                                }
                                // tampilkan data
                                setAdapter();
                            } else {
                                // tampilkan data jika terdapat error
                                Toast.makeText(ViewAllInventoryActivity.this, obj.getString(
                                                "message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // tampilkan message pada saat ada kesalahan eksekusi perintah
                            Toast.makeText(ViewAllInventoryActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllInventoryActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            // menyimpan parameter yang akan dikirim
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                // input parameter ke api dengan nama IDInv dengan nilai "Kosong"
                params.put("IDInv", "Kosong");
                return params;
            }
        };
        // kirim paket perintah ke server
        mRequestQueue.add(stringRequest);
    }

    private void setAdapter() {
        ArrayAdapter<Inventaris> adapter = new MyListAdapter();
        lvInventaris.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
    }

    private class MyListAdapter extends ArrayAdapter<Inventaris>{
        public MyListAdapter() {
            super(ViewAllInventoryActivity.this, R.layout.item_list_inventaris, arrInventaris);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_inventaris, parent, false);
            }

            Inventaris inventaris = arrInventaris.get(position);

            TextView tvNamaBarang = (TextView) convertView.findViewById(R.id.textViewNamaBarang);
            TextView tvKodeBarang = (TextView) convertView.findViewById(R.id.textViewKodeBarang);
            TextView tvJumlahBarang = (TextView) convertView.findViewById(R.id.textViewJumlahBarang);
            ImageView imvBarang = (ImageView) convertView.findViewById(R.id.imageViewBarang);

            mRequestQueueImage = Volley.newRequestQueue(ViewAllInventoryActivity.this);

            ImageRequest requestImage = new ImageRequest(URLs.URL_LOADIMAGE + inventaris.getPath(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imvBarang.setImageBitmap(response);
                            tvNamaBarang.setText(inventaris.getNama());
                            tvKodeBarang.setText("Kode : " + inventaris.getKode());
                            tvJumlahBarang.setText(String.format("Jumlah : %,d", inventaris.getJumlah()));
                        }
                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewAllInventoryActivity.this, "Error Loading Image: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            mRequestQueueImage.add(requestImage);

            return convertView;
        }
    }
}