package com.hairhub.Admin;

public class Salon {
  private int salonId;
  private String name;
  private String address;
  private String phoneNumber;
  private String email;
  private double rating;
 
  public int getSalonId() {
    return salonId;
  }
  public void setSalonId(int salonId) {
    this.salonId = salonId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public double getRating() {
    return rating;
  }
  public void setRating(double rating) {
    this.rating = rating;
  }
}
