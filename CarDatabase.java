import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * CarDatabase.java
 * Phase 4: this class no longer keeps cars in memory. Every method
 * runs a real SQL query/update against a MySQL database. The server
 * address, username, and password are supplied by whoever constructs
 * this class (see CWUserInterface's startup prompt) - never hardcoded here.
 *
 * Expects a database named "car_wiki" with a table "cars":
 *   id INT AUTO_INCREMENT PRIMARY KEY,
 *   make VARCHAR(50), model VARCHAR(100), country VARCHAR(50),
 *   topspeed INT, horsepower INT,
 *   UNIQUE (make, model)
 * See car_wiki_mysql_seed.sql for the full creation script.
 */
public class CarDatabase {

    private final String host;
    private final String username;
    private final String password;

    // Tests the connection immediately so the caller finds out right away
    // if the server address/username/password don't work.
    public CarDatabase(String host, String username, String password) throws SQLException {
        this.host = host;
        this.username = username;
        this.password = password;
        try (Connection conn = getConnection()) {
            // connection succeeded; nothing else to do here
        }
    }

    private Connection getConnection() throws SQLException { // connects to database shared
        String url = "jdbc:mysql://" + host + "/CarDatabase";
        return DriverManager.getConnection(url, username, password);
    }

    private Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
                rs.getString("make"),
                rs.getString("model"),
                rs.getString("country"),
                rs.getInt("topspeed"),
                rs.getInt("horsepower")
        );
    }

    // Runs a SELECT with one optional string parameter
    private List<Car> runQuery(String sql, String likeParam) {
        List<Car> results = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (likeParam != null) {
                stmt.setString(1, "%" + likeParam + "%");
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // caller still gets a empty list instead of crashing
        }
        return results;
    }

    public List<Car> getAllCars() {
        return runQuery("SELECT * FROM cars", null);
    }

    public List<Car> searchByMake(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(make) LIKE LOWER(?) ORDER BY make", query);
    }

    public List<Car> searchByModel(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(model) LIKE LOWER(?) ORDER BY model", query);
    }

    public List<Car> searchByCountry(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(country) LIKE LOWER(?) ORDER BY make", query);
    }

    public List<Car> sortByMake() {
        return runQuery("SELECT * FROM cars ORDER BY make", null);
    }

    public List<Car> sortByCountry() {
        return runQuery("SELECT * FROM cars ORDER BY country", null);
    }

    public List<Car> sortByTopSpeed() {
        return runQuery("SELECT * FROM cars ORDER BY topspeed DESC", null);
    }

    public List<Car> sortByHorsepower() {
        return runQuery("SELECT * FROM cars ORDER BY horsepower DESC", null);
    }

    // Checks if a car with the same make and model already exists (case-insensitive)
    public boolean carExists(String make, String model) {
        String sql = "SELECT COUNT(*) FROM cars WHERE LOWER(make) = LOWER(?) AND LOWER(model) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, make);
            stmt.setString(2, model);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (make, model, country, topspeed, horsepower) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getMake()); // assigns to proper attribute
            stmt.setString(2, car.getModel());
            stmt.setString(3, car.getCountry());
            stmt.setInt(4, (int) car.getTopSpeed());
            stmt.setInt(5, (int) car.getHorsepower());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCar(Car car) {
        String sql = "DELETE FROM cars WHERE LOWER(make) = LOWER(?) AND LOWER(model) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getMake()); // removes attributes and item from table
            stmt.setString(2, car.getModel());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Locates the row by the original make+model, then writes the updated values over it.
    public boolean updateCar(Car original, Car updated) {
        String sql = "UPDATE cars SET make = ?, model = ?, country = ?, topspeed = ?, horsepower = ? " +
                "WHERE LOWER(make) = LOWER(?) AND LOWER(model) = LOWER(?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, updated.getMake());
            stmt.setString(2, updated.getModel());
            stmt.setString(3, updated.getCountry());
            stmt.setInt(4, (int) updated.getTopSpeed());
            stmt.setInt(5, (int) updated.getHorsepower());
            stmt.setString(6, original.getMake());
            stmt.setString(7, original.getModel());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int loadFromFile(String path) throws IOException {
        int loaded = 0;
        int skipped = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 5) continue; // skip malformed lines instead of crashing
                try {
                    String make = parts[0].trim();
                    String model = parts[1].trim();
                    String country = parts[2].trim();
                    int topSpeed = Integer.parseInt(parts[3].trim());
                    int horsepower = Integer.parseInt(parts[4].trim());
                    Car car = new Car(make, model, country, topSpeed, horsepower);
                    if (!car.validate()) {
                        continue;
                    }
                    if (carExists(make, model)) {
                        skipped++;
                        continue;
                    }
                    if (addCar(car)) {
                        loaded++;
                    }
                } catch (NumberFormatException e) {
                    // skip this malformed line rather than crashing the whole load
                }
            }
        }
        lastSkippedDuplicates = skipped;
        return loaded;
    }

    private int lastSkippedDuplicates = 0;

    public int getLastSkippedDuplicates() {
        return lastSkippedDuplicates;
    }
}