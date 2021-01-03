package com.avhar.launchtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Launch {
  private String name;
  private String provider;
  private int status;
  private Date net;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getNet() {
    return net;
  }

  public void setNet(Date net) {
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
