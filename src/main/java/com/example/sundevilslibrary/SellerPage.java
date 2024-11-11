package com.example.sundevilslibrary;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.converter.NumberStringConverter;

import javax.security.auth.callback.ConfirmationCallback;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class SellerPage extends HBox { // extends Parent
    private IntegerProperty loggedOut = new SimpleIntegerProperty(0);

    Label TF = new Label("Seller Page");
    public SellerPage(){
        VBox vbox = new VBox(30);


        Label pageLabel = new Label("List your Book Here!");
        pageLabel.getStyleClass().add("title");

        Label titleLabel = new Label("Title");
        TextField title = new TextField();
        title.getStyleClass().add("textfield");

        Label condition = new Label("Condition");
        ComboBox<String> comboBox1 = new ComboBox<>();
        comboBox1.getItems().addAll("New", "Used - Like New", "Used");
        comboBox1.setValue("New");

        Label category = new Label("Category");
        ComboBox<String> comboBox2 = new ComboBox<>();
        comboBox2.getItems().addAll("Natural Science", "Computer", "Math", "English Language", "Other");
        comboBox2.setValue("Natural Science");
        Label origPrice = new Label("Original Price");
        TextField numberField = new TextField();
        numberField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        numberField.getStyleClass().add("textfield");

        VBox errorV = new VBox();

        Button submitButton = new Button("Generate Price");
        submitButton.getStyleClass().add("submit");
        submitButton.setPrefWidth(140);
        submitButton.setOnAction(event-> {
            if (title.getText().trim().isEmpty() || numberField.getText().trim().isEmpty() ){

                errorV.setVisible(true);
                } else{
            String bookTitle = title.getText();
            Double startingPrice = Double.parseDouble(numberField.getText());
            String cond = comboBox1.getValue();
            String cat = comboBox2.getValue();
            errorV.setVisible(false);
            generateListing(vbox, bookTitle, startingPrice, cond, cat);
              }
        });

        // require numberField to only accept numbers
        numberField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                try {
                    // Attempt to parse the input as a double
                    Double.parseDouble(newValue);
                    // If parsing is successful, the input is valid
                } catch (NumberFormatException e) {
                    // If parsing fails, remove invalid characters
                    numberField.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        });
        Button logOff = new Button("Log Out");
        logOff.getStyleClass().add("submit");
        logOff.setOnAction(event -> {
            loggedOut.set(1);
        });
        VBox logOutBox = new VBox(30);
        logOutBox.getChildren().add(logOff);
        logOutBox.setPadding(new Insets(20));
        logOutBox.setAlignment(Pos.BASELINE_LEFT);
        logOutBox.setPrefWidth(40);

        errorV.setPrefHeight(40);
        errorV.setMaxWidth(350);
        errorV.getStyleClass().add("errorbox");

        Label errorMsg = new Label("Fill in all fields before generating Price!");
        errorMsg.getStyleClass().add("error");
        Font font3 = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 16);
        errorMsg.setFont(font3);

        errorV.setVisible(false);

        errorV.setAlignment(Pos.CENTER);
        errorV.getChildren().add(errorMsg);


        vbox.setAlignment(Pos.TOP_CENTER);
        VBox inputs = new VBox(20);
        inputs.setPadding(new Insets(30));
        vbox.setPadding(new Insets(20));
        inputs.getChildren().addAll( pageLabel, titleLabel, title, condition, comboBox1, category, comboBox2,origPrice, numberField);

        inputs.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll( inputs, submitButton, errorV);


        this.setAlignment(Pos.CENTER);

        this.getChildren().addAll(logOutBox, vbox);



    }

    public Double generateListing(VBox vbox, String title, Double price, String condition, String category){
        this.getChildren().remove(vbox);
        VBox vbox2 = new VBox(20);
        Label confirmPurch = new Label("Confirm Your Listing");
        confirmPurch.getStyleClass().add("title");
        Label title1 = new Label("Title: \t" + title);
        DecimalFormat formatPrices = new DecimalFormat("#.##");
        if (condition.equals("New")){
            price *= 0.85;
        } else if (condition.equals("Used")){
            price *= 0.6;
        } else {
            price *= 0.7;
        }
        String prices = formatPrices.format(price);
        Label newPrice = new Label("Calculated Price: \t" + prices);
        Label Condition = new Label("Condition: \t" + condition);
        Label Category = new Label("Category: \t" + category);

        Button confirmListing = new Button("Confirm Listing");
        confirmListing.getStyleClass().add("submit");
        Button cancelListing = new Button("Cancel Listing");
        cancelListing.getStyleClass().add("submit");

        cancelListing.setOnAction(event ->{
            this.getChildren().remove(vbox2);
            this.getChildren().add(vbox);
            this.setAlignment(Pos.CENTER);
        });

        confirmListing.setOnAction(event ->{
            String filepath = "src/bookDatabase/Books.txt";
            try {
                FileWriter fw = new FileWriter(filepath, true);
                fw.write("{" + "\n" + "Title: " + title + "\n" + "Category: " + category + "\n" + "Price: $" + prices + "\n" + "Condition: " + condition + "\n" + "}" + "\n");
                fw.close();

            } catch (IOException error) {
                System.out.println("Failed to open file Books.txt");
            }
            this.getChildren().remove(vbox2);
            vbox.getChildren().add(new Label("Your Book was successfully listed!"));
            this.getChildren().add(vbox);


            this.setAlignment(Pos.CENTER);
        });


        VBox receipts = new VBox(20);
        receipts.getChildren().addAll(title1, newPrice, Condition, Category);
        receipts.setPadding(new Insets(30));
        receipts.setAlignment(Pos.CENTER_LEFT);
        vbox2.getChildren().addAll(confirmPurch, receipts, confirmListing, cancelListing );



        vbox2.setAlignment(Pos.TOP_CENTER);
        vbox2.setPadding(new Insets(30));


        this.getChildren().add(vbox2);
        this.setAlignment(Pos.CENTER);
        return price;
    }
    public ObservableValue<? extends Number> getLoggedOut() {
        return loggedOut;
    }


}