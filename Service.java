public class Service {

    // Ιδιότητες της κλάσης
    private int id;              // Μοναδικό ID της υπηρεσίας
    private String name;         // Όνομα υπηρεσίας
    private String description;  // Περιγραφή υπηρεσίας
    private double price;        // Τιμή υπηρεσίας
    private int duration;        // Διάρκεια σε λεπτά

    // Κατασκευαστής
    public Service(int id, String name, String description, double price, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Μέθοδος για εμφάνιση της υπηρεσίας ως string
    @Override
    public String toString() {
        return "Service{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", price=" + price +
               ", duration=" + duration + " mins" +
               '}';
    }
}
