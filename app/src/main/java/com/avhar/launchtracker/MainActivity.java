package com.avhar.launchtracker;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avhar.launchtracker.data.Launch;
import com.avhar.launchtracker.data.Rocket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
  private RequestQueue mQueue;
  ArrayList<Launch> launches;
  LaunchAdapter adapter;
  String url = "https://lldev.thespacedevs.com/2.1.0/launch/upcoming?mode=detailed";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mQueue = Volley.newRequestQueue(this);
    launches = new ArrayList<>();

    long cacheTime = loadFromCache();
    long currentTime = new Date().getTime();

    if (currentTime - cacheTime > 900000) {
      launches.clear();
      loadFromAPI();
    } else {
      findViewById(R.id.loadingIcon).setVisibility(View.GONE);
    }

    RecyclerView rvLaunches = findViewById(R.id.rvLaunches);

    adapter = new LaunchAdapter(launches, getApplicationContext());

    rvLaunches.setLayoutManager(new LinearLayoutManager(this));
    rvLaunches.setAdapter(adapter);

    rvLaunches.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
          loadFromAPI();
        }
      }
    });
  }

  private void loadFromAPI() {
    ImageView loadingIcon = findViewById(R.id.loadingIcon);
    ((AnimationDrawable) loadingIcon.getBackground()).start();

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

                    launch.setLl2Id(jsonLaunch.optString("id"));
                    launch.setName(jsonLaunch.optString("name"));
                    launch.setProvider(jsonLaunch.getJSONObject("launch_service_provider").optString("name"));
                    launch.setLaunchType(jsonLaunch.getJSONObject("launch_service_provider").optString("type"));
                    launch.setStatus(jsonLaunch.getJSONObject("status").optInt("id"));

                    SimpleDateFormat decoder = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    decoder.setTimeZone(TimeZone.getTimeZone("Z"));
                    launch.setNet(decoder.parse(jsonLaunch.optString("net")));
                    launch.setWindowStart(decoder.parse(jsonLaunch.optString("window_start")));
                    launch.setWindowEnd(decoder.parse(jsonLaunch.optString("window_end")));

                    if (!jsonLaunch.isNull("mission")) {
                      launch.setDescription(jsonLaunch.getJSONObject("mission").getString("description"));
                    } else {
                      launch.setDescription("Description unavailable");
                    }

                    Rocket rocket = launch.getRocket();
                    JSONObject jsonRocket = jsonLaunch.getJSONObject("rocket").getJSONObject("configuration");

                    rocket.setDiameter(jsonRocket.optDouble("diameter"));
                    rocket.setLength(jsonRocket.optDouble("length"));
                    rocket.setName(jsonRocket.optString("full_name"));
                    rocket.setImage(jsonRocket.optString("max_stage"));
                    rocket.setMass(jsonRocket.optDouble("launch_mass"));
                    rocket.setLl2Id(jsonRocket.optInt("id"));
                    rocket.setLowEarthCapacity(jsonRocket.optDouble("leo_capacity"));

                    launches.add(launch);
                  }

                  url = response.getString("next");
                  loadingIcon.setVisibility(View.GONE);
                  updateCache();
                } catch (JSONException | ParseException e) {
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

  private void updateCache() {
    FileOutputStream dataStream;
    FileOutputStream dateStream;
    try {
      dataStream = this.openFileOutput("cache", Context.MODE_PRIVATE);
      dateStream = this.openFileOutput("date", Context.MODE_PRIVATE);
      ObjectOutputStream dataOutput = new ObjectOutputStream(dataStream);
      ObjectOutputStream dateOutput = new ObjectOutputStream(dateStream);
      dataOutput.writeObject(launches);
      dateOutput.writeObject(new Date());
      dataOutput.close();
      dataStream.close();
      dateOutput.close();
      dateStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private long loadFromCache() {
    long time = -1;

    FileInputStream fis;
    FileInputStream dateStream;
    try {
      fis = this.openFileInput("cache");
      dateStream = this.openFileInput("date");
      ObjectInputStream is = new ObjectInputStream(fis);
      ObjectInputStream dateInput = new ObjectInputStream(dateStream);
      launches = (ArrayList<Launch>) is.readObject();
      Date date = (Date) dateInput.readObject();
      time = date.getTime();
      is.close();
      fis.close();

      dateStream.close();
      dateInput.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return time;
  }
}