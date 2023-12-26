package com.example.sipmobile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class InputInventarisActivity extends AppCompatActivity {

    ImageView imvInput;
    Button btSave, btPilih;
    Inventaris inventaris;
    boolean gantiImage;
    String pathImage, myMessage;
    RequestQueue mRequestQueue;
    ProgressBar pgs;
    EditText etKode, etNama, etJumlah, etKategori, etTipe, etHarga, etTahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_inventaris);

        imvInput = (ImageView) findViewById(R.id.imageViewInput);
        etKode = (EditText) findViewById(R.id.editTextKodeInput);
        etNama = (EditText) findViewById(R.id.editTextNamaInput);
        etJumlah = (EditText) findViewById(R.id.editTextJumlahInput);
        etKategori = (EditText) findViewById(R.id.editTextKategoriInput);
        etTipe = (EditText) findViewById(R.id.editTextTipeInput);
        etHarga = (EditText) findViewById(R.id.editTextHargaInput);
        etTahun = (EditText) findViewById(R.id.editTextTahunInput);
        pgs = (ProgressBar) findViewById(R.id.progressBarInput);
        btPilih = (Button) findViewById(R.id.buttonPilihGambarInput);
        btSave = (Button) findViewById(R.id.buttonSaveInput);

        pgs.setVisibility(View.GONE);
        imvInput.setImageResource(R.drawable.ic_launcher_background);
        gantiImage = false;

        btPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gantiImage = true;
                String path = Environment.getExternalStorageDirectory() + "/" + "Pictures" + "/";
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(uri, "image/*");

                activityResultLauncher.launch(intent);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgs.setVisibility(View.VISIBLE);
                ExeInput();
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == RESULT_OK) {
                        Uri uri = o.getData().getData();
                        if (uri != null) {
                            imvInput.setImageURI(uri);
                            pathImage = getRealPathFromURI(getBaseContext(),uri);
                        } else {
                            Toast.makeText(InputInventarisActivity.this, "Pilih Foto Dibatalkan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private StringRequest createRequestVolley() {
        myMessage = "Insert Data";
        String myURL = URLs.URL_INSERT_DATA_INVENTARIS;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgs.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(InputInventarisActivity.this, myMessage + ": " +
                                    jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            clearActivity();
                        } catch (Exception e) {
                            Toast.makeText(InputInventarisActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
        new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InputInventarisActivity.this,error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("IDInv", etKode.getText().toString());
                params.put("Nama", etNama.getText().toString());
                params.put("Jumlah", etJumlah.getText().toString());
                params.put("Kategori", etKategori.getText().toString());
                params.put("Tipe", etTipe.getText().toString());
                params.put("HargaBeli", etHarga.getText().toString());
                params.put("TahunBeli", etTahun.getText().toString());

                File file = new File(pathImage);
                String namaFile = file.getName();
                params.put("Foto1", "Kosong");
                params.put("Foto2", namaFile);

                return params;
            }
        };
        return stringRequest;
    }

    private void clearActivity() {
        etKode.setText("");
        etNama.setText("");
        etJumlah.setText("");
        etKategori.setText("");
        etTipe.setText("");
        etHarga.setText("");
        etTahun.setText("");
        imvInput.setImageResource(R.drawable.ic_launcher_background);
        gantiImage = false;
    }

    private void ExeInput() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = createRequestVolley();

        // Pemeriksaan field kosong
        if (etKode.getText().toString().isEmpty() ||
                etNama.getText().toString().isEmpty() ||
                etJumlah.getText().toString().isEmpty() ||
                etKategori.getText().toString().isEmpty() ||
                etTipe.getText().toString().isEmpty() ||
                etHarga.getText().toString().isEmpty() ||
                etTahun.getText().toString().isEmpty() ||
                pathImage == null || pathImage.isEmpty()) {

            Toast.makeText(this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            pgs.setVisibility(View.GONE);
            return;  // Berhenti eksekusi jika ada field yang kosong
        }

        if (gantiImage) {
            File file = new File(pathImage);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part FiletoUpload = MultipartBody.Part.createFormData("file", file.getName(),requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.uploadFile(FiletoUpload, filename);

            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                    ServerResponse serverResponse = response.body();

                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {
                            mRequestQueue.add(stringRequest);

                            Intent intent = new Intent(InputInventarisActivity.this, ViewAllInventoryActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(InputInventarisActivity.this, serverResponse.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e("Response : ", serverResponse.toString());
                    }

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Log.e("Response : ", t.getMessage());
                }
            });
        } else {
            mRequestQueue.add(stringRequest);
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] data_media_uri = {MediaStore.Images.Media.DATA};

            cursor = context.getContentResolver().query(contentUri, data_media_uri, null,
                    null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}