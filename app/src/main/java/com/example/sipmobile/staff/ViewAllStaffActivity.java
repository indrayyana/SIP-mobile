package com.example.sipmobile.staff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class ViewAllStaffActivity extends AppCompatActivity {

    EditText etSearch;
    ListView lvStaff;
    FloatingActionButton fabAdd;
    ProgressBar progressBar;
    private ArrayList<Staff> arrStaff = new ArrayList<Staff>();
    Staff staff;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_staff);

        etSearch = (EditText) findViewById(R.id.editTextSearchStaff);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAddStaff);
        progressBar = (ProgressBar) findViewById(R.id.progressBarStaff);
        progressBar.setVisibility(View.VISIBLE);
        lvStaff = (ListView) findViewById(R.id.ListViewStaff);

        insertDataJSONtoList();

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchData(etSearch.getText().toString());
            }
        });

        lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Staff dataStaff = arrStaff.get(i);

                Intent intent = new Intent(ViewAllStaffActivity.this, UpdateOrDeleteStaffActivity.class);
                // mengirim nilai dataStaff yg di klik ke UpdateOrDeleteStaffActivity
                intent.putExtra("dataStaff", dataStaff);
                startActivity(intent);

                // Menutup ViewAllStaffActivity agar tidak kembali lagi saat tombol "Back" ditekan
                finish();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllStaffActivity.this, InputStaffActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertDataJSONtoList() {
        // membuat object untuk request volley
        mRequestQueue = Volley.newRequestQueue(this);

        // Buat paket perintah untuk mendapatkan data dengan metode POST pada alamat URL Load Data
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOAD_DATA_STAFF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // membuat var obj untuk menampung object
                            JSONObject obj = new JSONObject(response);

                            // cek nilai dari object error
                            if (!obj.getBoolean("error")) {
                                // mendapatkan nilai dari array dengan nama "data"
                                JSONArray dataStaffArray = obj.getJSONArray("data");

                                for (int i = 0; i < dataStaffArray.length(); i++) {
                                    // mendapatkan object dari array
                                    JSONObject dtobjStaff = dataStaffArray.getJSONObject(i);
                                    arrStaff.add(
                                            new Staff(
                                                    // mendapatkan data dari masing-masing object di array
                                                    dtobjStaff.getInt("Id"),
                                                    dtobjStaff.getString("Nama"),
                                                    dtobjStaff.getString("Jabatan"),
                                                    dtobjStaff.getString("Tipe"),
                                                    dtobjStaff.getInt("Gaji"),
                                                    dtobjStaff.getInt("TahunBergabung")
                                            ));
                                }
                                // tampilkan data
                                setAdapter(arrStaff);
                            } else {
                                // tampilkan data jika terdapat error
                                Toast.makeText(ViewAllStaffActivity.this, obj.getString(
                                                "message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // tampilkan message pada saat ada kesalahan eksekusi perintah
                            Toast.makeText(ViewAllStaffActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllStaffActivity.this, "Tidak dapat memuat ulang data Staff",
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
                URLs.URL_LOAD_DATA_STAFF, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (!obj.getBoolean("error")){
                        JSONArray dataStaffArray = obj.getJSONArray("data");
                        arrStaff.clear();
                        for (int i = 0; i < dataStaffArray.length(); i++){
                            JSONObject dtobjStaff = dataStaffArray.getJSONObject(i);
                            staff = new Staff (
                                    dtobjStaff.getInt("Id"),
                                    dtobjStaff.getString("Nama"),
                                    dtobjStaff.getString("Jabatan"),
                                    dtobjStaff.getString("Tipe"),
                                    dtobjStaff.getInt("Gaji"),
                                    dtobjStaff.getInt("TahunBergabung")
                            );
                            arrStaff.add(staff);
                        }

                        // tampilkan data
                        setAdapter(arrStaff);
                    }else{
                        // tampilkan data jika terdapat error
                        Toast.makeText(ViewAllStaffActivity.this, obj.getString(
                                        "message"),
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(ViewAllStaffActivity.this,
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // tampilkan error saat terjadi kesalahan pada saat melakukan proses response
                Toast.makeText(ViewAllStaffActivity.this, "Tidak dapat memuat ulang data Staff",
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

    private void setAdapter(ArrayList<Staff> list) {
        ArrayAdapter<Staff> adapter = new MyListAdapter(list);
        lvStaff.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
    }

    private class MyListAdapter extends ArrayAdapter<Staff> {
        private List<Staff> itemList;

        public MyListAdapter(List<Staff> itemList) {
            super(ViewAllStaffActivity.this, R.layout.item_list_staff, itemList);
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_staff, parent, false);
            }

            Staff staff = itemList.get(position);

            TextView tvNamaStaff = (TextView) convertView.findViewById(R.id.textViewNamaStaff);
            TextView tvJabatan = (TextView) convertView.findViewById(R.id.textViewJabatan);
            TextView tvTahunBergabung = (TextView) convertView.findViewById(R.id.textViewTahunBergabung);

            tvNamaStaff.setText(staff.getNama());
            tvJabatan.setText("Jabatan : " + staff.getJabatan());
            tvTahunBergabung.setText(String.format("Tahun Bergabung : %d", staff.getTahunBergabung()));

            return convertView;
        }
    }
}