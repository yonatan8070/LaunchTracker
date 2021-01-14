package com.avhar.launchtracker.data;

import java.util.Date;

public class Launch {
  private String ll2Id;
  private String name;
  private String provider;
  private int status;
  private Date net;
  private String launchType;
  private Rocket rocket;

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
    return this.name + this.provider;
  }
}
