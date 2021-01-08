package com.avhar.launchtracker;
// continue here: https://guides.codepath.com/android/Using-the-RecyclerView#binding-the-adapter-to-the-recyclerview

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
  //  private TextView mTextViewResult;
  private RequestQueue mQueue;
  ArrayList<Launch> launches;
  LaunchAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    Button buttonParse = findViewById(R.id.button);

    mQueue = Volley.newRequestQueue(this);
    launches = new ArrayList<>();
    jsonParse();

    buttonParse.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        jsonParse();
      }
    });

    RecyclerView rvLaunches = (RecyclerView) findViewById(R.id.rvLaunches);

    adapter = new LaunchAdapter(launches, getApplicationContext());

    rvLaunches.setLayoutManager(new LinearLayoutManager(this));
    rvLaunches.setAdapter(adapter);
  }

  private void jsonParse() {
    String url = "https://lldev.thespacedevs.com/2.1.0/launch/upcoming";

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              // This runs where a response arrives from the server
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("Response received from " + url);
                try {
                  // This is the object containing the JSON list with the launches
                  JSONArray results = response.getJSONArray("results");

                  for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonLaunch = results.getJSONObject(i);
                    Launch launch = new Launch();

                    launch.setName(jsonLaunch.getString("name"));
                    launch.setProvider(jsonLaunch.getJSONObject("launch_service_provider").getString("name"));
                    launch.setLaunchType(jsonLaunch.getJSONObject("launch_service_provider").getString("type"));
                    launch.setStatus(jsonLaunch.getJSONObject("status").getInt("id"));

                    launch.setNet(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(jsonLaunch.getString("net")));


                    launches.add(launch);
                  }
                } catch (JSONException | ParseException e) {
                  // This runs in case the JSON does not contain the correct keys
                  e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
              }
            }, new Response.ErrorListener() {
      // This runs if an error is returned from the server
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Toast toast = new Toast(getApplicationContext());
        toast.setText("Error");
        toast.show();
      }
    });
    mQueue.add(request);
  }
}