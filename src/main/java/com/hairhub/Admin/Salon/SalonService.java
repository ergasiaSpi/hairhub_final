package com.hairhub.Admin.Salon;
import java.util.List;

public class SalonService {
  private SalonDao salonDAO = new SalonDao();
  public void addSalon(Salon salon) {
    salonDAO.addSalon(salon);
  }
  public List<Salon> getAllSalons() {
    return salonDAO.getAllSalons();
  }
}
