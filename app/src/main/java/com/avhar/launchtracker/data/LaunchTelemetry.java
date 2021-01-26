package com.avhar.launchtracker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LaunchTelemetry {
  ArrayList<Map<String, Double>> data;

  public LaunchTelemetry() {
    this.data = new ArrayList<>();
  }

  public void addFrame(double time, double altitude, double velocity, double acceleration) {
    Map<String, Double> frame = new HashMap<>();

    frame.put("time", time);
    frame.put("altitude", altitude);
    frame.put("velocity", velocity);
    frame.put("acceleration", acceleration);

    data.add(frame);
  }

  public Map<String, Double> getFrameAt(int index) {
    return data.get(index);
  }

  public int getSize() {
    return data.size();
  }
}
