package com.example.sipmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btInv, btMt, btStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btInv = (Button) findViewById(R.id.buttonInventaris);
        btMt = (Button) findViewById(R.id.buttonMaintenance);
        btStaff = (Button) findViewById(R.id.buttonStaff);

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
}