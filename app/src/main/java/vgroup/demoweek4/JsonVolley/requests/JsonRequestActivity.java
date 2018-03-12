package vgroup.demoweek4.JsonVolley.requests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import vgroup.demoweek4.JsonVolley.utils.Const;
import vgroup.demoweek4.R;
import vgroup.demoweek4.alerts.AlertBuilder;
import vgroup.demoweek4.controller.AppController;
import vgroup.demoweek4.utils.Utils;

public class JsonRequestActivity extends Activity {

    private String TAG = JsonRequestActivity.class.getSimpleName();
    private Button btnServerJson, btnJsonLocal, clear;
    private TextView msgResponse;
    private ProgressDialog pDialog;
    private Context context;
    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        init();


    }


    /**
     * used to check internet connectivity
     *
     * @return true if connected or else false
     */

//    private boolean checkInternet() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null;
//    }
    private void init() {
        context = JsonRequestActivity.this;
        btnServerJson = findViewById(R.id.btnServerJson);
        btnJsonLocal = findViewById(R.id.btnLocalJson);
        msgResponse = findViewById(R.id.msgResponse);
        clear = findViewById(R.id.btnClear);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btnServerJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgResponse.setText("");
                if (Utils.checkInternet((Activity) context)) {
                    parseServerJson();
                    clear.setEnabled(true);
                } else {
                    AlertBuilder.getInstance().getDialog(JsonRequestActivity.this, getString(R.string.error_internet), 1);
                }
            }
        });
        btnJsonLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgResponse.setText("");
                parseJsonLocal();
                clear.setEnabled(true);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgResponse.setText("");
                clear.setEnabled(false);

            }
        });
    }

    //used to show progress dialog
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    //used to hide progress dialog
    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * captures the file from asset folder and return a string
     *
     * @return string from the json file
     */
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = JsonRequestActivity.this.getAssets().open("jsonArray.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * parses the json captured from the assets folder
     */
    private void parseJsonLocal() {
        try {

            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            jsonResponse = "";
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject person = (JSONObject) jsonArray
                        .get(i);

                String name = person.getString("name");
                String email = person.getString("email");
                JSONObject phone = person
                        .getJSONObject("phone");
                String home = phone.getString("home");
                String mobile = phone.getString("mobile");

                jsonResponse += "Name: " + name + "\n\n";
                jsonResponse += "Email: " + email + "\n\n";
                jsonResponse += "Home: " + home + "\n\n";
                jsonResponse += "Mobile: " + mobile + "\n\n\n";

            }

            msgResponse.setText(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Making json array request from server
     */
    private void parseServerJson() {
        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_JSON_ARRAY,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                                jsonResponse += "Mobile: " + mobile + "\n\n\n";

                            }

                            msgResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }


}
