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
 * Phase 4: GUI front end for Car Wiki, backed by CarDatabase (MySQL via JDBC).
 * The user supplies their own server address/username/password at startup -
 * nothing about their database access is hardcoded in this file.
 */
public class CWUserInterface extends JFrame {

    private final CarDatabase database;
    private final List<Car> wishlist = new ArrayList<>();
    private List<Car> currentResults = new ArrayList<>();
    private boolean showingWishlist = false;

    private JComboBox<String> attributeBox;
    private JTextField searchField;
    private JPanel extraSearchRowsPanel;
    private final List<JComboBox<String>> extraAttributeBoxes = new ArrayList<>();
    private final List<JTextField> extraValueFields = new ArrayList<>();

    private JTable table;
    private DefaultTableModel tableModel;

    private JPanel spotlightPanel;
    private JLabel spotlightTitle;
    private JLabel spotlightDetails;

    private JToggleButton viewWishlistToggle;
    private JButton wishlistRemoveButton;

    /**
     * Constructs the Car Wiki main window, wires up the top/center/bottom panels,
     * and runs an initial search so the table is populated as soon as the window opens.
     *
     * @param database the CarDatabase instance (already connected via JDBC) that this
     *                 UI will read from and write to for all search/CRUD operations
     */
    public CWUserInterface(CarDatabase database) {
        this.database = database;
        setTitle("Car Wiki");
        setSize(850, 630);
        setMinimumSize(new Dimension(500, 400)); // keeps the layout usable even if shrunk aggressively
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(217, 217, 217)); // grey body

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        JScrollPane bottomScroll = new JScrollPane(buildBottomPanel());
        bottomScroll.setBorder(BorderFactory.createEmptyBorder());
        bottomScroll.getVerticalScrollBar().setUnitIncrement(16);
        bottomScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bottomScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(bottomScroll, BorderLayout.SOUTH);

