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
import android.graphics.Bitmap;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateOrDeleteInventarisActivity extends AppCompatActivity {

    ImageView imvUpdateDelete;
    Button btUpdate, btDelete, btPilih;
    Inventaris inventaris;
    boolean gantiImage;
    String pathImage, ModeMaintain, tempFile, myMessage;
    RequestQueue mRequestQueue, mRequestQueueImage;
    ProgressBar pgs;
    EditText etKode, etNama, etJumlah, etKategori, etTipe, etHarga, etTahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_or_delete_inventaris);

        imvUpdateDelete = (ImageView) findViewById(R.id.imageViewUpdateDelete);
        etKode = (EditText) findViewById(R.id.editTextKodeUpdateDelete);
        etNama = (EditText) findViewById(R.id.editTextNamaUpdateDelete);
        etJumlah = (EditText) findViewById(R.id.editTextJumlahUpdateDelete);
        etKategori = (EditText) findViewById(R.id.editTextKategoriUpdateDelete);
        etTipe = (EditText) findViewById(R.id.editTextTipeUpdateDelete);
        etHarga = (EditText) findViewById(R.id.editTextHargaUpdateDelete);
        etTahun = (EditText) findViewById(R.id.editTextTahunUpdateDelete);
        pgs = (ProgressBar) findViewById(R.id.progressBarUpdateDelete);
        btPilih = (Button) findViewById(R.id.buttonPilihGambarUpdateDelete);
        btUpdate = (Button) findViewById(R.id.buttonUpdate);
        btDelete = (Button) findViewById(R.id.buttonDelete);

        pgs.setVisibility(View.VISIBLE);
        imvUpdateDelete.setImageResource(R.drawable.ic_launcher_background);
        gantiImage = false;

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataInventaris")) {
            Inventaris dataInventaris = (Inventaris) intent.getParcelableExtra("dataInventaris");

            pgs.setVisibility(View.GONE);
            etKode.setEnabled(false);
            etKode.setText(dataInventaris.getKode());
            etNama.setText(dataInventaris.getNama());
            etJumlah.setText(String.valueOf(dataInventaris.getJumlah()));
            etKategori.setText(dataInventaris.getKategori());
            etTipe.setText(dataInventaris.getTipe());
            etHarga.setText(String.valueOf(dataInventaris.getHargaBeli()));
            etTahun.setText(String.valueOf(dataInventaris.getTahunBeli()));

            mRequestQueueImage = Volley.newRequestQueue(UpdateOrDeleteInventarisActivity.this);

            ImageRequest imageRequest = new ImageRequest(URLs.URL_LOADIMAGE + dataInventaris.getPath(),
                    new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    imvUpdateDelete.setImageBitmap(response);
                    tempFile = dataInventaris.getPath();
                }
            }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pgs.setVisibility(View.GONE);
                            Toast.makeText(UpdateOrDeleteInventarisActivity.this,
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            mRequestQueueImage.add(imageRequest);

        }

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
                gantiImage = false;
                pgs.setVisibility(View.VISIBLE);
                ExeUpdateOrDelete();
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
                            imvUpdateDelete.setImageURI(uri);
                            pathImage = getRealPathFromURI(getBaseContext(),uri);
                        } else {
                            Toast.makeText(UpdateOrDeleteInventarisActivity.this, "Pilih Foto Dibatalkan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    private StringRequest createRequestVolley() {
        String myURLs = "";
        switch (ModeMaintain) {
            case "update" :
                myMessage = "Update Data";
                myURLs = URLs.URL_UPDATE_DATA;
                break;
            case "delete" :
                myMessage = "Delete Data";
                myURLs = URLs.URL_DELETE_DATA;
                break;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURLs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgs.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(UpdateOrDeleteInventarisActivity.this, myMessage + " " +
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            clearActivity();
                        } catch (Exception e) {
                            Toast.makeText(UpdateOrDeleteInventarisActivity.this,e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateOrDeleteInventarisActivity.this,error.getMessage(),
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

                switch (ModeMaintain) {
                    case "update" :
                        if (gantiImage) {
                            File file2 = new File(pathImage);
                            String namaFile2 = file2.getName();
                            params.put("Foto1", tempFile);
                            params.put("Foto2", namaFile2);
                        } else {
                            params.put("Foto1", tempFile);
                            params.put("Foto2", tempFile);
                        }
                        break;
                    case "delete" :
                        params.put("Foto1",tempFile);
                        params.put("Foto2","Kosong");
                        break;
                }
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
        imvUpdateDelete.setImageResource(R.drawable.ic_launcher_background);
        gantiImage = false;
    }

    private void ExeUpdateOrDelete() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest=createRequestVolley();

        if (gantiImage) {
            File file = new File(pathImage);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part FiletoUpload = MultipartBody.Part.createFormData("file", file.getName(),requestBody);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<ServerResponse> call = getResponse.uploadFile(FiletoUpload,filename);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                    ServerResponse serverResponse = response.body();
                    Toast.makeText(UpdateOrDeleteInventarisActivity.this, "Masuk",
                            Toast.LENGTH_LONG).show();

                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {
                            mRequestQueue.add(stringRequest);
                        } else {
                            Toast.makeText(UpdateOrDeleteInventarisActivity.this, serverResponse.getMessage(),
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

    // TODO: Add Fitur Search
//    private void searchData(String key) {
//        mRequestQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                URLs.URL_LOAD_DATA, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    if (!obj.getBoolean("error")){
//                        JSONArray dataInventarisArray = obj.getJSONArray("data");
//                        for (int i = 0; i < dataInventarisArray.length(); i++){
//                            JSONObject dtobjInventaris = dataInventarisArray.getJSONObject(i);
//                            inventaris = new Inventaris(
//                                    dtobjInventaris.getString("Kode"),
//                                    dtobjInventaris.getString("Nama"),
//                                    dtobjInventaris.getString("Kategori"),
//                                    dtobjInventaris.getString("Tipe"),
//                                    dtobjInventaris.getString("Foto"),
//                                    dtobjInventaris.getInt("Jumlah"),
//                                    dtobjInventaris.getInt("HargaBeli"),
//                                    dtobjInventaris.getInt("TahunBeli")
//                            );
//                        }
//                        viewData();
//                    }else{
//                        // tampilkan data jika terdapat error
//                        Toast.makeText(UpdateOrDeleteInventarisActivity.this, obj.getString(
//                                        "message"),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e){
//                    Toast.makeText(UpdateOrDeleteInventarisActivity.this,
//                            e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(UpdateOrDeleteInventarisActivity.this,
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("IDInv", key);
//                return params;
//            }
//        };
//        mRequestQueue.add(stringRequest);
//    }

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