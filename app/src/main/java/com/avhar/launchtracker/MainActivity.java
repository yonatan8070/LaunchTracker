package com.avhar.launchtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
  private TextView mTextViewResult;
  private RequestQueue mQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextViewResult = findViewById(R.id.textView);
    Button buttonParse = findViewById(R.id.button);

    mQueue = Volley.newRequestQueue(this);

    buttonParse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        jsonParse();
      }
    });
  }

  private void jsonParse() {
    String url = "https://lldev.thespacedevs.com/2.0.0/launch/upcoming";

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              // This runs where a response arrives from the server
              @Override
              public void onResponse(JSONObject response) {
                try {
                  // This is the object containing the JSON list with the launches
                  JSONArray jsonArray = response.getJSONArray("results");

                  for (int i = 0; i < jsonArray.length(); i++) {
                    // This is one specific launch at index i
                    JSONObject launch = jsonArray.getJSONObject(i);

                    String name = launch.getString("name");
                    String net = launch.getString("net");

                    mTextViewResult.append(name + " at " + net + "\n\n");
                  }
                } catch (JSONException e) {
                  // This runs in case the JSON does not contain the correct keys
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {
              // This runs if an error is returned from the server
              @Override
              public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
              }
            });
    mQueue.add(request);
  }
}