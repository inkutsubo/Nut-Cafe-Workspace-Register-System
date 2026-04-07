package com.mycompany.java_maven_project;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CoffeeController {
    private final ObservableList<Subscriber> masterData = FXCollections.observableArrayList();
    private final FilteredList<Subscriber> filteredData = new FilteredList<>(masterData, p -> true);
    private final DoubleProperty totalRevenue = new SimpleDoubleProperty(0.0);
    
    private static final String DATA_FILE = "nutcafe_subs.csv";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,6}$");

    // Configurable Pricing
    private final Map<String, Double> planPricing = new LinkedHashMap<>();

   // Inside CoffeeController.java constructor
public CoffeeController() {
    planPricing.put("Basic", 5.0);
    planPricing.put("Premium", 10.0);
    planPricing.put("VIP", 20.0);

    masterData.addListener((ListChangeListener<Subscriber>) c -> {
        double total = masterData.stream().mapToDouble(Subscriber::getPrice).sum();
        totalRevenue.set(total);
    });
}
    public ObservableList<Subscriber> getMasterData() { return masterData; }
    public FilteredList<Subscriber> getFilteredData() { return filteredData; }
    public DoubleProperty totalRevenueProperty() { return totalRevenue; }
    public List<String> getAvailablePlans() { return new ArrayList<>(planPricing.keySet()); }

    public void filterData(String keyword) {
        filteredData.setPredicate(sub -> {
            if (keyword == null || keyword.isBlank()) return true;
            String lower = keyword.toLowerCase();
            return sub.getName().toLowerCase().contains(lower) || 
                   sub.getEmail().toLowerCase().contains(lower);
        });
    }

    public void addSubscriber(String name, String ageStr, String email, String plan, String duration) throws Exception {
    if (name == null || name.isBlank()) throw new Exception("Name is required.");
    if (email == null || !EMAIL_PATTERN.matcher(email).matches()) throw new Exception("Invalid email format.");
    
    int age;
    try {
        age = Integer.parseInt(ageStr.trim());
    } catch (NumberFormatException e) {
        throw new Exception("Age must be a whole number.");
    }
    
    if (age < 13 || age > 120) throw new Exception("Age must be between 13 and 120.");

    // --- New Duration Logic ---
    int monthsToAdd = switch (duration) {
        case "6 Months" -> 6;
        case "1 Year" -> 12;
        default -> 1; // Default to 1 month
    };

    double basePrice = planPricing.getOrDefault(plan, 0.0);
    double totalPrice = basePrice * monthsToAdd;
    String expiryDate = LocalDate.now().plusMonths(monthsToAdd).toString();

    masterData.add(new Subscriber(name, age, email, plan, expiryDate, totalPrice));
    saveData();
}
    

public void updateSubscriber(Subscriber s, String name, String ageStr, String email, String plan, String duration) throws Exception {
    if (name == null || name.isBlank()) throw new Exception("Name is required.");
    
    int age;
    try {
        age = Integer.parseInt(ageStr.trim());
    } catch (NumberFormatException e) {
        throw new Exception("Age must be a number.");
    }
//expiry
    int monthsToAdd = switch (duration) {
        case "6 Months" -> 6;
        case "1 Year" -> 12;
        default -> 1;
    };


    String newExpiry = java.time.LocalDate.now().plusMonths(monthsToAdd).toString();
    double newPrice = planPricing.getOrDefault(plan, 0.0) * monthsToAdd;

    // 3. Update the Subscriber properties 
    s.nameProperty().set(name);
    s.ageProperty().set(age);
    s.emailProperty().set(email);
    s.planProperty().set(plan);
    s.expiryDateProperty().set(newExpiry); 
    s.priceProperty().set(newPrice);

    saveData(); // Persist to CSV
}
    public void deleteSubscriber(Subscriber s) {
        if (s != null) {
            masterData.remove(s);
            saveData();
        }
    }

    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            masterData.forEach(s -> pw.println(s.toCsv()));
        } catch (IOException e) { System.err.println("Save failed: " + e.getMessage()); }
    }

    public void loadData() {
        try {
            if (Files.exists(Paths.get(DATA_FILE))) {
                Files.readAllLines(Paths.get(DATA_FILE)).forEach(line -> {
                    if (!line.isBlank()) masterData.add(Subscriber.fromCsv(line));
                });
            }
        } catch (Exception e) { System.err.println("Load failed: " + e.getMessage()); }
    }
}
