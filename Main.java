import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * July 5, 2026
 * main.java
 * This class handles CLI functions
 */
public class main {
    public static void main(String[] args) {

        CarDatabase data = new CarDatabase(); //Connects to local database
        Scanner scan = new Scanner(System.in);
        String query; //Used to search through database
        String choice; //Used to navigate CLI
        boolean running = true; //Keeps while loop true until 0 is entered
        List<Car> wishlist = new ArrayList<>(); //Initates wishlist

        while (running) {
            //CLI MENU
            System.out.println("Enter the number you would like to execute:\n\n" +
                    "1. Search by Make\n" +
                    "2. Search by Model\n" +
                    "3. Search by Country\n" +
                    "4. Sort by Horsepower\n" +
                    "5. Sort by Make\n" +
                    "6. Sort by Country\n" +
                    "7. Sort by Top Speed\n" +
                    "8. Show all cars\n" +
                    "9. Add/remove wishlist item\n" +
                    "10. check wishlist\n" +
                    "0. Exit\n");
            choice = scan.nextLine().trim();
            switch (choice) {
                //SEARCH FUNCTION -- MAKE
                case "1" -> {
                    List<Car> results;
                    System.out.println("What make are you looking for?");
                    query = scan.nextLine().trim();
                    results = data.searchByMake(query);
                    printResults(results);
                }
                //SEARCH FUNCTION -- MODEL
                case "2" -> {
                    List<Car> results;
                    System.out.println("What model are you looking for?");
                    query = scan.nextLine().trim();
                    results = data.searchByModel(query);
                    printResults(results);
                }
                //SEARCH FUNCTION -- COUNTRY
                case "3" -> {
                    List<Car> results;
                    System.out.println("What country are you looking for?");
                    query = scan.nextLine().trim();
                    results = data.searchByCountry(query);
                    printResults(results);
                }
                //SORT -- HORSEPOWER
                case "4" -> {
                    List<Car> sortedHorses = data.sortByHorsepower();
                    printResults(sortedHorses);
                }
                //SORT -- MAKE
                case "5" -> {
                    List<Car> sortedMakes = data.sortByMake();
                    printResults(sortedMakes);
                }
                //SORT -- COUNTRY
                case "6" -> {
                    List<Car> sortedCountry = data.sortByCountry();
                    printResults(sortedCountry);
                }
                //SORT -- TOP SPEED
                case "7" -> {
                    List<Car> sortedTopSpeed = data.sortByTopSpeed();
                    printResults(sortedTopSpeed);
                }
                //PRINT ALL CARS
                case "8" -> {
                    List<Car> allCars = data.getAllCars();
                    allCars.sort(Comparator.comparing(Car::getMake));
                    printResults(allCars);
                }
                //MODIFY WISHLIST
                case "9" -> {
                    System.out.println("  1. Add to wishlist");
                    System.out.println("  2. Remove from wishlist");
                    System.out.print("Choose an option: ");
                    String wishlistChoice = scan.nextLine().trim();

                    //ADD CAR TO WISHLIST
                    if (wishlistChoice.equals("1")) {
                        System.out.print("Enter make to search for a car to add: ");
                        String addQuery = scan.nextLine().trim();
                        List<Car> addMatches = data.searchByMake(addQuery); //Appends matches from CarDatabase into Array
                        if (addMatches.isEmpty()) { //Checks the addMatches array to see if it is null
                            System.out.println("No cars found.");
                            break;
                        }
                        numberedList(addMatches); //Prints out a numbered list of options to append to wishlist
                        System.out.print("Enter the number of the car to add (1-" + addMatches.size() + "): ");
                        int addIndex; //Stores car the will be saved to wishlist
                        try {
                            addIndex = Integer.parseInt(scan.nextLine().trim()) - 1; //Gathers the integer entered to append
                        } catch (
                                NumberFormatException e) { //Error handling if another value is entered (ex. 'A', 999999, "abc")
                            System.out.println("That's not a valid number.");
                            break;
                        }
                        Car carToAdd = addMatches.get(addIndex); //Stores selected option in placeholder
                        if (!wishlist.contains(carToAdd)) { //Verifies carToAdd is not already in wishlist
                            wishlist.add(carToAdd);
                            System.out.println(carToAdd.getModel() + " added to wishlist.");
                        } else {
                            System.out.println(carToAdd.getModel() + " is already in your wishlist.");
                        }
                        //REMOVE CAR FROM WISHLIST
                    } else if (wishlistChoice.equals("2")) {
                        if (wishlist.isEmpty()) { //Checks the wishlist is empty before allowing user to proceed
                            System.out.println("Your wishlist is empty.");
                            break;
                        }
                        numberedList(wishlist); //Displays wishlist numbered
                        System.out.print("Enter the number of the car to remove (1-" + wishlist.size() + "): ");
                        int removeIndex;
                        try {
                            removeIndex = Integer.parseInt(scan.nextLine().trim()) - 1; //Grabs the option
                        } catch (NumberFormatException e) { //Error handling for invalid inputs
                            System.out.println("That's not a valid number.");
                            break;
                        }
                        Car toRemove = wishlist.get(removeIndex); //Gets the array value to remove
                        wishlist.remove(removeIndex); //removes value
                        System.out.println(toRemove.getModel() + " removed from wishlist.");
                    } else {
                        System.out.println("Invalid option, try again.");
                    }
                    break;
                }
                //CHECKS WISHLIST
                case "10" -> numberedList(wishlist);
                //EXIT
                case "0" -> running = false;
                //NOTIFY USER OF INVALID OPTION
                default -> System.out.println("Not a valid option");
            }

        }
    }

    //GATHERS TOTAL AMOUNT OF CARS AND PRINTS THE TOTAL CARS IN DATABASE
    private static void printResults(List<Car> cars) {
        if (cars.isEmpty()) {
            System.out.println("No cars found.");
            return;
        }
        System.out.println("Found " + cars.size() + " matches:"); //prints total matches (int)
        for (Car car : cars) {
            System.out.println(" " + car.outputInfo()); //Prints the query result
        }
    }

    //OUTPUTS ARRAY AS NUMBERED LIST; PRIMARILY FOR WISHLIST
    private static void numberedList(List<Car> cars) {
        for (int i = 0; i < cars.size(); i++) {
            System.out.println((i + 1) + ". " + cars.get(i).getMake() + " " + cars.get(i).getModel());
        }
    }
}
