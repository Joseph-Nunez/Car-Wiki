/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * June 5, 2026
 * Car.java
 * This class defines the attributes that
 * will be used to identify cars
 */

public class Car {

    private String Make, Model, Country;
    private int TopSpeed, Horsepower;

    public Car(String make, String model, String country, int topSpeed, int horsepower){
        this.Make = make;
        this.Model = model;
        this.Country = country;
        this.Horsepower = horsepower;
        this.TopSpeed = topSpeed;
    }

    public String outputInfo(){
        return String.format("%s %s (%s) - Top Speed: %d mph, Hosrepower: %d hp", Make, Model, Country, TopSpeed, Horsepower);
    }
    public String getMake() {
        return Make;
    }
    public String getModel() {
        return Model;
    }
    public String getCountry() {
        return Country;
    }
    public double getTopSpeed() {
        return TopSpeed;
    }
    public double getHorsepower() {
        return Horsepower;
    }
    public void setMake(String make) {
        this.Make = make;
    }
    public void setModel(String model) {
        this.Model = model;
    }
    public void setCountry(String country) {
        this.Country = country;
    }
    public void setTopSpeed(int topSpeed) {
        this.TopSpeed = topSpeed;
    }
    public void setHorsepower(int horsepower) {
        this.Horsepower = horsepower;
    }

    // checks if every field is entered for a car to be a valid record
    public boolean validate() {
        return Make != null && !Make.trim().isEmpty()
                && Model != null && !Model.trim().isEmpty()
                && Country != null && !Country.trim().isEmpty()
                && TopSpeed > 0
                && Horsepower > 0;
    }

}
