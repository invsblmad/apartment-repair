package project.models;

public class Material {
    private int id;
    private String name;
    private String manufacturer;
    private double price;
    private String unit;
    private int amount;
    private String image;
    private String state;

    public Material(int id, String name, String manufacturer, double price, String unit, int amount) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.unit = unit;
        this.amount = amount;
        this.image = "unknown.png";
        this.state = "initial";
    }

    public Material (String name, String manufacturer, double price, String unit, int amount) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.unit = unit;
        this.amount = amount;
        this.image = "unknown.png";
        this.state = "initial";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
