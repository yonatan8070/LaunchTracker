package com.avhar.launchtracker;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.avhar.launchtracker.data.Launch;
import com.avhar.launchtracker.data.LaunchTelemetry;
import com.avhar.launchtracker.data.Rocket;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class DetailsActivity extends AppCompatActivity {
  private RequestQueue mQueue;
  private LaunchTelemetry telemetry;
  private String url = "https://api.launchdashboard.space/v2/launches?launch_library_2_id=7cea85fa-b373-4896-83ae-2629f4030806";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.Theme_LaunchTracker);
    setContentView(R.layout.activity_details);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Launch launch = (Launch) getIntent().getSerializableExtra("launch");
    assert launch != null;

    boolean displayCountdown = getIntent().getBooleanExtra("displayCountdown", false);

//    url += launch.getLl2Id();

    ImageView loadingIcon = findViewById(R.id.loadingIcon);
    ((AnimationDrawable) loadingIcon.getBackground()).start();

    mQueue = Volley.newRequestQueue(this);
    loadTelemetry();

    TextView launchName = findViewById(R.id.launchName);
    launchName.setText(launch.getName());

    TextView launchProviderAndType = findViewById(R.id.launchProviderAndType);
    launchProviderAndType.setText(launch.getProvider() + " - " + launch.getLaunchType());

    TextView description = findViewById(R.id.description);
    description.setText(launch.getDescription());

    TextView countdownView = findViewById(R.id.countdown);
    if (displayCountdown) {
      SimpleDateFormat countdownFormat = new SimpleDateFormat("'T-' DD : HH : mm : ss", Locale.getDefault());

      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          Date now = new Date();
          long timeUntilLaunch = launch.getNet().getTime() - now.getTime();
          countdownView.setText(countdownFormat.format(new Date(timeUntilLaunch + 1000)));

          handler.postDelayed(this, 1000 - (now.getTime() % 1000));
        }
      }, 0);
    } else {
      countdownView.setVisibility(View.GONE);
    }

    TextView netView = findViewById(R.id.net);
    SimpleDateFormat netFormat = new SimpleDateFormat("'NET: 'dd/MM/yyyy 'at' HH:mm:ss", Locale.getDefault());
    netView.setText(netFormat.format(launch.getNet()));

    TabLayout graphTabGroup = findViewById(R.id.graphSelector);
    graphTabGroup.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      /*
       * Never gonna give you up
       * Never gonna let you down
       * Never gonna run around and desert you
       * Never gonna make you cry
       * Never gonna say goodbye
       * Never gonna tell a lie and hurt you
       * */
      private int rickRollCounter = 0;

      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        rickRollCounter = 0;
        LineChart chart = findViewById(R.id.chart);
        List<ILineDataSet> dataSets = chart.getLineData().getDataSets();

        for (int i = 0; i < dataSets.size(); i++) {
          dataSets.get(i).setVisible(tab.getPosition() == i);
        }

        float min = dataSets.get(tab.getPosition()).getYMin();
        float max = dataSets.get(tab.getPosition()).getYMax();

        float padding = (max - min) * 0.05f;

        chart.resetViewPortOffsets();

        chart.setVisibleYRange(0, max + padding, YAxis.AxisDependency.LEFT);
        chart.moveViewTo(0, (min + max) / 2, YAxis.AxisDependency.LEFT);

        chart.animateX(500, Easing.EaseInOutSine);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        if (++rickRollCounter == 10) {
          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
          startActivity(browserIntent);
        }
      }
    });

    TextView windowStart = findViewById(R.id.windowStart);
    SimpleDateFormat windowStartFormat = new SimpleDateFormat("'Window start: 'dd/MM/yyyy 'at' HH:mm:ss", Locale.getDefault());
    windowStart.setText(windowStartFormat.format(launch.getWindowStart()));

    TextView windowEnd = findViewById(R.id.windowEnd);
    SimpleDateFormat windowEndFormat = new SimpleDateFormat("'Window end: 'dd/MM/yyyy 'at' HH:mm:ss", Locale.getDefault());
    windowEnd.setText(windowEndFormat.format(launch.getWindowEnd()));

    Rocket rocket = launch.getRocket();
    ConstraintLayout rocketName = findViewById(R.id.rocketName);
    TextView nameLabel = rocketName.findViewById(R.id.label);
    TextView nameValue = rocketName.findViewById(R.id.value);
    nameLabel.setText("Name");
    nameValue.setText(rocket.getName());
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }

  private void loadTelemetry() {
    telemetry = new LaunchTelemetry();
    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                System.out.println("Response received from " + url);
                try {
                  JSONArray frameArray = response.getJSONArray("analysed").getJSONObject(0).getJSONArray("telemetry");

                  long start = new Date().getTime();

                  for (int i = 0; i < frameArray.length(); i++) {
                    JSONObject frame = frameArray.getJSONObject(i);

                    telemetry.addFrame(frame.getDouble("time"),
                            frame.getDouble("altitude"),
                            frame.getDouble("velocity"),
                            frame.getDouble("acceleration"));
                  }

                  long now = new Date().getTime();
                  System.out.println("Done in " + (now - start) + "ms");

                  displayChart();
                } catch (JSONException e) {
                  // This runs in case the JSON does not contain the correct keys
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {
      @SuppressLint("SetTextI18n")
      @Override
      public void onErrorResponse(VolleyError error) {
        TextView errorView = findViewById(R.id.errorText);
        if (error.networkResponse.statusCode == 404) {
          errorView.setText("Telemetry is not available for this launch");
        } else {
          errorView.setText("Error: " + error.networkResponse.statusCode);
        }
        errorView.setVisibility(View.VISIBLE);

        findViewById(R.id.loadingIcon).setVisibility(View.INVISIBLE);
        error.printStackTrace();
      }
    });
    mQueue.add(request);
  }

  private void displayChart() {
    LineChart chart = findViewById(R.id.chart);

    // I have no fucking clue why this works, but it somehow gets the color right
    TypedValue typedValue = new TypedValue();
    Resources.Theme theme = this.getTheme();
    theme.resolveAttribute(R.attr.titleTextColor, typedValue, true);
    @ColorInt int textColor = typedValue.data;

    chart.getLegend().setEnabled(false);
    chart.getDescription().setEnabled(false);
    chart.setHighlightPerTapEnabled(false);
    chart.setHighlightPerDragEnabled(false);
    chart.setDoubleTapToZoomEnabled(false);
    chart.setScaleEnabled(false);
    chart.setDragEnabled(false);

    YAxis leftAxis = chart.getAxis(YAxis.AxisDependency.LEFT);
    leftAxis.setTypeface(ResourcesCompat.getFont(this, R.font.rubik_regular));
    leftAxis.setTextColor(textColor);

    YAxis rightAxis = chart.getAxis(YAxis.AxisDependency.RIGHT);
    rightAxis.setTypeface(ResourcesCompat.getFont(this, R.font.rubik_regular));
    rightAxis.setTextColor(textColor);

    XAxis topAxis = chart.getXAxis();
    topAxis.setTypeface(ResourcesCompat.getFont(this, R.font.rubik_regular));
    topAxis.setTextColor(textColor);

    List<Entry> velocity = new ArrayList<>();
    List<Entry> acceleration = new ArrayList<>();
    List<Entry> altitude = new ArrayList<>();

    System.out.println(telemetry.getSize());
    for (int i = 0; i < telemetry.getSize(); i++) {
      Map<String, Double> frame = telemetry.getFrameAt(i);
      velocity.add(new Entry(frame.get("time").floatValue(), frame.get("velocity").floatValue()));
      acceleration.add(new Entry(frame.get("time").floatValue(), frame.get("acceleration").floatValue()));
      altitude.add(new Entry(frame.get("time").floatValue(), frame.get("altitude").floatValue()));
    }

    LineDataSet velocityDataSet = new LineDataSet(velocity, "Velocity");
    LineDataSet accelerationDataSet = new LineDataSet(acceleration, "Acceleration");
    LineDataSet altitudeDataSet = new LineDataSet(altitude, "Altitude");

    velocityDataSet.setCircleColor(0);
    accelerationDataSet.setCircleColor(0);
    altitudeDataSet.setCircleColor(0);

    // Setting the size is necessary for some reason, without it the circles are still visible
    velocityDataSet.setCircleRadius(1.0f);
    accelerationDataSet.setCircleRadius(1.0f);
    altitudeDataSet.setCircleRadius(1.0f);

    velocityDataSet.setColor(0xFFFF0000);
    accelerationDataSet.setColor(0xFF00FF00);
    altitudeDataSet.setColor(0xFF0000FF);

    velocityDataSet.setLineWidth(2.0f);
    accelerationDataSet.setLineWidth(2.0f);
    altitudeDataSet.setLineWidth(2.0f);

    accelerationDataSet.setVisible(false);
    altitudeDataSet.setVisible(false);

    LineData lineData = new LineData(velocityDataSet, accelerationDataSet, altitudeDataSet);

    chart.setData(lineData);

    chart.setVisibility(View.VISIBLE);
    findViewById(R.id.loadingIcon).setVisibility(View.GONE);

    chart.animateX(500, Easing.EaseInOutSine);
  }
}