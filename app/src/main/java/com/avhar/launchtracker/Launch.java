package com.avhar.launchtracker;

import java.text.SimpleDateFormat;

public class Launch {
  private String name;
  private String provider;
  private SimpleDateFormat net;

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

  public String toString() {
    return this.name + this.provider;
  }
}
