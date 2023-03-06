package project.models;

public class Apartment {
    private int number;
    private double area;
    private String owner;
    private String address;
    private String repairType;

    public Apartment(int number, double area, String owner, String address, String repairType) {
        this.number = number;
        this.area = area;
        this.owner = owner;
        this.address = address;
        this.repairType = repairType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }
}
