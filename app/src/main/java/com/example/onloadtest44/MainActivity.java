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


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText enterEmployee = findViewById(R.id.enterTail);
        EditText enterLocation = findViewById(R.id.enterLoc);

        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    int employeeID = Integer.parseInt(String.valueOf(enterEmployee.getText()));
                    String location = String.valueOf(enterLocation.getText());

                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    String Url = "http://192.168.0.109:3000/Employees/";

                    JsonArrayRequest checkEmployee = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            boolean found = false;

                            for (int z = 0; z < response.length(); z++) {
                                try {
                                    JSONObject object = response.getJSONObject(z);

                                    int jsonEmployee = object.getInt("id");
                                    String jsonEmployeeName = object.getString("Name");
                                    String jsonLocation = object.getString("Location");
                                    Log.d("jsonEmployee", String.valueOf(jsonEmployee));

                                    if (employeeID == jsonEmployee)
                                    {
                                        if (jsonLocation.equals(location)) {
                                            Intent intent = new Intent(view.getContext(), EnterFlight.class);
                                            intent.putExtra("EmployeeID", jsonEmployee);
                                            intent.putExtra("EmployeeName", jsonEmployeeName);
                                            startActivity(intent);
                                            found = true;
                                            break;
                                        }
                                    }
                                } catch (Exception V) {
                                    Log.d("Volley Error", "Getting Employee Error");
                                }
                            }

                            if (!found) {
                                Log.d("Login", "Employee not found");
                                enterEmployee.setError("Employee Not Found");
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("MainActivity", "Volley Error");
                            enterEmployee.setError("Error Getting Data");
                        }
                    });

                    queue.add(checkEmployee);

                }

                catch (Exception R)
                {

                }
            }
        });

    }


}