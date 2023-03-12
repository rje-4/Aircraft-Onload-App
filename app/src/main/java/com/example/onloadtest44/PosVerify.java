package com.example.onloadtest44;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class PosVerify extends AppCompatActivity {

    static int index = 0;
    static int positionAmount = 0;

    final String PRA = "Dan Q. Marino (696969)";

    int EmployeeID = 0;
    String EmployeeName = "N/A";

    Data d1 = ULDCheck.d1;

    PositionExists positionExists = new PositionExists();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_verify);

        getSupportActionBar().setTitle("Verify Position");

        Bundle extras = getIntent().getExtras();
        EmployeeID = extras.getInt("EmployeeID");
        EmployeeName = extras.getString("EmployeeName");

        TableLayout tl = findViewById(R.id.main_table2);

        Runnable task = new Runnable() {
            @SuppressLint("SetTextI18n")
            public void run() {
                addRows(tl);
            }
        };

        createView();

        Handler handler = new Handler();
        handler.postDelayed(task, 500);

        ////////////////////////////////////////////////////////////////////////////
        SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                index = 0;
                positionAmount = 0;

                Data d2 = new Data();
                Arrays.fill(d2.canNames, "");
                Arrays.fill(d2.canPositions, "");
                Arrays.fill(d2.canWeights, "");
                Arrays.fill(d2.canInspections, "");
                Arrays.fill(d2.canVerifications, "");
                d2.i = 0;
                d2.j = 0;
                d2.w = 0;
                d2.u = 0;
                d2.v = 0;

                d1 = new Data();

                Runnable task = new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        addRows(tl);
                    }
                };

                try {
                    Handler handler = new Handler();
                    handler.postDelayed(task, 500);

                    tl.removeAllViewsInLayout();

                    createView();

                }

                catch (Exception Z)
                {
                    Log.d("Refresh", "error");
                }

                finally
                {
                    swipeContainer.setRefreshing(false);
                }
            }
        });

        EditText enterCanText = (EditText) findViewById(R.id.enteruldtextverify);
        enterCanText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String enteredText = String.valueOf(enterCanText.getText());
                    //WIP!!!!
                    //verifyCanName(enteredText);
                    enterCanText.setText("");
                    return true;
                }
                return false;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        MenuItem itemswitch = menu.findItem(R.id.switch_action_bar);
        itemswitch.setActionView(R.layout.use_switch);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        final Switch sw = (Switch) menu.findItem(R.id.switch_action_bar).getActionView().findViewById(R.id.switch2);

        sw.setChecked(false);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sw.setChecked(false);
                    Intent myIntent = new Intent(PosVerify.this, GraphicView.class);
                    PosVerify.this.startActivity(myIntent);
                }
            }
        });
        return true;
    }

    @SuppressLint({"ResourceType", "SetTextI18n"})
    public void createView()
    {
        TableLayout tl = (TableLayout) findViewById(R.id.main_table2);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(999);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        getAssetInfo();

        addRows(tl);

    }


    public void getAssetInfo() {

        RequestQueue queue = Volley.newRequestQueue(PosVerify.this);
        String Url = "http://192.168.0.109:3000/Containers/";

        JsonArrayRequest requestCan = new JsonArrayRequest(Request.Method.GET, Url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                positionAmount = response.length();

                for (index = 0; index < response.length(); index++)
                {
                    try {
                        JSONObject currentCan = response.getJSONObject(index);

                        String jsonCanName = currentCan.getString("Asset");
                        String jsonCanPosition = currentCan.getString("Position");
                        String jsonCanWeight = currentCan.getString("Weight");
                        String jsonCanInspection = currentCan.getString("Inspected");
                        String jsonCanVerify = currentCan.getString("Verified");

                        d1.addToNames(jsonCanName);
                        d1.addToPositions(jsonCanPosition);
                        d1.addToWeights(jsonCanWeight);
                        d1.addToInspections(jsonCanInspection);
                        d1.addToVerifications(jsonCanVerify);

                        Log.d("ULD", jsonCanName + " " + jsonCanPosition);

                    }

                    catch (Exception e)
                    {
                        Log.d("PosVerify", "Object Array Error");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PosVerify", "Volley Error");
            }
        });
        queue.add(requestCan);
    }


    @SuppressLint("SetTextI18n")
    public void addRows(TableLayout tl)
    {
        String[] finalCanNames = d1.getFinalNameArray();
        String[] finalPositionNames = d1.getFinalPositionArray();
        String[] finalWeights = d1.getFinalWeightArray();
        String[] finalInspections = d1.getFinalInspectionArray();
        String[] finalVerifications = d1.getFinalVerificationArray();

        for (int k = 0; k < positionAmount; k++) {

            //Colors -----------------------
            int textColor = Color.GRAY;

            if (Objects.equals(finalInspections[k], "true"))
            {
                textColor = Color.BLUE;
            }

            //Verification Check -----------------
            String[] verifiedByString = {"N/A"};

            if (Objects.equals(finalInspections[k], "true"))
            {
                RequestQueue queue = Volley.newRequestQueue(PosVerify.this);
                String Url = "http://192.168.0.109:3000/Containers/" + k;

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
            if (Objects.equals(finalVerifications[k], "true"))
            {
                RequestQueue queue = Volley.newRequestQueue(PosVerify.this);
                String Url = "http://192.168.0.109:3000/Containers/" + k;

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

            //Positioning ---------------------------------

            char[] characters = (finalPositionNames[k] + " ").toCharArray();

            if (Objects.equals(finalPositionNames[k], "17C") || Objects.equals(finalPositionNames[k], "1C") || Objects.equals(finalPositionNames[k], "1D"))
            {
                TableRow tr = new TableRow(this);
                tr.setId(1000 + k);
                tr.setPadding(180,0,0,0);
                tr.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView label_middle = new TextView(this);
                label_middle.setId(2000 + k);
                label_middle.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                label_middle.setTextColor(textColor);
                label_middle.setPaintFlags(label_middle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                label_middle.setPadding(0, 50, 0, 50);
                label_middle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                label_middle.setTextSize(18);
                tr.addView(label_middle);

                if (k == 0)
                {
                    tr.setPadding(180,0,0,150);
                }

                tl.addView(tr, 0, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));


                int finalK = k;
                int finalK1 = k;
                label_middle.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                        builder1.setMessage("Position Information" +
                                "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

            else if (characters[1] == 'D' || characters[2] == 'D')
            {
                String posNum;

                if (characters [1] != 'D') {
                    posNum = String.valueOf(characters[0]) + String.valueOf(characters[1]);
                }

                else {
                    posNum = String.valueOf(characters[0]);
                }

                int posNum2 = Integer.parseInt(posNum);

                Log.d("case", String.valueOf(posNum2));

                String posNumFake;

                switch (posNum2) {
                    case 16:
                        posNumFake = "12R";
                        break;
                    case 15:
                        posNumFake = "11R";
                        break;
                    case 14:
                        posNumFake = "11R";
                        break;
                    case 13:
                        posNumFake = "10R";
                        break;
                    case 12:
                        posNumFake = "9R";
                        break;
                    case 11:
                        posNumFake = "8R";
                        break;
                    case 10:
                        posNumFake = "7R";
                        break;
                    case 9:
                        posNumFake = "7R";
                        break;
                    case 8:
                        posNumFake = "6R";
                        break;
                    case 7:
                        posNumFake = "5R";
                        break;
                    case 6:
                        posNumFake = "4R";
                        break;
                    case 5:
                        posNumFake = "4R";
                        break;
                    case 4:
                        posNumFake = "3R";
                        break;
                    case 3:
                        posNumFake = "3R";
                        break;
                    case 2:
                        posNumFake = "2R";
                        break;
                    case 1:
                        posNumFake = "1R";
                        break;
                    default:
                        posNumFake = "Error";

                }

                char[] posNumFakechars = posNumFake.toCharArray();
                String leftCanPos;

                if (posNumFakechars[1] != 'R') {
                    leftCanPos = String.valueOf(posNumFakechars[0]) + String.valueOf(posNumFakechars[1]);
                }

                else {
                    leftCanPos = String.valueOf(posNumFakechars[0]);
                }

                int leftCanPos2 = Integer.parseInt(leftCanPos);

                TableRow checkerD = null;

                try {
                    checkerD = findViewById(leftCanPos2);
                }
                catch (Exception ch)
                {
                    ch.printStackTrace();
                }

                if (checkerD != null)
                {
                    if (posNumFakechars[1] == 'R' || posNumFakechars[2] == 'R') {

                        TableRow tr = checkerD;
                        TextView label_right = new TextView(this);
                        label_right.setId(3000 + k);
                        label_right.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_right.setTextColor(textColor);
                        label_right.setPaintFlags(label_right.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_right.setPadding(0, 50, 0, 50);
                        label_right.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_right.setTextSize(18);
                        tr.addView(label_right);

                        int finalK = k;
                        int finalK1 = k;
                        label_right.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                else {

                    TableRow tr = new TableRow(this);
                    tr.setId(Integer.parseInt(posNum));
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    if (posNumFakechars[1] == 'R' || posNumFakechars[2] == 'R') {

                        TextView label_right = new TextView(this);
                        label_right.setId(3500 + k);
                        label_right.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_right.setTextColor(textColor);
                        label_right.setPaintFlags(label_right.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_right.setPadding(0, 50, 0, 50);
                        label_right.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_right.setTextSize(18);
                        tr.addView(label_right);

                        int finalK = k;
                        int finalK1 = k;
                        label_right.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                    } else {
                        TextView label_right = new TextView(this);
                        label_right.setId(4000 + k);
                        label_right.setText(finalCanNames[k] + "\n" + "Error with D");
                        label_right.setTextColor(textColor);
                        label_right.setPaintFlags(label_right.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_right.setPadding(0, 50, 0, 50);
                        label_right.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_right.setTextSize(18);
                        tr.addView(label_right);

                        int finalK = k;
                        int finalK1 = k;
                        label_right.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                    tl.addView(tr, 0, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));


                }

            }


            else {
                String posNum;

                if (characters [1] != 'L' && characters [1] != 'R' && characters [1] != 'C') {
                    posNum = String.valueOf(characters[0]) + String.valueOf(characters[1]);
                }

                else {
                    posNum = String.valueOf(characters[0]);
                }

                TableRow checker = null;

                try {
                    checker = findViewById(Integer.parseInt(posNum));
                }
                catch (Exception ch)
                {
                    Log.d("Add Rows Checker", "Checker Error");
                }

                if (checker != null)
                {
                    Log.d("checker", String.valueOf(checker));

                    int posNum2 = Integer.parseInt(posNum);

                    if (characters[1] == 'L' || characters[2] == 'L') {

                        TableRow tr = checker;
                        TextView label_left = new TextView(this);
                        label_left.setId(8000 + k);
                        label_left.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_left.setTextColor(textColor);
                        label_left.setPaintFlags(label_left.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_left.setPadding(0, 50, 0, 50);
                        label_left.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_left.setTextSize(18);
                        tr.addView(label_left);
                        tr.setPadding(0,0,-360,0);

                        int finalK = k;
                        int finalK1 = k;
                        label_left.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                    if (characters[1] == 'R' || characters[2] == 'R') {

                        TableRow tr = checker;
                        TextView label_right = new TextView(this);
                        label_right.setId(9000 + k);
                        label_right.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_right.setTextColor(textColor);
                        label_right.setPaintFlags(label_right.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_right.setPadding(0, 50, 0, 50);
                        label_right.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_right.setTextSize(18);
                        tr.addView(label_right);

                        int finalK = k;
                        int finalK1 = k;
                        label_right.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                else
                {
                    TableRow tr = new TableRow(this);
                    tr.setId(Integer.parseInt(posNum));
                    tr.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    if (characters[1] == 'L' || characters[2] == 'L') {

                        TextView label_left = new TextView(this);
                        label_left.setId(5000 + k);
                        label_left.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_left.setTextColor(textColor);
                        label_left.setPaintFlags(label_left.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_left.setPadding(0, 50, 0, 50);
                        label_left.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_left.setTextSize(18);
                        tr.addView(label_left);

                        int finalK = k;
                        int finalK1 = k;
                        label_left.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                    if (characters[1] == 'R' || characters[2] == 'R') {

                        TextView label_right = new TextView(this);
                        label_right.setId(6000 + k);
                        label_right.setText(finalCanNames[k] + "\n" + finalPositionNames[k]);
                        label_right.setTextColor(textColor);
                        label_right.setPaintFlags(label_right.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        label_right.setPadding(0, 50, 0, 50);
                        label_right.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        label_right.setTextSize(18);
                        tr.addView(label_right);

                        tr.setPadding(360,0,0,0);

                        int finalK = k;
                        int finalK1 = k;
                        label_right.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PosVerify.this);
                                builder1.setMessage("Position Information" +
                                        "\n" + "\nPosition: " + finalPositionNames[finalK] + "\nAsset Name: " + finalCanNames[finalK] + "\nWeight: " + finalWeights[finalK1] + "lbs" + "\nInspected By: " + verifiedByString[0] + "\nPosition Verified By: " + verifiedPosByString[0]
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

                    tl.addView(tr, 0, new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));


                }


            }


        }
    }


}