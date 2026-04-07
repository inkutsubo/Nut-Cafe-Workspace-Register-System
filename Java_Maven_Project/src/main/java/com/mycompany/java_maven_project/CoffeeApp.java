package com.mycompany.java_maven_project;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CoffeeApp extends Application {
    private final CoffeeController controller = new CoffeeController();
    private final CoffeeShopUI ui = new CoffeeShopUI();

    // UI Inputs for Dashboard
    private final TextField nameIn = new TextField();
    private final TextField ageIn = new TextField();
    private final TextField emailIn = new TextField();
    private final ComboBox<String> planIn = new ComboBox<>();
    private final TextField searchIn = new TextField();
    private final Label statusLabel = new Label(""); // Inline UX status

    @Override
    public void start(Stage stage) {
        controller.loadData();
        // Bắt đầu bằng màn hình Login thay vì Dashboard
        showLoginScreen(stage);
    }

    // ==========================================
    // 🔒 1. LOGIN SCREEN
    // ==========================================
    private void showLoginScreen(Stage stage) {
        TextField userIn = new TextField();
        ui.styleInput(userIn);
        userIn.setPromptText("Username");

        PasswordField passIn = new PasswordField();
        ui.styleInput(passIn);
        passIn.setPromptText("Password");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #D9534F; -fx-font-size: 13px; -fx-font-weight: bold;");

        Button loginBtn = new Button("Login ☕");
        ui.styleButton(loginBtn, "#6F4E37", true);
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        // Logic kiểm tra đăng nhập
        Runnable attemptLogin = () -> {
            if ("admin".equals(userIn.getText()) && "1234".equals(passIn.getText())) {
                showDashboard(stage); // Đăng nhập thành công -> Mở Dashboard
            } else {
                errorLabel.setText("Incorrect username or password.");
                passIn.clear();
            }
        };

        loginBtn.setOnAction(e -> attemptLogin.run());
        passIn.setOnAction(e -> attemptLogin.run()); // Hỗ trợ nhấn Enter ở ô mật khẩu để Login nhanh

        VBox form = new VBox(15,
            ui.createFieldGroup("Admin Username", userIn),
            ui.createFieldGroup("Password", passIn),
            new Region(), // Khoảng trống nhỏ
            loginBtn,
            errorLabel
        );
        form.setAlignment(Pos.CENTER);
        form.setPrefWidth(280);

        VBox card = ui.createCard("☕ Nut Cafe | Workspace Portal", form);
        card.setMaxWidth(350);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        // Đặt Card vào giữa màn hình
        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #FAF6F0;"); // Màu nền kem ấm áp

        // Kích thước cửa sổ giữ nguyên để lúc chuyển sang Dashboard không bị giật
        stage.setScene(new Scene(root, 1150, 700)); 
        stage.setTitle("☕ Nut Cafe - Login");
        stage.show();
    }

    // ==========================================
    // 📊 2. MAIN DASHBOARD SCREEN
    // ==========================================
    private void showDashboard(Stage stage) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #FAF6F0;");

        HBox header = createHeader();
        HBox body = new HBox(25, createSidebarForm(), createMainContent());
        body.setPadding(new Insets(25));
        HBox.setHgrow(body.getChildren().get(1), Priority.ALWAYS);

        root.getChildren().addAll(header, body);

        stage.getScene().setRoot(root); // Thay đổi giao diện trên cùng một Scene
        stage.setTitle("☕ Nut Cafe Workspace - Manager Dashboard");
    }

    private HBox createHeader() {
        HBox header = new HBox(30);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setStyle("-fx-background-color: #FFFFFF; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        Label title = new Label("☕ Nut Cafe Workspace Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #4A2E1B;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label subsStat = new Label();
        subsStat.textProperty().bind(Bindings.concat("👥 Active Subscribers: ", Bindings.size(controller.getMasterData()).asString()));
        subsStat.setStyle("-fx-font-size: 15px; -fx-text-fill: #6F4E37; -fx-font-weight: bold;");

        Label revStat = new Label();
        revStat.textProperty().bind(Bindings.concat("💰 Revenue: $", controller.totalRevenueProperty().asString("%.2f")));
        revStat.setStyle("-fx-font-size: 15px; -fx-text-fill: #2E8B57; -fx-font-weight: bold;");

        header.getChildren().addAll(title, spacer, subsStat, revStat);
        return header;
    }

    private VBox createSidebarForm() {
        ui.styleInput(nameIn); nameIn.setPromptText("e.g. Alex Bean");
        ui.styleInput(ageIn); ageIn.setPromptText("e.g. 24");
        ui.styleInput(emailIn); emailIn.setPromptText("alex@nutcafe.com");

        planIn.getItems().clear();
        planIn.getItems().addAll(controller.getAvailablePlans());
        planIn.setValue(controller.getAvailablePlans().get(0));
        planIn.setMaxWidth(Double.MAX_VALUE);
        planIn.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #EADDCC; -fx-border-radius: 6; -fx-padding: 4;");

        Button addBtn = new Button("Add Subscriber");
        ui.styleButton(addBtn, "#6F4E37", true); 
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> handleRegistration());

        Button clearBtn = new Button("Clear");
        ui.styleButton(clearBtn, "#EADDCC", false); 
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> clearForm(false));

        HBox btnGroup = new HBox(10, addBtn, clearBtn);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        HBox.setHgrow(clearBtn, Priority.ALWAYS);
        addBtn.setMaxWidth(Double.MAX_VALUE); clearBtn.setMaxWidth(Double.MAX_VALUE);

        statusLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        statusLabel.setWrapText(true);

        VBox form = new VBox(15,
            ui.createFieldGroup("Full Name", nameIn),
            ui.createFieldGroup("Age", ageIn),
            ui.createFieldGroup("Email Address", emailIn),
            ui.createFieldGroup("Subscription Plan", planIn),
            new Region(),
            btnGroup,
            statusLabel
        );
        form.setPrefWidth(320);
        return ui.createCard("New Registration", form);
    }

    private VBox createMainContent() {
        ui.styleInput(searchIn);
        searchIn.setPromptText("Search community by name or email...");
        searchIn.textProperty().addListener((obs, oldVal, newVal) -> controller.filterData(newVal));
        
        HBox searchBar = new HBox(10, new Label("🔍"), searchIn);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchIn, Priority.ALWAYS);

        TableView<Subscriber> table = new TableView<>(controller.getFilteredData());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-border-color: transparent; -fx-font-size: 14px;");
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Subscriber, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());

        TableColumn<Subscriber, Number> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(c -> c.getValue().ageProperty());
        ageCol.setMaxWidth(80);

        TableColumn<Subscriber, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> c.getValue().emailProperty());

        TableColumn<Subscriber, String> planCol = new TableColumn<>("Plan");
        planCol.setCellValueFactory(c -> c.getValue().planProperty());

        TableColumn<Subscriber, String> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(c -> c.getValue().expiryDateProperty());

        TableColumn<Subscriber, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> Bindings.format("$%.2f", c.getValue().priceProperty()));
        priceCol.setMaxWidth(100);

        table.getColumns().addAll(nameCol, ageCol, emailCol, planCol, expiryCol, priceCol);

        Button deleteBtn = new Button("Delete Selected");
        ui.styleButton(deleteBtn, "#D9534F", true);
        deleteBtn.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        deleteBtn.setOnAction(e -> {
            controller.deleteSubscriber(table.getSelectionModel().getSelectedItem());
            showStatus("Subscriber removed.", "#D9534F");
        });

        HBox actions = new HBox(deleteBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(15, searchBar, table, actions);
        return ui.createCard("Active Community", content);
    }

    private void handleRegistration() {
        try {
            controller.addSubscriber(nameIn.getText(), ageIn.getText(), emailIn.getText(), planIn.getValue());
            clearForm(true);
            showStatus("Success! Subscriber added ☕", "#2E8B57");
        } catch (Exception ex) {
            showStatus("Oops: " + ex.getMessage(), "#D9534F");
        }
    }

    private void clearForm(boolean focusName) {
        nameIn.clear(); ageIn.clear(); emailIn.clear();
        planIn.setValue(controller.getAvailablePlans().get(0));
        if (focusName) nameIn.requestFocus();
    }

    private void showStatus(String message, String colorCode) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: " + colorCode + "; -fx-font-size: 13px; -fx-font-weight: bold;");
    }

    public static void main(String[] args) { launch(); }
}