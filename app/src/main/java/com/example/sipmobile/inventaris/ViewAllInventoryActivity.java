package com.example.sipmobile.inventaris;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sipmobile.R;
import com.example.sipmobile.URLs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllInventoryActivity extends AppCompatActivity {

    EditText etSearch;
    ListView lvInventaris;
    FloatingActionButton fabAdd;
    ProgressBar progressBar;
    private ArrayList<Inventaris> arrInventaris = new ArrayList<Inventaris>();
    Inventaris inventaris;

    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueueImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_inventory);

        etSearch = (EditText) findViewById(R.id.editTextSearchInventaris);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lvInventaris = (ListView) findViewById(R.id.ListViewInventaris);

        insertDataJSONtoList();

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchData(etSearch.getText().toString());
            }
        });

        lvInventaris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Inventaris dataInventaris = arrInventaris.get(i);

                Intent intent = new Intent(ViewAllInventoryActivity.this, UpdateOrDeleteInventarisActivity.class);
                // mengirim nilai dataInventaris yg di klik ke UpdateOrDeleteInventarisActivity
                intent.putExtra("dataInventaris", dataInventaris);
                startActivity(intent);

                // Menutup ViewAllInventoryActivity agar tidak kembali lagi saat tombol "Back" ditekan
                finish();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllInventoryActivity.this, InputInventarisActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertDataJSONtoList() {
        // membuat object untuk request volley
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueueImage = Volley.newRequestQueue(this);

        // Buat paket perintah untuk mendapatkan data dengan metode POST pada alamat URL Load Data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOAD_DATA_INVENTARIS,
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
                                setAdapter(arrInventaris);
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
                progressBar.setVisibility(View.GONE);

                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllInventoryActivity.this, "Tidak dapat memuat ulang data Inventaris",
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            // menyimpan parameter yang akan dikirim
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                // input parameter ke api dengan nama 'Nama' dengan nilai "Kosong"
                params.put("Nama", "Kosong");
                return params;
            }
        };
        // kirim paket perintah ke server
        mRequestQueue.add(stringRequest);
    }

    private void searchData(String key) {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_LOAD_DATA_INVENTARIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){
                        JSONArray dataInventarisArray = obj.getJSONArray("data");
                        arrInventaris.clear();
                        for (int i = 0; i < dataInventarisArray.length(); i++){
                            JSONObject dtobjInventaris = dataInventarisArray.getJSONObject(i);
                            inventaris = new Inventaris(
                                    dtobjInventaris.getString("Kode"),
                                    dtobjInventaris.getString("Nama"),
                                    dtobjInventaris.getString("Kategori"),
                                    dtobjInventaris.getString("Tipe"),
                                    dtobjInventaris.getString("Foto"),
                                    dtobjInventaris.getInt("Jumlah"),
                                    dtobjInventaris.getInt("HargaBeli"),
                                    dtobjInventaris.getInt("TahunBeli")
                            );
                            arrInventaris.add(inventaris);
                        }

                        // tampilkan data
                        setAdapter(arrInventaris);
                    }else{
                        // tampilkan data jika terdapat error
                        Toast.makeText(ViewAllInventoryActivity.this, obj.getString(
                                        "message"),
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(ViewAllInventoryActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllInventoryActivity.this, "Tidak dapat memuat ulang data Inventaris",
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Nama", key);
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
    }

    private void setAdapter(ArrayList<Inventaris> list) {
        ArrayAdapter<Inventaris> adapter = new MyListAdapter(list);
        lvInventaris.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
    }

    private class MyListAdapter extends ArrayAdapter<Inventaris> {
        private List<Inventaris> itemList;

        public MyListAdapter(List<Inventaris> itemList) {
            super(ViewAllInventoryActivity.this, R.layout.item_list_inventaris, itemList);
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_inventaris, parent, false);
            }

            Inventaris inventaris = itemList.get(position);

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