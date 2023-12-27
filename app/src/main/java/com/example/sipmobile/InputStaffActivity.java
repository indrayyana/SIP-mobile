package com.example.sipmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class InputStaffActivity extends AppCompatActivity {

    Button btSave;
    String myMessage;
    RequestQueue mRequestQueue;
    ProgressBar pgs;
    EditText etNama, etJabatan, etTipe, etGaji, etTahunBergabung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_staff);

        etNama = (EditText) findViewById(R.id.editTextNamaStaffInput);
        etJabatan = (EditText) findViewById(R.id.editTextJabatanStaffInput);
        etTipe = (EditText) findViewById(R.id.editTextTipeStaffInput);
        etGaji = (EditText) findViewById(R.id.editTextGajiStaffInput);
        etTahunBergabung = (EditText) findViewById(R.id.editTextTahunBergabungInput);
        pgs = (ProgressBar) findViewById(R.id.progressBarStaffInput);
        btSave = (Button) findViewById(R.id.buttonSaveInputStaff);

        pgs.setVisibility(View.GONE);

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
                params.put("Tipe", etTipe.getText().toString());
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
        etTipe.setText("");
        etGaji.setText("");
        etTahunBergabung.setText("");
    }

    private void ExeInput() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = createRequestVolley();

        // Pemeriksaan field kosong
        if (etNama.getText().toString().isEmpty() ||
                etJabatan.getText().toString().isEmpty() ||
                etTipe.getText().toString().isEmpty() ||
                etGaji.getText().toString().isEmpty() ||
                etTahunBergabung.getText().toString().isEmpty()) {

            Toast.makeText(this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            pgs.setVisibility(View.GONE);
            return;  // Berhenti eksekusi jika ada field yang kosong
        }

        mRequestQueue.add(stringRequest);
    }
}