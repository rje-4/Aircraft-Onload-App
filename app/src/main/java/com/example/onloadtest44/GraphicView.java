package com.example.onloadtest44;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

//Written by Ryan Elias. Uploaded March 11, 2023. Based on FedEx Express AutoVerify Aircraft Onload Application.


public class GraphicView extends AppCompatActivity {

    final String PRA = "Dan Q. Marino (696969)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic_view);

        setInfo();


        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeContainer.setRefreshing(false);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem itemswitch = menu.findItem(R.id.switch_action_bar);
        itemswitch.setActionView(R.layout.use_switch);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        final Switch sw = (Switch) menu.findItem(R.id.switch_action_bar).getActionView().findViewById(R.id.switch2);

        sw.setChecked(true);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //Intent myIntent = new Intent(GraphicView.this, MainActivity.class);
                    //GraphicView.this.startActivity(myIntent);
                    sw.setChecked(false);
                    onBackPressed();

                }
            }
        });
        return true;
    }


    @SuppressLint("SetTextI18n")
    public void setInfo()
    {
        int positions = ULDCheck.positionAmount;

        Data d1 = ULDCheck.d1;
        String[] canNames = d1.getFinalNameArray();
        String[] canPositions = d1.getFinalPositionArray();
        String[] canWeights = d1.getFinalWeightArray();
        String[] canInspections = d1.getFinalInspectionArray();
        String[] canVerifications = d1.getFinalVerificationArray();

        for (int i = 0; i < positions; i++)
        {
            //Colors -----------------------
            int textColor = Color.GRAY;

            if (Objects.equals(canInspections[i], "true"))
            {
                textColor = Color.BLUE;
            }

            if (Objects.equals(canVerifications[i], "true"))
            {
                textColor = Color.parseColor("#178214");
            }

            //Verification Check -----------------
            String[] verifiedByString = {"N/A"};

            if (Objects.equals(canInspections[i], "true"))
            {
                RequestQueue queue = Volley.newRequestQueue(GraphicView.this);
                String Url = "http://" + MainActivity.IP + ":3000/Containers/" + i;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String inspector = response.getString("Inspector");
                            verifiedByString[0] = inspector;
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Get Inspector", "Volley Error");
                    }
                });

                queue.add(request);
            }


            String[] verifiedPosByString = {"N/A"};
            if (Objects.equals(canVerifications[i], "true"))
            {
                RequestQueue queue = Volley.newRequestQueue(GraphicView.this);
                String Url = "http://" + MainActivity.IP + ":3000/Containers/" + i;

                JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String verifier = response.getString("Verifier");
                            verifiedPosByString[0] = verifier;
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Get Verifier", "Volley Error");
                    }
                });

                queue.add(request2);
            }

            //Display --------------------------------------------

            String writePos = canPositions[i];

            char[] writePosChars = writePos.toCharArray();

            char[] temp = new char[4];
            int templength = writePosChars.length;
            for (int j = 0; j < templength; j++) {
                temp[j] = writePosChars[j];
            }
            writePosChars = temp;

            String posNumS;

            if (writePosChars[1] == 'D' || writePosChars[1] == 'C' || writePosChars[1] == 'L' || writePosChars[1] == 'R')
            {
                posNumS = String.valueOf(writePosChars[0]);
            }
            else
            {
                posNumS = String.valueOf(writePosChars[0]) + String.valueOf(writePosChars[1]);
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////

            try {

                if (writePosChars[1] == 'L' || writePosChars[2] == 'L') {
                    String searchValue = 'l' + posNumS;

                    @SuppressLint("DiscouragedApi")
                    int resID = getResources().getIdentifier(searchValue, "id", getPackageName());

                    TextView textBox = findViewById(resID);
                    textBox.setTextSize(12);
                    textBox.setTextColor(textColor);
                    textBox.setText(canNames[i] + "\n" + canPositions[i]);

                    int finalK = i;
                    int finalK1 = i;
                    textBox.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                            builder1.setMessage("Position Information" +
                                    "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                    + "\nPRA: " + PRA);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            return true;
                        }
                    });

                }

                else if (writePosChars[1] == 'C' || writePosChars[2] == 'C') {
                    if (writePos.equals("17C")) {
                        TextView p17C = findViewById(R.id.c17);
                        p17C.setTextSize(12);
                        p17C.setTextColor(textColor);
                        p17C.setText(canNames[i] + "\n" + canPositions[i]);
                        int finalK = i;
                        int finalK1 = i;
                        p17C.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                        + "\nPRA: " + PRA);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                return true;
                            }
                        });
                    }

                    if (writePos.equals("1C")) {
                        TextView p1C = findViewById(R.id.c1);
                        p1C.setTextSize(12);
                        p1C.setTextColor(textColor);
                        p1C.setText(canNames[i] + "\n" + canPositions[i]);
                        int finalK = i;
                        int finalK1 = i;
                        p1C.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                        + "\nPRA: " + PRA);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                return true;
                            }
                        });
                    }
                }



                else if (writePosChars[1] == 'D' || writePosChars[2] == 'D') {
                    if (writePos.equals("1D")) {
                        TextView p1D = findViewById(R.id.c1);
                        p1D.setTextSize(12);
                        p1D.setTextColor(textColor);
                        p1D.setText(canNames[i] + "\n" + canPositions[i]);
                        int finalK = i;
                        int finalK1 = i;
                        p1D.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                        + "\nPRA: " + PRA);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                return true;
                            }
                        });
                    }
                    else {
                        String posNumFake;

                        switch (Integer.parseInt(posNumS)) {
                            case 16:
                                posNumFake = "12";
                                break;
                            case 15:
                                posNumFake = "11";
                                break;
                            case 14:
                                posNumFake = "11";
                                break;
                            case 13:
                                posNumFake = "10";
                                break;
                            case 12:
                                posNumFake = "9";
                                break;
                            case 11:
                                posNumFake = "8";
                                break;
                            case 10:
                                posNumFake = "7";
                                break;
                            case 9:
                                posNumFake = "7";
                                break;
                            case 8:
                                posNumFake = "6";
                                break;
                            case 7:
                                posNumFake = "5";
                                break;
                            case 6:
                                posNumFake = "4";
                                break;
                            case 5:
                                posNumFake = "4";
                                break;
                            case 4:
                                posNumFake = "3";
                                break;
                            case 3:
                                posNumFake = "3";
                                break;
                            case 2:
                                posNumFake = "2";
                                break;
                            case 1:
                                posNumFake = "1";
                                break;
                            default:
                                posNumFake = "Error";

                        }

                        String searchValue = "r" + posNumFake;

                        @SuppressLint("DiscouragedApi")
                        int resID = getResources().getIdentifier(searchValue, "id", getPackageName());

                        TextView textBox = findViewById(resID);
                        textBox.setTextSize(12);
                        textBox.setTextColor(textColor);
                        textBox.setText(String.valueOf(canNames[i]) + "\n" + String.valueOf(canPositions[i]));
                        int finalK = i;
                        int finalK1 = i;
                        textBox.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                        + "\nPRA: " + PRA);
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });


                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                                return true;
                            }
                        });
                    }
                } else {
                    String searchValue = 'r' + posNumS;

                    @SuppressLint("DiscouragedApi")
                    int resID = getResources().getIdentifier(searchValue, "id", getPackageName());

                    TextView textBox = findViewById(resID);
                    textBox.setTextSize(12);
                    textBox.setTextColor(textColor);
                    textBox.setText(String.valueOf(canNames[i]) + "\n" + String.valueOf(canPositions[i]));
                    int finalK = i;
                    int finalK1 = i;
                    textBox.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(GraphicView.this);
                            builder1.setMessage("Position Information" +
                                    "\n" + "\nPosition: " + canPositions[finalK] + "\nAsset Name: " + canNames[finalK] + "\nWeight: " + canWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
                                    + "\nPRA: " + PRA);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                            return true;
                        }
                    });
                }

            }

            catch (Exception D)
            {
                TextView textBox = findViewById(R.id.c1);
                textBox.setText("ERROR");
            }


        }

    }


}