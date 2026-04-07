package com.mycompany.java_maven_project;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CoffeeShopUI {
    // Nut Cafe Brand Palette
    private final String COLOR_WHITE = "#FFFFFF";
    private final String BORDER_COLOR = "#EADDCC"; // Soft latte border
    private final String TEXT_TITLE = "#4A2E1B";   // Dark roast
    private final String TEXT_LABEL = "#6F4E37";   // Coffee brown

    public VBox createCard(String title, Region content) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: " + COLOR_WHITE + "; " +
                      "-fx-border-color: " + BORDER_COLOR + "; " +
                      "-fx-border-width: 1.5; " +
                      "-fx-border-radius: 12; " +
                      "-fx-background-radius: 12;");
        
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_TITLE + ";");
        
        card.getChildren().addAll(lblTitle, content);
        return card;
    }

    public VBox createFieldGroup(String labelText, Control inputField) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_LABEL + "; -fx-font-weight: bold;");
        return new VBox(6, label, inputField);
    }

    public void styleButton(Button btn, String bgColor, boolean primaryTextWhite) {
        btn.setStyle("-fx-background-color: " + bgColor + "; " +
                     "-fx-text-fill: " + (primaryTextWhite ? "white" : TEXT_TITLE) + "; " +
                     "-fx-font-weight: bold; " +
                     "-fx-background-radius: 8; " +
                     "-fx-padding: 10 20; " +
                     "-fx-cursor: hand;");
        
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));
    }

    public void styleInput(TextField tf) {
        tf.setStyle("-fx-background-color: #FCFAFA; " +
                    "-fx-border-color: " + BORDER_COLOR + "; " +
                    "-fx-border-radius: 6; " +
                    "-fx-background-radius: 6; " +
                    "-fx-padding: 10;");
    }
}