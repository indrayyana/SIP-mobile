package com.example.sipmobile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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

public class UpdateOrDeleteStaffActivity extends AppCompatActivity {

    int IDStaff;
    Button btUpdate, btDelete;
    String ModeMaintain, myMessage;
    RequestQueue mRequestQueue;
    ProgressBar pgs;
    EditText etNama, etJabatan, etTipe, etGaji, etTahunBergabung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delete_staff);

        etNama = (EditText) findViewById(R.id.editTextNamaStaffUpdateDelete);
        etJabatan = (EditText) findViewById(R.id.editTextJabatanStaffUpdateDelete);
        etTipe = (EditText) findViewById(R.id.editTextTipeStaffUpdateDelete);
        etGaji = (EditText) findViewById(R.id.editTextGajiStaffUpdateDelete);
        etTahunBergabung = (EditText) findViewById(R.id.editTextTahunBergabungUpdateDelete);
        pgs = (ProgressBar) findViewById(R.id.progressBarStaffUpdateDelete);
        btUpdate = (Button) findViewById(R.id.buttonUpdateStaff);
        btDelete = (Button) findViewById(R.id.buttonDeleteStaff);

        pgs.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataStaff")) {
            Staff dataStaff = (Staff) intent.getParcelableExtra("dataStaff");

            IDStaff = dataStaff.getId();
            etNama.setText(dataStaff.getNama());
            etJabatan.setText(dataStaff.getJabatan());
            etTipe.setText(dataStaff.getTipe());
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

    private void ExeUpdateOrDelete() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = createRequestVolley();

        mRequestQueue.add(stringRequest);
    }
}