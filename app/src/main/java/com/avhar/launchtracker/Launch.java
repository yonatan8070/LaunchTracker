package com.avhar.launchtracker;

import java.text.SimpleDateFormat;

public class Launch {
  private String name;
  private String provider;
  private String status;
  private SimpleDateFormat net;
  private String launchType;

  public Launch() {
  }

  public Launch(String name, String provider) {
    this.name = name;
    this.provider = provider;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public SimpleDateFormat getNet() {
    return net;
  }

  public void setNet(SimpleDateFormat net) {
    this.net = net;
  }

  public String getLaunchType() {
    return launchType;
  }

  public void setLaunchType(String launchType) {
    this.launchType = launchType;
  }

  public String toString() {
    return this.name + this.provider;
  }
}
