package com.example.sipmobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllMaintenanceActivity extends AppCompatActivity {

    EditText etSearch;
    ListView lvMaintenance;
    ProgressBar progressBar;
    private ArrayList<Maintenance> arrMaintenance = new ArrayList<Maintenance>();
    Maintenance maintenance;
    private RequestQueue mRequestQueue;
    private RequestQueue mRequestQueueImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_maintenance);

        etSearch = (EditText) findViewById(R.id.editTextSearchMaintenance);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMaintenance);
        progressBar.setVisibility(View.VISIBLE);
        lvMaintenance = (ListView) findViewById(R.id.ListViewMaintenance);

        insertDataJSONtoList();

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchData(etSearch.getText().toString());
            }
        });

        lvMaintenance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Maintenance dataMaintenance = arrMaintenance.get(i);

                Intent intent = new Intent(ViewAllMaintenanceActivity.this, InputOrUpdateMaintenanceActivity.class);
                // mengirim nilai dataMaintenance yg di klik ke UpdateOrDeleteMaintenanceActivity
                intent.putExtra("dataMaintenance", dataMaintenance);
                startActivity(intent);

                // Menutup ViewAllInventoryActivity agar tidak kembali lagi saat tombol "Back" ditekan
                finish();
            }
        });
    }

    private void insertDataJSONtoList() {
        // membuat object untuk request volley
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueueImage = Volley.newRequestQueue(this);

        // Buat paket perintah untuk mendapatkan data dengan metode POST pada alamat URL Load Data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOAD_DATA_MAINTENANCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // membuat var obj untuk menampung object
                            JSONObject obj = new JSONObject(response);

                            // cek nilai dari object error
                            if (!obj.getBoolean("error")) {
                                // mendapatkan nilai dari array dengan nama "data"
                                JSONArray dataMaintenanceArray = obj.getJSONArray("data");

                                for (int i = 0; i < dataMaintenanceArray.length(); i++) {
                                    // mendapatkan object dari array
                                    JSONObject dtobjMaintenance = dataMaintenanceArray.getJSONObject(i);
                                    arrMaintenance.add(
                                            new Maintenance(
                                                    // mendapatkan data dari masing-masing object di array
                                                    dtobjMaintenance.getString("Kode"),
                                                    dtobjMaintenance.getString("TanggalMaintenance"),
                                                    dtobjMaintenance.getString("VendorMaintenance"),
                                                    dtobjMaintenance.getString("StaffPIC"),
                                                    dtobjMaintenance.getString("Nama"),
                                                    dtobjMaintenance.getString("Foto")
                                            ));
                                }
                                // tampilkan data
                                setAdapter(arrMaintenance);
                            } else {
                                // tampilkan data jika terdapat error
                                Toast.makeText(ViewAllMaintenanceActivity.this, obj.getString(
                                                "message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // tampilkan message pada saat ada kesalahan eksekusi perintah
                            Toast.makeText(ViewAllMaintenanceActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllMaintenanceActivity.this, "Tidak dapat memuat ulang data Maintenance",
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
                URLs.URL_LOAD_DATA_MAINTENANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){
                        JSONArray dataMaintenanceArray = obj.getJSONArray("data");
                        arrMaintenance.clear();
                        for (int i = 0; i < dataMaintenanceArray.length(); i++){
                            JSONObject dtobjMaintenance = dataMaintenanceArray.getJSONObject(i);
                            maintenance = new Maintenance(
                                    dtobjMaintenance.getString("Kode"),
                                    dtobjMaintenance.getString("TanggalMaintenance"),
                                    dtobjMaintenance.getString("VendorMaintenance"),
                                    dtobjMaintenance.getString("StaffPIC"),
                                    dtobjMaintenance.getString("Nama"),
                                    dtobjMaintenance.getString("Foto")
                            );
                            arrMaintenance.add(maintenance);
                        }

                        // tampilkan data
                        setAdapter(arrMaintenance);
                    } else {
                        // tampilkan data jika terdapat error
                        Toast.makeText(ViewAllMaintenanceActivity.this, obj.getString(
                                        "message"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(ViewAllMaintenanceActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllMaintenanceActivity.this, "Tidak dapat memuat ulang data Maintenance",
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

    private void setAdapter(ArrayList<Maintenance> list) {
        ArrayAdapter<Maintenance> adapter = new MyListAdapter(list);
        lvMaintenance.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
    }

    private class MyListAdapter extends ArrayAdapter<Maintenance> {
        private List<Maintenance> itemList;

        public MyListAdapter(List<Maintenance> itemList) {
            super(ViewAllMaintenanceActivity.this, R.layout.item_list_maintenance, itemList);
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_maintenance, parent, false);
            }

            Maintenance maintenance = itemList.get(position);

            TextView tvNamaMaintenance = (TextView) convertView.findViewById(R.id.textViewNamaMaintenance);
            TextView tvKodeMaintenance = (TextView) convertView.findViewById(R.id.textViewKodeMaintenance);
            TextView tvTanggalMaintenance = (TextView) convertView.findViewById(R.id.textViewTanggalMaintenance);
            ImageView imvMaintenance = (ImageView) convertView.findViewById(R.id.imageViewBarangMaintenance);

            mRequestQueueImage = Volley.newRequestQueue(ViewAllMaintenanceActivity.this);

                ImageRequest requestImage = new ImageRequest(URLs.URL_LOADIMAGE + maintenance.getFotoInventaris(),
                        new Response.Listener<Bitmap>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(Bitmap response) {
                                imvMaintenance.setImageBitmap(response);
                                tvNamaMaintenance.setText(maintenance.getNamaInventaris());
                                tvKodeMaintenance.setText("Kode : " + maintenance.getKode());
                                if (!"null".equals(maintenance.getTanggalMaintenance())) {
                                    tvTanggalMaintenance.setText(maintenance.getTanggalMaintenance());
                                } else {
                                    tvTanggalMaintenance.setText("-");
                                }
                            }
                        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ViewAllMaintenanceActivity.this, "Error Loading Image: " + error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                mRequestQueueImage.add(requestImage);

            return convertView;
        }
    }
}