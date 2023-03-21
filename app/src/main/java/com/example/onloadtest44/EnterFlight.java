package com.example.onloadtest44;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.

public class EnterFlight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_flight);

        EditText enterTail = findViewById(R.id.enterTail);
        Button enterFlight = findViewById(R.id.enterFlight);

        enterFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tailNum = Integer.parseInt(String.valueOf(enterTail.getText()));


                RequestQueue queue = Volley.newRequestQueue(EnterFlight.this);
                String Url = "http://" + MainActivity.IP + ":3000/Route/";

                JsonArrayRequest checkTail = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        boolean found = false;

                        Bundle extras = getIntent().getExtras();
                        int EmployeeID = extras.getInt("EmployeeID");
                        String EmployeeName = extras.getString("EmployeeName");

                        for (int z = 0; z < response.length(); z++)
                        {
                            try {
                                JSONObject object = response.getJSONObject(z);

                                int jsonFlightNum = object.getInt("id");
                                int jsonTailNum = object.getInt("Tail");
                                String origin = object.getString("Origin");
                                String dest = object.getString("Destination");
                                String equip = object.getString("Equipment");
                                String date = object.getString("Date");

                                if (tailNum == jsonFlightNum) {
                                    Intent intent = new Intent(view.getContext(), SelectFlight.class);
                                    intent.putExtra("key1", jsonFlightNum);
                                    intent.putExtra("key2", jsonTailNum);
                                    intent.putExtra("key3", origin);
                                    intent.putExtra("key4", dest);
                                    intent.putExtra("key5", equip);
                                    intent.putExtra("key6", date);
                                    intent.putExtra("EmployeeID", EmployeeID);
                                    intent.putExtra("EmployeeName", EmployeeName);
                                    startActivity(intent);
                                    found = true;
                                    break;
                                }
                                else if (tailNum == jsonTailNum) {
                                    Intent intent = new Intent(view.getContext(), SelectFlight.class);
                                    intent.putExtra("key1", jsonFlightNum);
                                    intent.putExtra("key2", jsonTailNum);
                                    intent.putExtra("key3", origin);
                                    intent.putExtra("key4", dest);
                                    intent.putExtra("key5", equip);
                                    intent.putExtra("key6", date);
                                    intent.putExtra("EmployeeID", EmployeeID);
                                    intent.putExtra("EmployeeName", EmployeeName);
                                    startActivity(intent);
                                    found = true;
                                    break;
                                }
                            }

                            catch (Exception V)
                            {
                                Log.d("Volley Error", "Getting Flight Error");
                            }
                        }

                        if (!found) {
                            Log.d("Flight", "Flight not found");
                            enterTail.setError("Flight Not Found");
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MainActivity", "Volley Error");
                    }
                });

                queue.add(checkTail);
            }
        });

    }
}