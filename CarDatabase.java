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
 */
public class CarDatabase {

    private final String host;
    private final String username;
    private final String password;

    /**
     * Constructs a CarDatabase bound to the given server, and immediately opens
     * and closes a test connection so the caller finds out right away if the
     * server address, username, or password don't work, rather than discovering
     * the problem later on the first real query.
     *
     * @param host     the database server address (e.g. "localhost:3306" or a
     *                 remote host), used to build the JDBC connection URL
     * @param username the MySQL username to authenticate with
     * @param password the MySQL password to authenticate with
     * @throws SQLException if a connection cannot be established with the given
     *                       host, username, and password
     */
    public CarDatabase(String host, String username, String password) throws SQLException {
        this.host = host;
        this.username = username;
        this.password = password;
        try (Connection conn = getConnection()) {
            // connection succeeded; nothing else to do here
        }
    }

    /**
     * Opens a new JDBC connection to the shared "CarDatabase" schema on this
     * instance's host, using the username/password supplied at construction.
     *
     * @return an open Connection to the CarDatabase schema
     * @throws SQLException if the connection attempt fails (e.g. bad
     *                       credentials, unreachable host, or server down)
     */
    private Connection getConnection() throws SQLException { // connects to database shared
        String url = "jdbc:mysql://" + host + "/CarDatabase";
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Converts the current row of a ResultSet from the cars table into a Car object.
     *
     * @param rs the ResultSet positioned (via rs.next()) on the row to read; must
     *           contain "make", "model", "country", "topspeed", and "horsepower" columns
     * @return a new Car built from the current row's column values
     * @throws SQLException if any column can't be read from the current row
     */
    private Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
                rs.getString("make"),
                rs.getString("model"),
                rs.getString("country"),
                rs.getInt("topspeed"),
                rs.getInt("horsepower")
        );
    }

    /**
     * Runs a SELECT query with at most one optional string parameter bound as a
     * "%value%" LIKE pattern, and maps every returned row into a Car. Any
     * SQLException is caught and logged rather than thrown, so the caller always
     * gets back a list (empty on failure) instead of the application crashing.
     *
     * @param sql       the SQL SELECT statement to run; if it contains a "?"
     *                  placeholder, likeParam is bound to it
     * @param likeParam the value to wrap in "%...%" and bind to the query's "?"
     *                  placeholder, or null if the query has no parameter to bind
     * @return a list of Car objects mapped from the query results; empty if no
     *         rows matched or if a database error occurred
     */
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

    /**
     * Retrieves every car currently stored in the database, in whatever order
     * the database returns them (no ORDER BY applied).
     *
     * @return a list of all cars in the database; empty if the table is empty
     *         or a database error occurred
     */
    public List<Car> getAllCars() {
        return runQuery("SELECT * FROM cars", null);
    }

    /**
     * Searches for cars whose make contains the given text (case-insensitive),
     * sorted alphabetically by make.
     *
     * @param query the text to search for within each car's make
     * @return a list of matching cars ordered by make; empty if none matched
     *         or a database error occurred
     */
    public List<Car> searchByMake(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(make) LIKE LOWER(?) ORDER BY make", query);
    }

    /**
     * Searches for cars whose model contains the given text (case-insensitive),
     * sorted alphabetically by model.
     *
     * @param query the text to search for within each car's model
     * @return a list of matching cars ordered by model; empty if none matched
     *         or a database error occurred
     */
    public List<Car> searchByModel(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(model) LIKE LOWER(?) ORDER BY model", query);
    }

    /**
     * Searches for cars whose country contains the given text (case-insensitive),
     * sorted alphabetically by make.
     *
     * @param query the text to search for within each car's country
     * @return a list of matching cars ordered by make; empty if none matched
     *         or a database error occurred
     */
    public List<Car> searchByCountry(String query) {
        return runQuery("SELECT * FROM cars WHERE LOWER(country) LIKE LOWER(?) ORDER BY make", query);
    }

    /**
     * Retrieves every car in the database sorted alphabetically by make.
     *
     * @return all cars ordered by make; empty if the table is empty or a
     *         database error occurred
     */
    public List<Car> sortByMake() {
        return runQuery("SELECT * FROM cars ORDER BY make", null);
    }

    /**
     * Retrieves every car in the database sorted alphabetically by country.
     *
     * @return all cars ordered by country; empty if the table is empty or a
     *         database error occurred
     */
    public List<Car> sortByCountry() {
        return runQuery("SELECT * FROM cars ORDER BY country", null);
    }

    /**
     * Retrieves every car in the database sorted by top speed, fastest first.
     *
     * @return all cars ordered by top speed descending; empty if the table is
     *         empty or a database error occurred
     */
    public List<Car> sortByTopSpeed() {
        return runQuery("SELECT * FROM cars ORDER BY topspeed DESC", null);
    }

    /**
     * Retrieves every car in the database sorted by horsepower, highest first.
     *
     * @return all cars ordered by horsepower descending; empty if the table is
     *         empty or a database error occurred
     */
    public List<Car> sortByHorsepower() {
        return runQuery("SELECT * FROM cars ORDER BY horsepower DESC", null);
    }

    /**
     * Checks whether a car with the same make and model (case-insensitive)
     * already exists in the database.
     *
     * @param make  the make to check for
     * @param model the model to check for
     * @return true if a car with a matching make and model exists; false if no
     *         match was found or a database error occurred
     */
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

    /**
     * Inserts a new car row into the database using the given Car's fields.
     *
     * @param car the car to insert; its make, model, country, top speed, and
     *            horsepower are written to the cars table
     * @return true if the insert succeeded; false if a database error occurred
     */
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

    /**
     * Deletes the row matching the given car's make and model (case-insensitive).
     *
     * @param car the car to remove, identified by its make and model
     * @return true if a row was deleted; false if no matching row was found or
     *         a database error occurred
     */
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

    /**
     * Locates the row matching the original car's make and model, then
     * overwrites every column on that row with the updated car's values.
     *
     * @param original the car whose make and model identify the row to update
     * @param updated  the car whose make, model, country, top speed, and
     *                 horsepower values should be written over that row
     * @return true if a row was updated; false if no matching row was found or
     *         a database error occurred
     */
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

    /**
     * Reads a comma-separated file of cars (make,model,country,topspeed,horsepower
     * per line) and adds each valid, non-duplicate car to the database. Lines that
     * are blank, malformed (wrong number of fields, non-numeric top speed or
     * horsepower), or fail Car.validate() are silently skipped rather than
     * aborting the whole load; lines whose make+model already exist in the
     * database are counted as skipped duplicates (retrievable afterward via
     * getLastSkippedDuplicates()) rather than re-inserted.
     *
     * @param path the file system path to the comma-separated car data file
     * @return the number of cars successfully loaded and added to the database
     * @throws IOException if the file at path cannot be opened or read
     */
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

    /**
     * Returns how many duplicate cars (by make+model) were skipped during the
     * most recent call to loadFromFile(String).
     *
     * @return the number of duplicate rows skipped on the last file load, or 0
     *         if loadFromFile has not been called yet
     */
    public int getLastSkippedDuplicates() {
        return lastSkippedDuplicates;
    }
}