        performSearch();
    }

    /**
     * Builds the top section of the window: the title, the primary
     * search bar (attribute dropdown, value field, Search button, "+" button), and the
     * container panel that holds any chained filter rows added via the "+" button.
     *
     * @return a fully assembled JPanel ready to be added to the frame's NORTH region
     */
    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(217, 217, 217));

        JLabel title = new JLabel("Car Wiki");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchBar.setBackground(new Color(217, 119, 46)); // orange per mockup
        searchBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Attribute selector matches CarDatabase's real methods: searchByMake / searchByModel / searchByCountry
        attributeBox = new JComboBox<>(new String[]{"Make", "Model", "Country"});
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton plusButton = new JButton("+");
        plusButton.setToolTipText("Add another filter");

        searchBar.add(new JLabel("Search by:"));
        searchBar.add(attributeBox);
        searchBar.add(searchField);
        searchBar.add(searchButton);
        searchBar.add(plusButton);

        extraSearchRowsPanel = new JPanel();
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
            extraSearchRowsPanel.remove(row);
            extraAttributeBoxes.remove(rowAttributeBox);
            extraValueFields.remove(valueField);
            extraSearchRowsPanel.revalidate();
            extraSearchRowsPanel.repaint();
            performSearch();
        });

        extraAttributeBoxes.add(rowAttributeBox);
        extraValueFields.add(valueField);
        extraSearchRowsPanel.add(row);
        extraSearchRowsPanel.revalidate();
        extraSearchRowsPanel.repaint();
    }

    /**
     * Builds the center section of the window: the single-result "spotlight" feature stacked above
     * the scrollable, sortable results table.
     *
     * @return a fully assembled JPanel ready to be added to the frame's CENTER region
     */
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
        spotlightPanel.setVisible(false);

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

    /**
     * Builds the bottom section of the window: the file-load row, the CRUD button row
     * (Show All / Add / Edit / Delete), and the wishlist button row, and wires every
     * button's action listener to the appropriate CarDatabase or wishlist operation.
     *
     * @return a fully assembled JPanel (meant to be wrapped in a JScrollPane by the
     * caller) ready to be added to the frame's SOUTH region
     */
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
                int skipped = database.getLastSkippedDuplicates();
                String message = "Loaded " + count + " car(s) from file.";
                if (skipped > 0) {
                    message += " Skipped " + skipped + " duplicate(s) already in the database.";
                }
                showMessage(message, JOptionPane.INFORMATION_MESSAGE);
                performSearch();
            } catch (IOException ex) {
                showMessage("Could not read file: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });

        showAllButton.addActionListener(e -> {
            showingWishlist = false;
            viewWishlistToggle.setSelected(false);
            wishlistRemoveButton.setEnabled(false);
            searchField.setText("");
            clearExtraSearchRows();
            currentResults = database.sortByMake();
            refreshTable();
        });

        addButton.addActionListener(e -> {
            Car newCar = showCarDialog("Add New Car", null);
            if (newCar != null) {
                if (database.carExists(newCar.getMake(), newCar.getModel())) {
                    showMessage(newCar.getMake() + " " + newCar.getModel() + " already exists in the database.",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean success = database.addCar(newCar);
                if (success) {
                    performSearch();
                } else {
                    showMessage("Could not add that car to the database.", JOptionPane.ERROR_MESSAGE);
                }
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
                boolean success = database.updateCar(selected, updated);
                if (success) {
                    performSearch();
                } else {
                    showMessage("Could not update that car in the database.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            Car selected = getSelectedCar();
            if (selected == null) {
                showMessage("Select a car in the table first.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            database.removeCar(selected);
            wishlist.remove(selected);
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
            boolean removed = wishlist.remove(selected);
            showMessage(removed ? selected.getModel() + " removed from wishlist."
                            : selected.getModel() + " isn't in your wishlist.",
                    JOptionPane.INFORMATION_MESSAGE);
            if (showingWishlist) {
                performSearch();
            }
        });

        viewWishlistToggle.addActionListener(e -> {
            showingWishlist = viewWishlistToggle.isSelected();
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

    /**
     * Maps the currently selected row in the (possibly sorted) JTable back to the
     * Car object it represents by converting the view row index to a model row
     * index and looking that index up in currentResults.
     *
     * @return the Car corresponding to the selected table row, or null if no row
     *         is selected or the selection is out of range
     */
    private Car getSelectedCar() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) return null;
        int modelRow = table.convertRowIndexToModel(viewRow);
        if (modelRow < 0 || modelRow >= currentResults.size()) return null;
        return currentResults.get(modelRow);
    }

    // Runs the base search using CarDatabase's real search methods, then applies
    // each extra filter row (AND) locally, then refreshes the table + spotlight.
    private void performSearch() {
        List<Car> source;

        if (showingWishlist) {
            source = new ArrayList<>(wishlist);
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
                if (!rowValue.isEmpty()) {
                    source = filterByAttribute(source, rowAttribute, rowValue);
                }
            }
        }

        currentResults = source;
        refreshTable();
    }

    /**
     * Locally filters a list of cars down to those whose given attribute value
     * contains the given search text (case-insensitive). Used only for the
     * chained "+" filter rows, so CarDatabase itself doesn't need a matching
     * method for every attribute combination.
     *
     * @param source    the list of cars to filter (typically the result of a
     *                  prior CarDatabase search or a previous filter pass)
     * @param attribute which field to match against; expected values are
     *                  "Make", "Model", or "Country" (anything else falls back
     *                  to matching against Make)
     * @param value     the text to search for within the selected attribute
     * @return a new list containing only the cars from source whose selected
     *         attribute contains value, ignoring case
     */
    private List<Car> filterByAttribute(List<Car> source, String attribute, String value) {
        List<Car> results = new ArrayList<>();
        String v = value.toLowerCase();
        for (Car car : source) {
            String fieldValue;
            switch (attribute) {
                case "Model": fieldValue = car.getModel(); break;
                case "Country": fieldValue = car.getCountry(); break;
                default: fieldValue = car.getMake();
            }
            if (fieldValue.toLowerCase().contains(v)) {
                results.add(car);
            }
        }
        return results;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Car car : currentResults) {
            tableModel.addRow(new Object[]{
                    car.getMake(), car.getModel(), car.getCountry(),
                    car.getTopSpeed(), car.getHorsepower()
            });
        }

        if (currentResults.size() == 1) {
            Car car = currentResults.get(0);
            spotlightTitle.setText(car.getMake() + " " + car.getModel());
            spotlightDetails.setText("<html>Country: " + car.getCountry()
                    + " &nbsp;|&nbsp; Top Speed: " + (int) car.getTopSpeed() + " mph"
                    + " &nbsp;|&nbsp; Horsepower: " + (int) car.getHorsepower() + " hp</html>");
            spotlightPanel.setVisible(true);
        } else {
            spotlightPanel.setVisible(false);
        }
    }

    /**
     * Opens a modal form dialog for adding a new car or editing an existing one.
     * Fields are pre-filled from existing when provided. Top speed and horsepower
     * are parsed as int (matching Car's fields), and the resulting Car is checked
     * with Car.validate() before being returned. Any parse failure or validation
     * failure is reported to the user via a message dialog rather than thrown.
     *
     * @param title    the dialog window's title (e.g. "Add New Car" or "Edit Car")
     * @param existing the Car whose values should pre-fill the form for editing,
     *                 or null to start from a blank form for adding a new car
     * @return a new Car built from the form's input if the user clicked OK and
     *         the input was valid (non-empty fields, whole-number top speed and
     *         horsepower, and Car.validate() passing); null if the user cancelled
     *         the dialog, entered non-numeric top speed/horsepower, or the
     *         resulting Car failed validation
     */
    private Car showCarDialog(String title, Car existing) {
        JTextField makeField = new JTextField(existing != null ? existing.getMake() : "");
        JTextField modelField = new JTextField(existing != null ? existing.getModel() : "");
        JTextField countryField = new JTextField(existing != null ? existing.getCountry() : "");
        JTextField topSpeedField = new JTextField(existing != null ? String.valueOf((int) existing.getTopSpeed()) : "");
        JTextField horsepowerField = new JTextField(existing != null ? String.valueOf((int) existing.getHorsepower()) : "");

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 8, 8));
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

        int result = JOptionPane.showConfirmDialog(this, formPanel, title,
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
            topSpeed = Integer.parseInt(topSpeedField.getText().trim());
            horsepower = Integer.parseInt(horsepowerField.getText().trim());
        } catch (NumberFormatException e) {
            showMessage("Top speed and horsepower must be whole numbers.", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Car car = new Car(make, model, country, topSpeed, horsepower);
        if (!car.validate()) {
            showMessage("All fields are required, and top speed/horsepower must be greater than 0.",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return car;
    }

    /**
     * Displays a simple modal message dialog to the user, titled "Car Wiki".
     *
     * @param message     the text to display in the dialog body
     * @param messageType the JOptionPane message type constant controlling the
     *                    dialog's icon (e.g. JOptionPane.INFORMATION_MESSAGE,
     *                    JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE)
     */
    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Car Wiki", messageType);
    }
}