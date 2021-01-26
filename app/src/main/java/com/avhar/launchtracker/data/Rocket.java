package com.avhar.launchtracker.data;

import java.io.Serializable;

public class Rocket implements Serializable {
  private String ll2Id;
  private String name; // In text
  private double length; // In meters
  private double diameter; // In meters
  private double mass; // In Kilograms
  private double lowEarthCapacity; // In Kilograms
  private int stageCount; // In number
  private int launchCost; // In USD

  private String image; // In URL

  public Rocket() {
  }

  public String getLl2Id() {
    return ll2Id;
  }

  public void setLl2Id(String ll2Id) {
    this.ll2Id = ll2Id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLength() {
    return length;
  }

  public void setLength(double length) {
    this.length = length;
  }

  public double getDiameter() {
    return diameter;
  }

  public void setDiameter(double diameter) {
    this.diameter = diameter;
  }

  public double getMass() {
    return mass;
  }

  public void setMass(double mass) {
    this.mass = mass;
  }

  public double getLowEarthCapacity() {
    return lowEarthCapacity;
  }

  public void setLowEarthCapacity(double lowEarthCapacity) {
    this.lowEarthCapacity = lowEarthCapacity;
  }

  public int getStageCount() {
    return stageCount;
  }

  public void setStageCount(int stageCount) {
    this.stageCount = stageCount;
  }

  public int getLaunchCost() {
    return launchCost;
  }

  public void setLaunchCost(int launchCost) {
    this.launchCost = launchCost;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
