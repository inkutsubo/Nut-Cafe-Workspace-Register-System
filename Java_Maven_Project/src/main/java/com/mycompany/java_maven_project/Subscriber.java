package com.mycompany.java_maven_project;

import javafx.beans.property.*;

public class Subscriber {
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty plan = new SimpleStringProperty();
    private final StringProperty expiryDate = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();

    public Subscriber(String name, int age, String email, String plan, String expiry, double price) {
        this.name.set(name);
        this.age.set(age);
        this.email.set(email);
        this.plan.set(plan);
        this.expiryDate.set(expiry);
        this.price.set(price);
    }

    // Standard getters for Logic
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public double getPrice() { return price.get(); }

    // Properties for JavaFX TableView bindings
    public StringProperty nameProperty() { return name; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty emailProperty() { return email; }
    public StringProperty planProperty() { return plan; }
    public StringProperty expiryDateProperty() { return expiryDate; }
    public DoubleProperty priceProperty() { return price; }

    public String toCsv() {
        return String.join(",", name.get(), String.valueOf(age.get()), email.get(), plan.get(), expiryDate.get(), String.valueOf(price.get()));
    }

    public static Subscriber fromCsv(String csv) {
        String[] parts = csv.split(",");
        return new Subscriber(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], Double.parseDouble(parts[5]));
    }
}
