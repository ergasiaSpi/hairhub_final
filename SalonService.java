import java.util.List;
public class SalonService {
  private SalonDAO salonDAO = new SalonDAO();
  public void addSalon(Salon salon) {
    salonDAO.addSalon(salon);
  }
  public List<Salon> getAllSalons() {
    return salonDAO.getAllSalons();
  }
  public List<Salon> findSalonsByLocation(String location) {
    return salonDAO.findSalonsByLoction(location);
  }
}
