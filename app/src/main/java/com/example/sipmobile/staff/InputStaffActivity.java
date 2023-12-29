package com.example.sipmobile.staff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class InputStaffActivity extends AppCompatActivity {

    Button btSave;
    String myMessage;
    RequestQueue mRequestQueue;
    ProgressBar pgs;
    Spinner spTipe;
    EditText etNama, etJabatan, etGaji, etTahunBergabung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_staff);

        etNama = (EditText) findViewById(R.id.editTextNamaStaffInput);
        etJabatan = (EditText) findViewById(R.id.editTextJabatanStaffInput);
        spTipe = (Spinner) findViewById(R.id.spinnerTipeStaffInput);
        etGaji = (EditText) findViewById(R.id.editTextGajiStaffInput);
        etTahunBergabung = (EditText) findViewById(R.id.editTextTahunBergabungInput);
        pgs = (ProgressBar) findViewById(R.id.progressBarStaffInput);
        btSave = (Button) findViewById(R.id.buttonSaveInputStaff);

        pgs.setVisibility(View.GONE);

        // Custom warna text item spinner
        String[] itemTipeStaff = getResources().getStringArray(R.array.staff);
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(itemTipeStaff));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.style_text_spinner, arrayList);
        spTipe.setAdapter(arrayAdapter);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgs.setVisibility(View.VISIBLE);
                ExeInput();
            }
        });
    }

    private StringRequest createRequestVolley() {
        myMessage = "Insert Data";
        String myURL = URLs.URL_INSERT_DATA_STAFF;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgs.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(InputStaffActivity.this, myMessage + ": " +
                                    jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(InputStaffActivity.this, ViewAllStaffActivity.class);
                            startActivity(intent);
                            finish();

                            clearActivity();
                        } catch (Exception e) {
                            Toast.makeText(InputStaffActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InputStaffActivity.this, error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
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

    private void ExeInput() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = createRequestVolley();

        // Pemeriksaan field kosong
        if (etNama.getText().toString().isEmpty() ||
                etJabatan.getText().toString().isEmpty() ||
                etGaji.getText().toString().isEmpty() ||
                etTahunBergabung.getText().toString().isEmpty()) {

            Toast.makeText(this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            pgs.setVisibility(View.GONE);
            return;  // Berhenti eksekusi jika ada field yang kosong
        }

        mRequestQueue.add(stringRequest);
    }
}