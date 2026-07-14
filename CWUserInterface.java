import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Joseph Nunez
 * CEN 3024C - Software Development 1
 * CWUserInterface.java
 * GUI front end for Car Wiki, replacing CLI
 */
public class CWUserInterface extends JFrame {

    private final CarDatabase database = new CarDatabase();
    private final List<Car> wishlist = new ArrayList<>(); // separate from database, just holds references to favorited cars
    private List<Car> currentResults = new ArrayList<>(); // whatever's currently in the table - search results OR wishlist
    private boolean showingWishlist = false; // performSearch() pulls from wishlist instead of the database if true

    private JComboBox<String> attributeBox;
    private JTextField searchField;
    private JPanel extraSearchRowsPanel;
    private final List<JComboBox<String>> extraAttributeBoxes = new ArrayList<>(); // parallel list w/ extraValueFields
    private final List<JTextField> extraValueFields = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JPanel spotlightPanel; // only visible when search narrows to exactly 1 car
    private JLabel spotlightTitle;
    private JLabel spotlightDetails;

    private JToggleButton viewWishlistToggle;
    private JButton wishlistRemoveButton;

    //Generates the base for the GUI
    public CWUserInterface() {
        setTitle("Car Wiki");
        setSize(850, 630);
        setMinimumSize(new Dimension(500, 400)); // Keeps the layout usable at any window size
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ends process if close is selected
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(217, 217, 217)); // Sets background grey

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        JScrollPane bottomScroll = new JScrollPane(buildBottomPanel()); // Creates Scroll function to see multiple listings at once
        bottomScroll.setBorder(BorderFactory.createEmptyBorder());
        bottomScroll.getVerticalScrollBar().setUnitIncrement(16);
        bottomScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bottomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(bottomScroll, BorderLayout.SOUTH);

