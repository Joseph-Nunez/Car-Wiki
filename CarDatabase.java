import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * July 5, 2026
 * CarDatabase.java
 * This class keeps a list of cars to be able
 * to test the functionality and logic of car wiki
 */
public class CarDatabase {
    //Local database for testing avoiding SQLite
    private Car[] cars = {
            new Car("Nissan", "Altima", "Japan", 188, 332),
            new Car("Audi", "R8", "Germany", 174, 521),
            new Car("Ferrari", "Roma", "Italy", 219, 600),
            new Car("Tesla", "Model X", "USA", 148, 161),
            new Car("Rolls-Royce", "Cullinan", "United Kingdom", 205, 609),
            new Car("Chevrolet", "Impala", "USA", 147, 435),
            new Car("Koenigsegg", "Regera", "Sweden", 218, 757),
            new Car("Genesis", "G70", "South Korea", 155, 279),
            new Car("Hyundai", "Ioniq 5", "South Korea", 155, 540),
            new Car("Kia", "Telluride", "South Korea", 200, 290),
            new Car("Ford", "Focus RS", "USA", 145, 498),
            new Car("Honda", "Integra", "Japan", 192, 285),
            new Car("Chevrolet", "Corvette Stingray", "USA", 188, 522),
            new Car("Volvo", "C40", "Sweden", 118, 136),
            new Car("Toyota", "Corolla", "Japan", 182, 493),
            new Car("Skoda", "Octavia RS", "Czech Republic", 145, 141),
            new Car("Dodge", "Durango", "USA", 156, 520),
            new Car("Bugatti", "Veyron", "France", 217, 910),
            new Car("Rolls-Royce", "Phantom", "United Kingdom", 203, 708),
            new Car("Chevrolet", "Camaro ZL1", "USA", 199, 526),
            new Car("Volvo", "XC90", "Sweden", 122, 211),
            new Car("Ford", "Mustang GT", "USA", 159, 454),
            new Car("Kia", "EV6", "South Korea", 182, 441),
            new Car("Toyota", "Supra", "Japan", 168, 514),
            new Car("Nissan", "Leaf", "Japan", 168, 311),
            new Car("BMW", "M4", "Germany", 155, 365),
            new Car("Porsche", "Taycan", "Germany", 192, 773),
            new Car("SEAT", "Ateca", "Spain", 111, 195),
            new Car("Mazda", "MX-5 Miata", "Japan", 175, 367),
            new Car("Mitsubishi", "Eclipse", "Japan", 177, 362),
            new Car("Lotus", "Emira", "United Kingdom", 140, 286),
            new Car("Mercedes-Benz", "C63 AMG", "Germany", 185, 280),
            new Car("Opel", "Corsa", "Germany", 124, 128),
            new Car("Lamborghini", "Revuelto", "Italy", 218, 616),
            new Car("Hyundai", "Tucson", "South Korea", 195, 419),
            new Car("Porsche", "Cayman GT4", "Germany", 192, 863),
            new Car("Toyota", "Camry", "Japan", 155, 392),
            new Car("Maserati", "Ghibli", "Italy", 211, 848),
            new Car("Chrysler", "300 SRT", "USA", 123, 189),
            new Car("Lexus", "LFA", "Japan", 148, 542),
            new Car("Tesla", "Model Y", "USA", 146, 180),
            new Car("Nissan", "370Z", "Japan", 155, 492),
            new Car("Hyundai", "Veloster N", "South Korea", 191, 458),
            new Car("Audi", "RS3", "Germany", 152, 298),
            new Car("Ferrari", "SF90 Stradale", "Italy", 193, 937),
            new Car("BMW", "M5", "Germany", 167, 431),
            new Car("Alfa Romeo", "Giulia Quadrifoglio", "Italy", 167, 460),
            new Car("BMW", "320i", "Germany", 169, 277),
            new Car("Tesla", "Cybertruck", "USA", 116, 127),
            new Car("BMW", "X5 M", "Germany", 165, 423),
            new Car("Ferrari", "Portofino", "Italy", 215, 655),
            new Car("McLaren", "GT", "United Kingdom", 197, 698),
            new Car("Jaguar", "XE", "United Kingdom", 152, 524),
            new Car("BMW", "i8", "Germany", 168, 321),
            new Car("Nissan", "Skyline", "Japan", 167, 343),
            new Car("Bentley", "Bentayga", "United Kingdom", 198, 836),
            new Car("Aston Martin", "DB11", "United Kingdom", 197, 638),
            new Car("Chrysler", "Pacifica", "USA", 138, 190),
            new Car("Koenigsegg", "Jesko", "Sweden", 193, 625),
            new Car("Subaru", "BRZ", "Japan", 181, 526),
            new Car("McLaren", "Artura", "United Kingdom", 216, 607),
            new Car("Mercedes-Benz", "E63 AMG", "Germany", 145, 371),
            new Car("Nissan", "GT-R", "Japan", 150, 458),
            new Car("McLaren", "P1", "United Kingdom", 205, 846),
            new Car("Subaru", "Legacy", "Japan", 153, 455),
            new Car("Maserati", "Quattroporte", "Italy", 218, 630),
            new Car("Porsche", "Panamera", "Germany", 195, 794),
            new Car("Tesla", "Model 3", "USA", 110, 169),
            new Car("Volvo", "V60", "Sweden", 126, 220),
            new Car("Toyota", "Land Cruiser", "Japan", 190, 482),
            new Car("Alfa Romeo", "Stelvio", "Italy", 158, 466),
            new Car("Peugeot", "208", "France", 145, 204),
            new Car("Ford", "GT", "USA", 185, 499),
            new Car("Holden", "Commodore", "Australia", 119, 144),
            new Car("Kia", "Stinger GT", "South Korea", 158, 361),
            new Car("Hyundai", "Sonata", "South Korea", 143, 546),
            new Car("Lamborghini", "Huracan", "Italy", 213, 877),
            new Car("Renault", "Megane RS", "France", 113, 215),
            new Car("Lotus", "Exige", "United Kingdom", 160, 279),
            new Car("Mitsubishi", "Outlander", "Japan", 143, 549),
            new Car("Jaguar", "XF", "United Kingdom", 170, 507),
            new Car("Mazda", "RX-7", "Japan", 198, 521),
            new Car("Bentley", "Continental GT", "United Kingdom", 195, 629),
            new Car("Rolls-Royce", "Ghost", "United Kingdom", 220, 860),
            new Car("Honda", "Accord", "Japan", 145, 345),
            new Car("Audi", "Q7", "Germany", 144, 284),
            new Car("Lexus", "RC F", "Japan", 183, 370),
            new Car("Volkswagen", "GTI", "Germany", 165, 311),
            new Car("Hyundai", "Elantra N", "South Korea", 200, 541),
            new Car("Chevrolet", "Silverado", "USA", 155, 546),
            new Car("Dodge", "Charger SRT", "USA", 178, 270),
            new Car("Mazda", "CX-5", "Japan", 179, 291),
            new Car("McLaren", "720S", "United Kingdom", 203, 936),
            new Car("Audi", "A4", "Germany", 177, 539),
            new Car("Jaguar", "I-Pace", "United Kingdom", 173, 411),
            new Car("SEAT", "Ibiza", "Spain", 126, 146),
            new Car("Aston Martin", "Valkyrie", "United Kingdom", 211, 966),
            new Car("Honda", "CR-V", "Japan", 160, 372),
            new Car("Alfa Romeo", "4C", "Italy", 156, 452),
            new Car("Aston Martin", "DBS Superleggera", "United Kingdom", 194, 943),
            new Car("Genesis", "GV80", "South Korea", 181, 403),
            new Car("Lamborghini", "Urus", "Italy", 204, 761),
            new Car("Mitsubishi", "Lancer Evolution", "Japan", 199, 287),
            new Car("Volkswagen", "Golf R", "Germany", 140, 484),
            new Car("Bentley", "Flying Spur", "United Kingdom", 209, 888),
            new Car("Peugeot", "308", "France", 116, 129),
            new Car("Opel", "Astra", "Germany", 144, 147),
            new Car("Koenigsegg", "Agera", "Sweden", 206, 735),
            new Car("Porsche", "911 Turbo S", "Germany", 194, 778),
            new Car("Dodge", "Challenger SRT Hellcat", "USA", 196, 285),
            new Car("McLaren", "570S", "United Kingdom", 218, 725),
            new Car("Audi", "RS6 Avant", "Germany", 163, 395),
            new Car("Audi", "TT RS", "Germany", 150, 474),
            new Car("Maserati", "Levante", "Italy", 216, 878),
            new Car("Ford", "GT500", "USA", 185, 404),
            new Car("Holden", "Monaro", "Australia", 149, 203),
            new Car("Honda", "S2000", "Japan", 173, 254),
            new Car("Dodge", "Viper", "USA", 182, 533),
            new Car("Jaguar", "F-Type R", "United Kingdom", 159, 303),
            new Car("Nissan", "Silvia", "Japan", 200, 318),
            new Car("Mercedes-Benz", "AMG GT", "Germany", 156, 309),
            new Car("Jeep", "Wrangler", "USA", 116, 215),
            new Car("Ferrari", "296 GTB", "Italy", 207, 679),
            new Car("Toyota", "GR Yaris", "Japan", 157, 394),
            new Car("Maserati", "MC20", "Italy", 209, 707),
            new Car("Lexus", "IS500", "Japan", 185, 425),
            new Car("Porsche", "Macan", "Germany", 196, 951),
            new Car("Volkswagen", "Tiguan", "Germany", 180, 385),
            new Car("Ferrari", "F8 Tributo", "Italy", 206, 850),
            new Car("Subaru", "WRX STI", "Japan", 156, 276),
            new Car("Lamborghini", "Aventador", "Italy", 192, 924),
            new Car("Renault", "Alpine A110", "France", 137, 155),
            new Car("SEAT", "Leon", "Spain", 112, 120),
            new Car("Volkswagen", "Beetle", "Germany", 161, 316),
            new Car("Bugatti", "Chiron", "France", 210, 734),
            new Car("Ford", "F-150 Raptor", "USA", 150, 476),
            new Car("Genesis", "G80", "South Korea", 175, 468),
            new Car("Volkswagen", "Passat", "Germany", 175, 254),
            new Car("Peugeot", "3008", "France", 117, 129),
            new Car("Ford", "Bronco", "USA", 200, 326),
            new Car("Toyota", "GR86", "Japan", 174, 268),
            new Car("Mercedes-Benz", "G-Wagon", "Germany", 193, 439),
            new Car("Aston Martin", "Vantage", "United Kingdom", 208, 882),
            new Car("Cadillac", "Escalade-V", "USA", 119, 175),
            new Car("Fiat", "500", "Italy", 118, 125),
            new Car("Bugatti", "Divo", "France", 199, 786),
            new Car("Tesla", "Model S Plaid", "USA", 112, 165),
            new Car("Volvo", "S60", "Sweden", 123, 207),
            new Car("Skoda", "Kodiaq", "Czech Republic", 125, 205),
            new Car("Lotus", "Evora", "United Kingdom", 146, 431),
            new Car("Jeep", "Grand Cherokee Trackhawk", "USA", 145, 172),
            new Car("Cadillac", "CT5-V Blackwing", "USA", 149, 215),
            new Car("Mazda", "CX-9", "Japan", 149, 371),
            new Car("Honda", "Civic Type R", "Japan", 195, 333),
            new Car("Honda", "NSX", "Japan", 191, 340),
            new Car("Fiat", "Panda", "Italy", 136, 123),
            new Car("Renault", "Clio", "France", 121, 214),
            new Car("Mazda", "Mazda3", "Japan", 199, 420),
            new Car("Kia", "Forte", "South Korea", 190, 460),
            new Car("Subaru", "Outback", "Japan", 191, 377),
            new Car("Lexus", "LC500", "Japan", 157, 331),
            new Car("Cadillac", "CT4-V", "USA", 116, 168),
            new Car("BMW", "M3", "Germany", 195, 269),
            new Car("Mercedes-Benz", "S-Class", "Germany", 194, 490),
            new Car("Toyota", "Prius", "Japan", 154, 352),
            new Car("Subaru", "Forester", "Japan", 192, 485),
            new Car("Skoda", "Superb", "Czech Republic", 132, 159),
            new Car("Nissan", "Altima Sport", "Japan", 192, 366),
            new Car("Audi", "R8 Sport", "Germany", 154, 262),
            new Car("Ferrari", "Roma Sport", "Italy", 211, 698),
            new Car("Tesla", "Model X Sport", "USA", 135, 162),
            new Car("Rolls-Royce", "Cullinan Sport", "United Kingdom", 198, 635),
            new Car("Chevrolet", "Impala Sport", "USA", 189, 392),
            new Car("Koenigsegg", "Regera Sport", "Sweden", 201, 928),
            new Car("Genesis", "G70 Sport", "South Korea", 172, 454),
            new Car("Hyundai", "Ioniq 5 Sport", "South Korea", 183, 524),
            new Car("Kia", "Telluride Sport", "South Korea", 161, 264),
            new Car("Ford", "Focus RS Sport", "USA", 147, 383),
            new Car("Honda", "Integra Sport", "Japan", 151, 547),
            new Car("Chevrolet", "Corvette Stingray Sport", "USA", 156, 269),
            new Car("Volvo", "C40 Sport", "Sweden", 116, 196),
            new Car("Toyota", "Corolla Sport", "Japan", 167, 426),
            new Car("Skoda", "Octavia RS Sport", "Czech Republic", 130, 175),
            new Car("Dodge", "Durango Sport", "USA", 178, 511),
            new Car("Bugatti", "Veyron Sport", "France", 193, 797),
            new Car("Rolls-Royce", "Phantom Sport", "United Kingdom", 218, 895),
            new Car("Chevrolet", "Camaro ZL1 Sport", "USA", 152, 380),
            new Car("Volvo", "XC90 Sport", "Sweden", 112, 210),
            new Car("Ford", "Mustang GT Sport", "USA", 167, 250),
            new Car("Kia", "EV6 Sport", "South Korea", 173, 525),
            new Car("Toyota", "Supra Sport", "Japan", 183, 350),
            new Car("Nissan", "Leaf Sport", "Japan", 163, 470),
            new Car("BMW", "M4 Sport", "Germany", 144, 419),
            new Car("Porsche", "Taycan Sport", "Germany", 209, 760),
            new Car("SEAT", "Ateca Sport", "Spain", 117, 212),
            new Car("Mazda", "MX-5 Miata Sport", "Japan", 197, 403),
            new Car("Mitsubishi", "Eclipse Sport", "Japan", 172, 408),
            new Car("Lotus", "Emira Sport", "United Kingdom", 182, 459),
            new Car("Mercedes-Benz", "C63 AMG Sport", "Germany", 160, 456),
            new Car("Opel", "Corsa Sport", "Germany", 128, 190)
    };

