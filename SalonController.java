public class SalonController {
  private SalonService salonService = new SalonService();
  public void showAllSalons() {
    List<Salon> salons = salonService.getAllSalons();
    for (Salon salon : salons) {
      System.out.println("Name: " + salon.getName());
      System.out.println("Location: " + salon.getLocation());
      System.out.println("Phone: " + salon.getPhoneNumber());
      System.out.println("Rating: " + salon.getRating());
    }
  }
  public void searchSalonsByLocation(String location) {
    List<Salon> salons = salonServise.findSalonsByLocation(location);
    if (salons.isEmpty()) {
      System.out.println("No salons found in this location.");
    } else {
      for (Salon salon : salons) {
        System.out.println("Name: " + salon.getName());
        System.out.println("Location: " + salon.getLocation());
        System.out.println("Phone: " + salon.getPhoneNumber());
        System.out.println("Rating: " + salon.getRating());
      }
    }
  }
}