        performSearch(); // runs on startup so table isn't empty when window opens
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(217, 217, 217));

        JLabel title = new JLabel("Car Wiki");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBar.setBackground(new Color(217, 119, 46));
        searchBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Attribute selector matches CarDatabase's real methods: searchByMake / searchByModel / searchByCountry
        attributeBox = new JComboBox<>(new String[]{"Make", "Model", "Country"});
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton plusButton = new JButton("+");
        plusButton.setToolTipText("Add another filter");

        searchBar.add(new JLabel("Search by:"));
        searchBar.add(attributeBox); // Allows searching by categories
        searchBar.add(searchField);
        searchBar.add(searchButton);
        searchBar.add(plusButton);

        extraSearchRowsPanel = new JPanel(); //Allows for multiple queries
        extraSearchRowsPanel.setLayout(new BoxLayout(extraSearchRowsPanel, BoxLayout.Y_AXIS));
        extraSearchRowsPanel.setBackground(new Color(217, 217, 217));
        extraSearchRowsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch()); // Enter key triggers search
        plusButton.addActionListener(e -> addExtraSearchRow());

        topPanel.add(title);
        topPanel.add(searchBar);
        topPanel.add(extraSearchRowsPanel);

        return topPanel;
    }

    // Adds a chained "attribute + value" filter row under the main search bar
    private void addExtraSearchRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row.setBackground(new Color(217, 217, 217));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> rowAttributeBox = new JComboBox<>(new String[]{"Make", "Model", "Country"});
        JTextField valueField = new JTextField(15);
        JButton removeRowButton = new JButton("x");

        row.add(rowAttributeBox);
        row.add(valueField);
        row.add(removeRowButton);

        rowAttributeBox.addActionListener(e -> performSearch());
        valueField.addActionListener(e -> performSearch());
        removeRowButton.addActionListener(e -> {
            extraSearchRowsPanel.remove(row); // remove from panel...
            extraAttributeBoxes.remove(rowAttributeBox); // ...and from tracking lists, or performSearch() will still read this row
            extraValueFields.remove(valueField);
            extraSearchRowsPanel.revalidate(); // recalculates layout after removing a component
            extraSearchRowsPanel.repaint(); // actually redraws it
            performSearch();
        });

        extraAttributeBoxes.add(rowAttributeBox);
        extraValueFields.add(valueField);
        extraSearchRowsPanel.add(row);
        extraSearchRowsPanel.revalidate(); // needed after adding a component at runtime or it won't show up
        extraSearchRowsPanel.repaint();
    }

    // Car spotlight (single result) + results table
    private JPanel buildCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(217, 217, 217));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        spotlightTitle = new JLabel();
        spotlightTitle.setFont(new Font("Arial", Font.BOLD, 18));
        spotlightDetails = new JLabel();

        JPanel spotlightText = new JPanel();
        spotlightText.setLayout(new BoxLayout(spotlightText, BoxLayout.Y_AXIS));
        spotlightText.setBackground(new Color(217, 217, 217));
        spotlightText.add(spotlightTitle);
        spotlightText.add(spotlightDetails);

        // Note: no image/icon here for now — plug a car photo into this panel later
        spotlightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        spotlightPanel.setBackground(new Color(217, 217, 217));
        spotlightPanel.add(spotlightText);
        spotlightPanel.setVisible(false); // hidden until refreshTable() finds exactly 1 result

        tableModel = new DefaultTableModel(
                new Object[]{"Make", "Model", "Country", "Top Speed", "Horsepower"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // edits go through the Edit Selected dialog, not inline
            }
        };
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true); // click column headers to sort
        JScrollPane scrollPane = new JScrollPane(table);

        centerPanel.add(spotlightPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        return centerPanel;
    }

    // File load + CRUD + wishlist actions
    private JPanel buildBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(217, 217, 217));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filePanel.setBackground(new Color(217, 217, 217));
        JTextField filePathField = new JTextField(30);
        JButton loadButton = new JButton("Load File");
        filePanel.add(new JLabel("File path:"));
        filePanel.add(filePathField);
        filePanel.add(loadButton);

        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        crudPanel.setBackground(new Color(217, 217, 217));
        JButton showAllButton = new JButton("Show All");
        JButton addButton = new JButton("Add Record");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");

        crudPanel.add(showAllButton);
        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);

        JPanel wishlistPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        wishlistPanel.setBackground(new Color(217, 217, 217));
        JButton wishlistAddButton = new JButton("Add to Wishlist");
        wishlistRemoveButton = new JButton("Remove from Wishlist");
        wishlistRemoveButton.setEnabled(false); // only usable while viewing the wishlist
        viewWishlistToggle = new JToggleButton("View Wishlist");

        wishlistPanel.add(wishlistAddButton);
        wishlistPanel.add(wishlistRemoveButton);
        wishlistPanel.add(viewWishlistToggle);

        bottomPanel.add(filePanel);
        bottomPanel.add(crudPanel);
        bottomPanel.add(wishlistPanel);

        loadButton.addActionListener(e -> {
            String path = filePathField.getText().trim();
            if (path.isEmpty()) {
                showMessage("Please enter a file path first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int count = database.loadFromFile(path);
                showMessage("Loaded " + count + " car(s) from file.", JOptionPane.INFORMATION_MESSAGE);
                performSearch();
            } catch (IOException ex) {
                showMessage("Could not read file: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });

        showAllButton.addActionListener(e -> {
            showingWishlist = false; // exit wishlist mode
            viewWishlistToggle.setSelected(false); // un-press the toggle visually
            wishlistRemoveButton.setEnabled(false);
            searchField.setText("");
            clearExtraSearchRows();
            currentResults = database.sortByMake();
            refreshTable();
        });

        addButton.addActionListener(e -> {
            Car newCar = showCarDialog("Add New Car", null); // blank form if null
            if (newCar != null) {
                if (database.carExists(newCar.getMake(), newCar.getModel())) { // stop duplicate make/model combos
                    showMessage(newCar.getMake() + " " + newCar.getModel() + " already exists in the database.",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                database.addCar(newCar);
                performSearch();
            }
        });

        editButton.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected == null) {
                showMessage("Select a car in the table first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Car updated = showCarDialog("Edit Car", selected);
            if (updated != null) {
                selected.setMake(updated.getMake());
                selected.setModel(updated.getModel());
                selected.setCountry(updated.getCountry());
                selected.setTopSpeed((int) updated.getTopSpeed());
                selected.setHorsepower((int) updated.getHorsepower());
                performSearch();
            }
        });

        deleteButton.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected == null) {
                showMessage("Select a car in the table first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            database.removeCar(selected);
            wishlist.remove(selected); // drop car from the wishlist
            performSearch();
        });

        wishlistAddButton.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected == null) {
                showMessage("Select a car in the table first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!wishlist.contains(selected)) {
                wishlist.add(selected);
                showMessage(selected.getModel() + " added to wishlist.", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessage(selected.getModel() + " is already in your wishlist.", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        wishlistRemoveButton.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected == null) {
                showMessage("Select a car in the table first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean removed = wishlist.remove(selected); // List.remove returns whether it actually found/removed something
            showMessage(removed ? selected.getModel() + " removed from wishlist."
                            : selected.getModel() + " isn't in your wishlist.",
                    JOptionPane.INFORMATION_MESSAGE);
            if (showingWishlist) {
                performSearch(); // refresh so the removed car disappears right away
            }
        });

        viewWishlistToggle.addActionListener(e -> {
            showingWishlist = viewWishlistToggle.isSelected(); // true when the button is pressed
            wishlistRemoveButton.setEnabled(showingWishlist);
            performSearch();
        });

        return bottomPanel;
    }

    private void clearExtraSearchRows() {
        extraSearchRowsPanel.removeAll();
        extraAttributeBoxes.clear();
        extraValueFields.clear();
        extraSearchRowsPanel.revalidate();
        extraSearchRowsPanel.repaint();
    }

    // Maps the table's selected row back to the Car object it represents
    private Car getSelectedCar() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return null; // -1 means nothing selected
        int modelRow = table.convertRowIndexToModel(viewRow); // table is sortable, so visible row != underlying data row
        if (modelRow < 0 || modelRow >= currentResults.size()) return null;
        return currentResults.get(modelRow);
    }

    // Runs the base search using CarDatabase's real search methods
    private void performSearch() {
        List<Car> source;

        if (showingWishlist) {
            source = new ArrayList<>(wishlist); // copy so filtering below never mutates the real wishlist
        } else {
            String query = searchField.getText().trim();
            String attribute = (String) attributeBox.getSelectedItem();
            if (query.isEmpty()) {
                source = database.getAllCars();
            } else if ("Model".equals(attribute)) {
                source = database.searchByModel(query);
            } else if ("Country".equals(attribute)) {
                source = database.searchByCountry(query);
            } else {
                source = database.searchByMake(query);
            }

            for (int i = 0; i < extraAttributeBoxes.size(); i++) {
                String rowAttribute = (String) extraAttributeBoxes.get(i).getSelectedItem();
                String rowValue = extraValueFields.get(i).getText().trim();
                if (!rowValue.isEmpty()) { // blank rows shouldn't filter anything out
                    source = filterByAttribute(source, rowAttribute, rowValue);
                }
            }
        }

        currentResults = source;
        refreshTable();
    }

    // Local AND-filter used only for the chained "+" rows
    private List<Car> filterByAttribute(List<Car> source, String attribute, String value) {
        List<Car> results = new ArrayList<>();
        String v = value.toLowerCase();
        for (Car car : source) {
            String fieldValue;
            switch (attribute) {
                case "Model":
                    fieldValue = car.getModel();
                    break;
                case "Country":
                    fieldValue = car.getCountry();
                    break;
                default:
                    fieldValue = car.getMake();
            }
            if (fieldValue.toLowerCase().contains(v)) { // "contains" so partial text matches too
                results.add(car);
            }
        }
        return results;
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // clear old rows first to avoid stacking
        for (Car car : currentResults) {
            tableModel.addRow(new Object[]{
                    car.getMake(), car.getModel(), car.getCountry(),
                    car.getTopSpeed(), car.getHorsepower()
            });
        }

        if (currentResults.size() == 1) { // only show spotlight when exactly one car matches
            Car car = currentResults.get(0);
            spotlightTitle.setText(car.getMake() + " " + car.getModel());
            spotlightDetails.setText("<html>Country: " + car.getCountry() // html tag controls spacing in the label
                    + " &nbsp;|&nbsp; Top Speed: " + (int) car.getTopSpeed() + " mph"
                    + " &nbsp;|&nbsp; Horsepower: " + (int) car.getHorsepower() + " hp</html>");
            spotlightPanel.setVisible(true);
        } else {
            spotlightPanel.setVisible(false);
        }
    }

    // Opens a form dialog for adding or editing a Car; returns null if cancelled or invalid.
    private Car showCarDialog(String title, Car existing) {
        JTextField makeField = new JTextField(existing != null ? existing.getMake() : ""); // pre-fill when editing, blank when adding
        JTextField modelField = new JTextField(existing != null ? existing.getModel() : "");
        JTextField countryField = new JTextField(existing != null ? existing.getCountry() : "");
        JTextField topSpeedField = new JTextField(existing != null ? String.valueOf((int) existing.getTopSpeed()) : "");
        JTextField horsepowerField = new JTextField(existing != null ? String.valueOf((int) existing.getHorsepower()) : "");

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8)); // 5 rows, 2 cols: label next to field
        formPanel.add(new JLabel("Make:"));
        formPanel.add(makeField);
        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);
        formPanel.add(new JLabel("Top Speed (mph):"));
        formPanel.add(topSpeedField);
        formPanel.add(new JLabel("Horsepower:"));
        formPanel.add(horsepowerField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, title, // blocks here until user clicks OK/Cancel
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return null; // user cancelled
        }

        String make = makeField.getText().trim();
        String model = modelField.getText().trim();
        String country = countryField.getText().trim();

        int topSpeed;
        int horsepower;
        try {
            topSpeed = Integer.parseInt(topSpeedField.getText().trim()); // non-numeric text error handling
            horsepower = Integer.parseInt(horsepowerField.getText().trim());
        } catch (NumberFormatException e) {
            showMessage("Top speed and horsepower must be whole numbers.", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Car car = new Car(make, model, country, topSpeed, horsepower);
        if (!car.validate()) { // catches empty fields/numbers <= 0 that parseInt wouldn't catch
            showMessage("All fields are required, and top speed/horsepower must be greater than 0.",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return car;
    }

    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Car Wiki", messageType);
    }
}