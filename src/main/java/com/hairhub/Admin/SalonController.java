package com.hairhub.Admin;
import java.util.List;

public class SalonController {
  private SalonService salonService = new SalonService();
  public void showAllSalons() {
    List<Salon> salons = salonService.getAllSalons();
    for (Salon salon : salons) {
      System.out.println("Name: " + salon.getName());
      System.out.println("Phone: " + salon.getPhoneNumber());
      System.out.println("Rating: " + salon.getRating());
    }
  }
}
