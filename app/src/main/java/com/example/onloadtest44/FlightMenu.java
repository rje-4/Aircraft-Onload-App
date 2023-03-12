package com.example.onloadtest44;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.

public class FlightMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_menu);

        Bundle extras = getIntent().getExtras();
        int EmployeeID = extras.getInt("EmployeeID");
        String EmployeeName = extras.getString("EmployeeName");
        int flightNum = extras.getInt("flightNum");
        String origin = extras.getString("flightOrigin");
        String dest = extras.getString("flightDest");

        getSupportActionBar().setTitle("Flight: FDX" + flightNum + " " + origin + " -> " + dest);

        CardView uldCheck = findViewById(R.id.uldCheck);
        CardView posCheck = findViewById(R.id.posCheck);

        uldCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(FlightMenu.this, "",
                        "Loading. Please wait...", true);
                Intent intent = new Intent(view.getContext(), ULDCheck.class);
                intent.putExtra("EmployeeID", EmployeeID);
                intent.putExtra("EmployeeName", EmployeeName);
                startActivity(intent);
                dialog.cancel();
            }
        });

        posCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(FlightMenu.this, "",
                        "Loading. Please wait...", true);
                Intent intent = new Intent(view.getContext(), PosVerify.class);
                intent.putExtra("EmployeeID", EmployeeID);
                intent.putExtra("EmployeeName", EmployeeName);
                startActivity(intent);
                dialog.cancel();
            }
        });

    }
}