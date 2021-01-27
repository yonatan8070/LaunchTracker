package com.avhar.launchtracker.data;

import java.io.Serializable;
import java.util.Date;

public class Launch implements Serializable {
  private String ll2Id;
  private String name;
  private String provider;
  private String description;
  private int status;
  private Date net;
  private Date windowStart;
  private Date windowEnd;
  private String launchType;
  private Rocket rocket;

  public Launch() {
    this.rocket = new Rocket();
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public Date getWindowStart() {
    return windowStart;
  }

  public void setWindowStart(Date windowsStart) {
    this.windowStart = windowsStart;
  }

  public Date getWindowEnd() {
    return windowEnd;
  }

  public void setWindowEnd(Date windowEnd) {
    this.windowEnd = windowEnd;
  }

  public String getLaunchType() {
    return launchType;
  }

  public void setLaunchType(String launchType) {
    this.launchType = launchType;
  }

  public String getLl2Id() {
    return this.ll2Id;
  }

  public void setLl2Id(String ll2Id) {
    this.ll2Id = ll2Id;
  }

  public Rocket getRocket() {
    return this.rocket;
  }

  public void setRocket(Rocket rocket) {
    this.rocket = rocket;
  }

  public String toString() {
    return this.name + " " + this.provider;
  }
}
