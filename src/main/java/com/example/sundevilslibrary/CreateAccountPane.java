package com.example.sundevilslibrary;
//import necessary packages
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateAccountPane extends VBox {

    private final Runnable onBackToLogin;

    public CreateAccountPane(Runnable onBackToLogin) {
        this.onBackToLogin = onBackToLogin;
        initializeUI();
    }

    private void initializeUI() {
        // pane settings
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(20));
        this.setSpacing(15);

        //On the agenda: update the styles of these components
        //It is very basic, but I'm not sure what designs we want yet

        // Label
        Label titleLabel = new Label("Create a New Account");
        titleLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        titleLabel.getStyleClass().add("title");
        this.getChildren().add(titleLabel);

        // Name Field
        Label nameLabel = new Label("Full Name:");
        TextField nameField = new TextField();
        VBox nameBox = new VBox(5, nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setMaxWidth(400);
        nameField.getStyleClass().add("textfield");
        // ID Field
        Label idLabel = new Label("ASURITE ID:");
        TextField idField = new TextField();
        VBox idBox = new VBox(5, idLabel, idField);
        idBox.setAlignment(Pos.CENTER_LEFT);
        idBox.setMaxWidth(400);
        idField.getStyleClass().add("textfield");
        // password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        VBox passwordBox = new VBox(5, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setMaxWidth(400);
        passwordField.getStyleClass().add("textfield");
        // Account Type Selection
        Label accountTypeLabel = new Label("Account Type:");
        ToggleGroup accountTypeGroup = new ToggleGroup();
        RadioButton buyerRadio = new RadioButton("Buyer");
        buyerRadio.setToggleGroup(accountTypeGroup);
        buyerRadio.setSelected(true);
        RadioButton sellerRadio = new RadioButton("Seller");
        sellerRadio.setToggleGroup(accountTypeGroup);
        HBox accountTypeBox = new HBox(20, buyerRadio, sellerRadio);
        accountTypeBox.setAlignment(Pos.CENTER_LEFT);
        VBox accountTypeVBox = new VBox(5, accountTypeLabel, accountTypeBox);
        accountTypeVBox.setAlignment(Pos.CENTER_LEFT);
        accountTypeVBox.setMaxWidth(400);

        // submit Button
        Button submitButton = new Button("Create Account");
        submitButton.setMaxWidth(200);
        submitButton.getStyleClass().add("submit");
        // success message
        Label successLabel = new Label("Successfully created a new account!");
        successLabel.setVisible(false);
        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setVisible(false);

        // Add components
        this.getChildren().addAll(nameBox, idBox, passwordBox, accountTypeVBox, submitButton, successLabel, backToLoginButton);

        // submit button handler
        submitButton.setOnAction(_ -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String password = passwordField.getText().trim();
            String accountType = ((RadioButton) accountTypeGroup.getSelectedToggle()).getText();

            // make sure all fields are entered
            if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
                showAlert("All fields are required.");
                return;
            }

            try {
                if (accountType.equals("Buyer")) {
                    saveBuyerToFile(name, id, password);
                    LoadBuyers.readBuyersFromFile("src/users/Buyers.txt"); //we need to re-load the data since we are adding to it
                } else {
                    saveSellerToFile(name, id, password);
                    LoadSellers.readSellersFromFile("src/users/Sellers.txt"); //we need to re-load the data since we are adding to it
                }
                successLabel.setVisible(true);
                backToLoginButton.setVisible(true);
                submitButton.setDisable(true);
            } catch (IOException e) {
                //e.printStackTrace();
                showAlert("An error occurred");
            }
        });

        // Back to Log in Button Action
        backToLoginButton.setOnAction(_ -> {
            if (onBackToLogin != null) {
                onBackToLogin.run();
            }
        });
    }
    //So I don't know if this is how I am supposed to do this (I was just trying to match the format given in the files)
    //But I think it works
    private void saveSellerToFile(String name, String id, String password) throws IOException {
        String filepath = "src/users/Sellers.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write("{\n");
            writer.write("    \"name\": " + name + "\n");
            writer.write("    \"id\": " + id + "\n");
            writer.write("    \"password\": " + password + "\n");
            writer.write("}\n");
        }
    }

    private void saveBuyerToFile(String name, String id, String password) throws IOException {
        String filepath = "src/users/Buyers.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write("{\n");
            writer.write("    \"name\": " + name + "\n");
            writer.write("    \"id\": " + id + "\n");
            writer.write("    \"password\": " + password + "\n");
            writer.write("}\n");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);  //IntelliJ made this because it was complaining
        alert.setTitle("Create Account");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

