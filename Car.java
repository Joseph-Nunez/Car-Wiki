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

    /**
     * Constructs a Car with the given make, model, country, top speed, and horsepower.
     *
     * @param make      the car's manufacturer/brand
     * @param model     the car's model name
     * @param country   the country the car is from
     * @param topSpeed  the car's top speed in mph
     * @param horsepower the car's horsepower
     */
    public Car(String make, String model, String country, int topSpeed, int horsepower){
        this.Make = make;
        this.Model = model;
        this.Country = country;
        this.Horsepower = horsepower;
        this.TopSpeed = topSpeed;
    }

    /**
     * Builds a human-readable summary of this car's make, model, country,
     * top speed, and horsepower.
     *
     * @return a formatted string like "Make Model (Country) - Top Speed: N mph, Horsepower: N hp"
     */
    public String outputInfo(){
        return String.format("%s %s (%s) - Top Speed: %d mph, Hosrepower: %d hp", Make, Model, Country, TopSpeed, Horsepower);
    }

    /**
     * @return this car's make
     */
    public String getMake() {
        return Make;
    }

    /**
     * @return this car's model
     */
    public String getModel() {
        return Model;
    }

    /**
     * @return this car's country of origin
     */
    public String getCountry() {
        return Country;
    }

    /**
     * @return this car's top speed in mph
     */
    public double getTopSpeed() {
        return TopSpeed;
    }

    /**
     * @return this car's horsepower
     */
    public double getHorsepower() {
        return Horsepower;
    }

    /**
     * @param make the new make to assign to this car
     */
    public void setMake(String make) {
        this.Make = make;
    }

    /**
     * @param model the new model to assign to this car
     */
    public void setModel(String model) {
        this.Model = model;
    }

    /**
     * @param country the new country to assign to this car
     */
    public void setCountry(String country) {
        this.Country = country;
    }

    /**
     * @param topSpeed the new top speed (mph) to assign to this car
     */
    public void setTopSpeed(int topSpeed) {
        this.TopSpeed = topSpeed;
    }

    /**
     * @param horsepower the new horsepower to assign to this car
     */
    public void setHorsepower(int horsepower) {
        this.Horsepower = horsepower;
    }

    /**
     * Checks whether every field is filled in for this car to count as a valid
     * record: make, model, and country must be non-null and non-blank, and top
     * speed and horsepower must both be greater than 0.
     *
     * @return true if all fields are present and valid; false otherwise
     */
    public boolean validate() {
        return Make != null && !Make.trim().isEmpty()
                && Model != null && !Model.trim().isEmpty()
                && Country != null && !Country.trim().isEmpty()
                && TopSpeed > 0
                && Horsepower > 0;
    }

}
