package com.example.onloadtest44;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.

public class FlightMenu extends AppCompatActivity {

    boolean savedAntiTipVerifyCheck = false;

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


                if (!savedAntiTipVerifyCheck) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FlightMenu.this);
                    builder.setTitle("Anti-Tip Tether Check");
                    builder.setMessage("Enter Employee Number of Second Nose Tether Inspector.");

                    EditText input = new EditText(FlightMenu.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.getBackground().setColorFilter(Color.parseColor("#6200EE"),
                            PorterDuff.Mode.SRC_ATOP);
                    builder.setView(input);

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface tethercheck, int which) {
                                    //EMPTY. OVERRIDE
                                }
                            });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface tethercheck, int which) {
                            tethercheck.cancel();
                        }
                    });

                    final AlertDialog tethercheck = builder.create();
                    tethercheck.show();

                    tethercheck.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int enteredEmployeeNum = Integer.parseInt(String.valueOf(input.getText()));

                            RequestQueue queue = Volley.newRequestQueue(FlightMenu.this);
                            String Url = "http://" + MainActivity.IP + ":3000/Employees/";

                            JsonArrayRequest checkEmployee = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    boolean found = false;

                                    for (int z = 0; z < response.length(); z++) {
                                        try {
                                            JSONObject object = response.getJSONObject(z);

                                            int secondEmployee = object.getInt("id");

                                            if (enteredEmployeeNum == secondEmployee && enteredEmployeeNum != EmployeeID) {
                                                Intent intent = new Intent(view.getContext(), PosVerify.class);
                                                intent.putExtra("EmployeeID", EmployeeID);
                                                intent.putExtra("EmployeeName", EmployeeName);
                                                startActivity(intent);
                                                found = true;
                                                savedAntiTipVerifyCheck = true;
                                                tethercheck.cancel();
                                                break;

                                            }
                                        } catch (Exception V) {
                                            Log.d("Volley Error", "Getting Employee Error");
                                        }
                                    }

                                    if (!found) {
                                        Log.d("Login", "Employee not found");
                                        input.setError("Employee Not Found or Not Allowed");
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("MainActivity", "Volley Error");
                                    input.setError("Employee Not Found or Not Allowed");
                                }
                            });

                            queue.add(checkEmployee);

                        }
                    });


                }

                else
                {
                    Intent intent = new Intent(view.getContext(), PosVerify.class);
                    intent.putExtra("EmployeeID", EmployeeID);
                    intent.putExtra("EmployeeName", EmployeeName);
                    startActivity(intent);
                }

                dialog.cancel();
            }
        });

    }
}