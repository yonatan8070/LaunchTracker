package com.avhar.launchtracker;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avhar.launchtracker.data.Launch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
  private RequestQueue mQueue;
  ArrayList<Launch> launches;
  LaunchAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mQueue = Volley.newRequestQueue(this);
    launches = new ArrayList<>();
    jsonParse();

    RecyclerView rvLaunches = findViewById(R.id.rvLaunches);

    adapter = new LaunchAdapter(launches, getApplicationContext());

    rvLaunches.setLayoutManager(new LinearLayoutManager(this));
    rvLaunches.setAdapter(adapter);
  }

  private void jsonParse() {
    String url = "https://lldev.thespacedevs.com/2.1.0/launch/upcoming";

    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("Response received from " + url);
                try {
                  JSONArray results = response.getJSONArray("results");

                  for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonLaunch = results.getJSONObject(i);
                    Launch launch = new Launch();

                    launch.setLl2Id(jsonLaunch.getString("id"));
                    launch.setName(jsonLaunch.getString("name"));
                    launch.setProvider(jsonLaunch.getJSONObject("launch_service_provider").getString("name"));
                    launch.setLaunchType(jsonLaunch.getJSONObject("launch_service_provider").getString("type"));
                    launch.setStatus(jsonLaunch.getJSONObject("status").getInt("id"));

                    SimpleDateFormat decoder = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    decoder.setTimeZone(TimeZone.getTimeZone("Z"));
                    launch.setNet(decoder.parse(jsonLaunch.getString("net")));

                    launches.add(launch);
                  }
                } catch (JSONException | ParseException e) {
                  // This runs in case the JSON does not contain the correct keys
                  e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
      }
    });
    mQueue.add(request);
  }
}