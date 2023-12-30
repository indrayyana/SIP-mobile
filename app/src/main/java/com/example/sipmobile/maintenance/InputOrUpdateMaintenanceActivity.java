package com.example.sipmobile.maintenance;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.example.sipmobile.R;
import com.example.sipmobile.URLs;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InputOrUpdateMaintenanceActivity extends AppCompatActivity {

    Button btSave, btUpdate;
    ImageView imvMaintenance;
    String myMessage, tempFile, ModeMaintain;
    RequestQueue mRequestQueue, mRequestQueueImage;
    ProgressBar pgs;
    EditText etKode, etNama, etTanggal, etVendor, etStaffPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_or_update_maintenance);

        etKode = (EditText) findViewById(R.id.editTextKodeInputOrUpdateMaintenance);
        etNama = (EditText) findViewById(R.id.editTextNamaInputOrUpdateMaintenance);
        etTanggal = (EditText) findViewById(R.id.editTextTglMaintenance);
        etVendor = (EditText) findViewById(R.id.editTextVendorMaintenance);
        etStaffPIC = (EditText) findViewById(R.id.editTextStaffPIC);
        pgs = (ProgressBar) findViewById(R.id.progressBarInputOrUpdateMaintenance);
        imvMaintenance = (ImageView) findViewById(R.id.imageViewInputOrUpdateMaintenance);
        btSave = (Button) findViewById(R.id.buttonSaveMaintenance);
        btUpdate = (Button) findViewById(R.id.buttonUpdateMaintenance);

        pgs.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataMaintenance")) {
            Maintenance dataMaintenance = (Maintenance) intent.getParcelableExtra("dataMaintenance");

            etKode.setEnabled(false);
            etKode.setText(dataMaintenance.getKode());
            etNama.setEnabled(false);
            etNama.setText(dataMaintenance.getNamaInventaris());

            // Cek apakah tgl, vendor, staffPIC maintenance null atau tidak
            if (!"null".equals(dataMaintenance.getTanggalMaintenance()) &&
                    !"null".equals(dataMaintenance.getVendorMaintenance()) &&
                    !"null".equals(dataMaintenance.getStaffPIC())) {
                etTanggal.setText(dataMaintenance.getTanggalMaintenance());
                etVendor.setText(dataMaintenance.getVendorMaintenance());
                etStaffPIC.setText(dataMaintenance.getStaffPIC());

                btSave.setEnabled(false);
                btSave.setBackgroundColor(getResources().getColor(R.color.grey));
                btSave.setTextColor(getResources().getColor(android.R.color.darker_gray));
            } else {
                etTanggal.setText("");
                etVendor.setText("");
                etStaffPIC.setText("");

                btUpdate.setEnabled(false);
                btUpdate.setBackgroundColor(getResources().getColor(R.color.grey));
                btUpdate.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }

            mRequestQueueImage = Volley.newRequestQueue(InputOrUpdateMaintenanceActivity.this);

            ImageRequest imageRequest = new ImageRequest(URLs.URL_LOADIMAGE + dataMaintenance.getFotoInventaris(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imvMaintenance.setImageBitmap(response);
                            tempFile = dataMaintenance.getFotoInventaris();
                            pgs.setVisibility(View.GONE);
                        }
                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pgs.setVisibility(View.GONE);
                            Toast.makeText(InputOrUpdateMaintenanceActivity.this,
                                    error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            mRequestQueueImage.add(imageRequest);
        }

        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        etTanggal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    openDialog();
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeMaintain = "insert";
                pgs.setVisibility(View.VISIBLE);
                ExeInputOrUpdate();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModeMaintain = "update";
                pgs.setVisibility(View.VISIBLE);
                ExeInputOrUpdate();
            }
        });
    }

    private void openDialog() {
        // Mendapatkan tanggal hari ini
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = year + "-" + month + "-" + day;
                etTanggal.setText(date);
            }
        }, year, month, day);

        dialog.show();
    }

    private StringRequest createRequestVolley() {
        String myURLs = "";
        switch (ModeMaintain) {
            case "insert" :
                myMessage = "Insert Data";
                myURLs = URLs.URL_INSERT_DATA_MAINTENANCE;
                break;
            case "update" :
                myMessage = "Update Data";
                myURLs = URLs.URL_UPDATE_DATA_MAINTENANCE;
                break;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myURLs,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pgs.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(InputOrUpdateMaintenanceActivity.this, myMessage + " " +
                                    jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(InputOrUpdateMaintenanceActivity.this, ViewAllMaintenanceActivity.class);
                            startActivity(intent);
                            finish();

                            clearActivity();
                        } catch (Exception e) {
                            Toast.makeText(InputOrUpdateMaintenanceActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InputOrUpdateMaintenanceActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("IDMt", etKode.getText().toString());
                params.put("TanggalMaintenance", etTanggal.getText().toString());
                params.put("VendorMaintenance", etVendor.getText().toString());
                params.put("StaffPIC", etStaffPIC.getText().toString());

                return params;
            }
        };
        return stringRequest;
    }

    private void clearActivity() {
        etKode.setText("");
        etNama.setText("");
        etTanggal.setText("");
        etVendor.setText("");
        etStaffPIC.setText("");
        imvMaintenance.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    private void ExeInputOrUpdate() {
        mRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest=createRequestVolley();

        // Pemeriksaan field kosong
        if (etTanggal.getText().toString().isEmpty() ||
                etVendor.getText().toString().isEmpty() ||
                etStaffPIC.getText().toString().isEmpty()) {

            Toast.makeText(this, "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            pgs.setVisibility(View.GONE);
            return;  // Berhenti eksekusi jika ada field yang kosong
        }

        mRequestQueue.add(stringRequest);
    }
}