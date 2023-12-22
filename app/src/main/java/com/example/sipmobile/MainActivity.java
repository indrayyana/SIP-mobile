package com.example.sipmobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SplashScreen splashScreen;
    Button btInv, btMt, btStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashScreen.setKeepOnScreenCondition(new SplashScreen.KeepOnScreenCondition() {
            @Override
            public boolean shouldKeepOnScreen() {
                return false;
            }
        });

        btInv = (Button) findViewById(R.id.buttonInventaris);
        btMt = (Button) findViewById(R.id.buttonMaintenance);
        btStaff = (Button) findViewById(R.id.buttonStaff);

        showPermission();

        btInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewAllInventoryActivity.class);
                startActivity(intent);
            }
        });

        btMt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewAllMaintenanceActivity.class);
                startActivity(intent);
            }
        });

        btStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewAllStaffActivity.class);
                startActivity(intent);
            }
        });
    }

        public void showPermission() {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.INTERNET};

            ArrayList<String> permissionList = new ArrayList<String>();
            boolean cekAll = true;

            for (int i = 0; i < permissions.length; i++) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                    cekAll = false;
                }
            }

            if (cekAll == false) {
                String[] stringArray = new String[permissionList.size()];
                permissionList.toArray(stringArray);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionList.get(0))) {
                    showExplanation("Permission Needed", "Application need permission please",
                            stringArray, 1001);
                } else {
                    // muncul pertama kali saat aplikasi dijalankan
                    requestPermission(stringArray, 1001);
                }
            }
        }

        // code untuk memperlihatkan permission jika user sudah melakukan pemilihan don't allow
        private void showExplanation(String title, String message, String[] permission, final int permissionCode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // perintah jika ditekan tombok ok
                            requestPermission(permission, permissionCode);
                        }
                    });
            builder.create().show();
        }

        // Method melakukan permission dengan kode yg telah ditentukan pada saat pemanggilan
        private void requestPermission(String[] permissionName, int permissionRequestCode) {
            ActivityCompat.requestPermissions(this, permissionName, permissionRequestCode);
        }

}