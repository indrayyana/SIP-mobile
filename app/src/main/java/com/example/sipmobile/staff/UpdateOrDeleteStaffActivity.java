package com.example.sipmobile.staff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UpdateOrDeleteStaffActivity extends AppCompatActivity {

    int IDStaff;
    Button btUpdate, btDelete;
    String ModeMaintain, myMessage;
    RequestQueue mRequestQueue;
    ProgressBar pgs;
    Spinner spTipe;
    EditText etNama, etJabatan, etGaji, etTahunBergabung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delete_staff);

        etNama = (EditText) findViewById(R.id.editTextNamaStaffUpdateDelete);
        etJabatan = (EditText) findViewById(R.id.editTextJabatanStaffUpdateDelete);
        spTipe = (Spinner) findViewById(R.id.spinnerTipeStaffUpdateDelete);
        etGaji = (EditText) findViewById(R.id.editTextGajiStaffUpdateDelete);
        etTahunBergabung = (EditText) findViewById(R.id.editTextTahunBergabungUpdateDelete);
        pgs = (ProgressBar) findViewById(R.id.progressBarStaffUpdateDelete);
        btUpdate = (Button) findViewById(R.id.buttonUpdateStaff);
        btDelete = (Button) findViewById(R.id.buttonDeleteStaff);

        pgs.setVisibility(View.VISIBLE);

        // Custom warna text item spinner
        String[] itemTipeStaff = getResources().getStringArray(R.array.staff);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(itemTipeStaff));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_text_spinner, arrayList);
        spTipe.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataStaff")) {
            Staff dataStaff = (Staff) intent.getParcelableExtra("dataStaff");

            IDStaff = dataStaff.getId();
            etNama.setText(dataStaff.getNama());
            etJabatan.setText(dataStaff.getJabatan());
            spTipe.setSelection(getPositionByValue(dataStaff.getTipe()));
            etGaji.setText(String.valueOf(dataStaff.getGaji()));
            etTahunBergabung.setEnabled(false);
            etTahunBergabung.setText(String.valueOf(dataStaff.getTahunBergabung()));
            pgs.setVisibility(View.GONE);
        }

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeMaintain = "update";
                pgs.setVisibility(View.VISIBLE);

                // Pemeriksaan field kosong saat update
                if (etNama.getText().toString().isEmpty() ||
                        etJabatan.getText().toString().isEmpty() ||
                        etGaji.getText().toString().isEmpty() ||
                        etTahunBergabung.getText().toString().isEmpty()) {

                    Toast.makeText(UpdateOrDeleteStaffActivity.this, "Semua input harus diisi",
                            Toast.LENGTH_SHORT).show();
                    pgs.setVisibility(View.GONE);
                    return;  // Berhenti eksekusi jika ada field yang kosong
                }

                ExeUpdateOrDelete();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeMaintain = "delete";

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateOrDeleteStaffActivity.this);

                builder.setTitle("Hapus Staff")
                        .setMessage("Anda yakin ingin menghapus data ini ?")
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // perintah jika ditekan tombol Ya
                                pgs.setVisibility(View.VISIBLE);
                                ExeUpdateOrDelete();
                            }
                        }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pgs.setVisibility(View.GONE);
                            }
                        });
                builder.create().show();
            }
        });
    }

    // Untuk mendapatkan indeks item yg sesuai dgn params
    private int getPositionByValue(String item) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spTipe.getAdapter();

        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(item)) {
                    return i;
                }
            }
        }

        return 0; // Default: Pilih item pertama jika tidak ditemukan
    }

    private StringRequest createRequestVolley() {
        String myURLs = "";
        switch (ModeMaintain) {
            case "update" :
                myMessage = "Update Data";
                myURLs = URLs.URL_UPDATE_DATA_STAFF;
                break;
            case "delete" :
                myMessage = "Delete Data";
                myURLs = URLs.URL_DELETE_DATA_STAFF;
                break;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURLs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgs.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(UpdateOrDeleteStaffActivity.this, myMessage + " " +
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(UpdateOrDeleteStaffActivity.this, ViewAllStaffActivity.class);
                            startActivity(intent);
                            finish(); // Menutup UpdateOrDeleteStaffActivity agar tidak kembali lagi saat tombol "Back" ditekan

                            clearActivity();
                        } catch (Exception e) {
                            Toast.makeText(UpdateOrDeleteStaffActivity.this,e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateOrDeleteStaffActivity.this,error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("IDStaff", String.valueOf(IDStaff));
                params.put("Nama", etNama.getText().toString());
                params.put("Jabatan", etJabatan.getText().toString());
                params.put("Tipe", spTipe.getSelectedItem().toString());
                params.put("Gaji", etGaji.getText().toString());
                params.put("TahunBergabung", etTahunBergabung.getText().toString());

                return params;
            }
        };
        return stringRequest;
    }

    private void clearActivity() {
        etNama.setText("");
        etJabatan.setText("");
        etGaji.setText("");
        etTahunBergabung.setText("");
    }

    private void ExeUpdateOrDelete() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = createRequestVolley();
        mRequestQueue.add(stringRequest);
    }
}