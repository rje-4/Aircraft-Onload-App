package com.example.onloadtest44;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONObject;

public class SelectFlight extends AppCompatActivity {

    @SuppressLint({"ResourceType", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_flight);

        int jsonFlightNum = 0;
        int jsonTailNum = 0;
        String origin = "";
        String dest = "";
        String equip = "";
        String date = "";
        int EmployeeID = 0;
        String EmployeeName = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonFlightNum = extras.getInt("key1");
            jsonTailNum = extras.getInt("key2");
            origin = extras.getString("key3");
            dest = extras.getString("key4");
            equip = extras.getString("key5");
            date = extras.getString("key6");
            EmployeeID = extras.getInt("EmployeeID");
            EmployeeName = extras.getString("EmployeeName");
        }

        try
        {
            TextView flightInfo = findViewById(R.id.flightInfo);

            flightInfo.setText("Flight: FDX" + jsonFlightNum + " " + origin + " -> " + dest +
                    "\nAircraft: " + equip + " (N" + jsonTailNum + "FE)" +
                    "\nZ-Date: " + date);

        }

        catch (Exception N)
        {
            Log.d("Error Posting", "Error Posting");
        }

        CardView cv = findViewById(R.id.flight);

        int finalEmployeeID = EmployeeID;
        String finalEmployeeName = EmployeeName;
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FlightMenu.class);
                intent.putExtra("EmployeeID", finalEmployeeID);
                intent.putExtra("EmployeeName", finalEmployeeName);
                startActivity(intent);
            }
        });

    }
}