    //PRINTS ALL CARS
    public List<Car> getAllCars(){
        return new ArrayList<>(Arrays.asList(cars));
    }
    //SEARCH MAKES
    public List<Car> searchByMake(String query) {
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if (car.getMake().toLowerCase().contains(query.toLowerCase())) {
                results.add(car);
            }
        }
        results.sort(Comparator.comparing(Car::getMake)); //Sorts by make alphabetically
        return results;
    }

    //SEARCH MODELS
    public List<Car> searchByModel(String query) {
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if (car.getModel().toLowerCase().contains(query.toLowerCase())) {
                results.add(car);
            }
        }
        results.sort(Comparator.comparing(Car::getModel)); //Sorts by model alphabetically
        return results;
    }

    public List<Car> searchByCountry(String query) {
        List<Car> results = new ArrayList<>();
        for (Car car : cars) {
            if (car.getCountry().toLowerCase().contains(query.toLowerCase())) {
                results.add(car);
            }
        }
        results.sort(Comparator.comparing(Car::getMake));
        return results;
    }

    public List<Car> sortByMake() {
        List<Car> list = getAllCars();
        list.sort(Comparator.comparing(Car::getMake));
        return list;
    }

    public List<Car> sortByCountry() {
        List<Car> list = getAllCars();
        list.sort(Comparator.comparing(Car::getCountry));
        return list;
    }

    public List<Car> sortByTopSpeed() {
        List<Car> list = getAllCars();
        list.sort(Comparator.comparingDouble(Car::getTopSpeed).reversed());
        return list;
    }

    public List<Car> sortByHorsepower() {
        List<Car> list = getAllCars();
        list.sort(Comparator.comparingDouble(Car::getHorsepower).reversed());
        return list;
    }
    // Checks if a car with the same make and model already exists (case-insensitive)
    public boolean carExists(String make, String model) {
        for (Car car : cars) {
            if (car.getMake().equalsIgnoreCase(make) && car.getModel().equalsIgnoreCase(model)) {
                return true;
            }
        }
        return false;
    }

    public void addCar(Car car) {
        Car[] newCars = Arrays.copyOf(cars, cars.length + 1);
        newCars[cars.length] = car;
        cars = newCars;
    }

    public boolean removeCar(Car car) {
        int index = -1;
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] == car) { // reference match, since Car doesn't override equals()
                index = i;
                break;
            }
        }
        if (index == -1) return false;

        Car[] newCars = new Car[cars.length - 1];
        int j = 0;
        for (int i = 0; i < cars.length; i++) {
            if (i != index) {
                newCars[j++] = cars[i];
            }
        }
        cars = newCars;
        return true;
    }

    public int loadFromFile(String path) throws IOException {
        List<Car> newCars = new ArrayList<>(); // collected here, then merged into the array once at the end
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 5) continue; // skip misformatted lines instead of crashing
                try {
                    String make = parts[0].trim();
                    String model = parts[1].trim();
                    String country = parts[2].trim();
                    int topSpeed = Integer.parseInt(parts[3].trim());
                    int horsepower = Integer.parseInt(parts[4].trim());
                    Car car = new Car(make, model, country, topSpeed, horsepower);
                    if (car.validate()) {
                        newCars.add(car);
                    }
                } catch (NumberFormatException e) {
                    // skip misformatted line rather than crashing the whole load
                }
            }
        }

        if (!newCars.isEmpty()) {
            Car[] combined = Arrays.copyOf(cars, cars.length + newCars.size());
            for (int i = 0; i < newCars.size(); i++) {
                combined[cars.length + i] = newCars.get(i); // adds to database
            }
            cars = combined;
        }

        return newCars.size();
    }
}



