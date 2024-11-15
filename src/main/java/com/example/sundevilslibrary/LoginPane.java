package com.example.sundevilslibrary;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
public class LoginPane extends HBox {
    private IntegerProperty loggedInAs = new SimpleIntegerProperty(0);
    private IntegerProperty createAccountRequested = new SimpleIntegerProperty(0);

    // Initialize variables for show/hide password functionality
    PasswordField passwordField = new PasswordField();
    URL selectedUrl = getClass().getResource("/images/eye.png");
    URL unselectedUrl = getClass().getResource("/images/hidden.png");

    CheckBox showpass = new CheckBox();
    TextField psdField = new TextField();
    Image selectedImage = new Image(selectedUrl.toString());
    Image unselectedImage = new Image(unselectedUrl.toString());
    ImageView unImgView = new ImageView(unselectedImage);
    ImageView selImgView = new ImageView(selectedImage);


    public LoginPane (ArrayList<Buyer> buyers, ArrayList<Admin> admins, ArrayList<Seller> sellers){
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Create and add logo
        URL url = getClass().getResource("/images/library_logo.jpg");

         Image logoImage = new Image(url.toString());// Replace with actual path
         ImageView imageView = new ImageView(logoImage);
         imageView.setFitWidth(175);
         imageView.setFitHeight(175);
         imageView.setPreserveRatio(true);
         Circle circleClip = new Circle(imageView.getBoundsInLocal().getWidth() / 2);
         circleClip.setCenterX(imageView.getBoundsInLocal().getWidth() / 2);
         circleClip.setCenterY(imageView.getBoundsInLocal().getHeight() / 2);

        // Set the circle as the clip for the image view
         imageView.setClip(circleClip);

         BorderPane p = new BorderPane();
         p.setCenter(imageView);
         p.setMaxWidth(imageView.getFitWidth()+5);
         p.setMaxHeight(imageView.getFitHeight()+5);

         p.getStyleClass().add("logo");;
         vbox.getChildren().add(p);

        // Create and add Welcom Title
        Label titleLabel = new Label("Welcome! Sign In Below");

        Font font = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 36);
        titleLabel.setFont(font);
        vbox.getChildren().add(titleLabel);
        titleLabel.getStyleClass().add("title");


        //error msg functionality
        VBox errorV = new VBox();
        errorV.setPrefHeight(40);
        errorV.setMaxWidth(350);
        errorV.getStyleClass().add("errorbox");

        Label errorMsg = new Label("Wrong Username or Password");
        errorMsg.getStyleClass().add("error");
        Font font3 = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 16);
        errorMsg.setFont(font3);

        errorV.setVisible(false);

        errorV.setAlignment(Pos.CENTER);
        errorV.getChildren().add(errorMsg);
        vbox.getChildren().add(errorV);


        // Create and add ASURITE ID  username field
        Label asuriteIdLabel = new Label("ASURITE ID:");
        asuriteIdLabel.setAlignment(Pos.CENTER_LEFT);
        TextField asuriteIdField = new TextField();

        VBox uservbox = new VBox();
        uservbox.setPrefWidth(450);
        uservbox.getChildren().addAll(asuriteIdLabel, asuriteIdField);

        uservbox.setMargin(asuriteIdLabel, new Insets(0, 0, 10, 0));

        uservbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().add(uservbox);


        // Create and add password field


        Label passwordLabel = new Label("Password:");
        StackPane s = new StackPane();

        // create event listener to conditionally show/hide password
        showpass.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showpass.setSelected(!oldValue);
        });
        StackPane s2 = new StackPane();
        s2.setMaxWidth(25);
        s2.getChildren().addAll( unImgView, selImgView, showpass);

        showpass.setOpacity(0);
        selImgView.setVisible(false);
        psdField.setVisible(false);
        unImgView.setFitHeight(25);
        unImgView.setFitWidth(25);
        selImgView.setFitHeight(25);
        selImgView.setFitWidth(25);
        s.getChildren().addAll(passwordField,psdField, s2);
        s.setAlignment(s2, Pos.CENTER_RIGHT);
        s.setMargin(s2, new Insets(15,15,15,15));
        psdField.getStyleClass().add("textfield");
        Font font2 = Font.font("Helvetica", FontPosture.REGULAR, 12);



        VBox passvbox = new VBox();
        passvbox.setPrefWidth(450);
        passvbox.getChildren().addAll(passwordLabel, s);
        passvbox.setMargin(passwordLabel, new Insets(0, 0, 10, 0));
        passvbox.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().add(passvbox);

        // add classnames for additional styling
        asuriteIdField.getStyleClass().add("textfield");
        passwordField.getStyleClass().add("textfield");
        // Create and add submit button
        Button submitButton = new Button("Sign In");
        submitButton.getStyleClass().add("submit");
        submitButton.setStyle("-fx-font-size: 16;");
        showpass.setOnAction(event);

        // add margin styling
        vbox.setPrefWidth(450);
        submitButton.setMaxWidth(200);
        vbox.setMargin(uservbox, new Insets(10, 40, 10, 40));
        vbox.setMargin(titleLabel, new Insets(10, 10, 40, 10));
        vbox.setMargin(passvbox, new Insets(10, 40, 10, 40));
        vbox.setMargin(imageView, new Insets(10, 10, 80, 10));
        vbox.setMargin(submitButton, new Insets(40, 10, 0, 10));




        // defines login functionality, shows error msg for failed login
        submitButton.setOnAction(event -> {
            // Collect user input
            String username = asuriteIdField.getText();
            String password = passwordField.getText();
            loggedInAs.set(HandleLogin(buyers, sellers, admins, username, password));
            if (loggedInAs.intValue() == 1){
                System.out.println("Logged In As Buyer!");
            } else if (loggedInAs.intValue() == 2){
                System.out.println("Logged In As Seller!");
            }
            else if (loggedInAs.intValue() == 3){
                System.out.println("Logged In As Admin!");
            }
            else if (loggedInAs.intValue() == -1){
                errorV.setVisible(true);
                System.out.println("Wrong username or password");
            }

        });
        vbox.getChildren().add(submitButton);

        // "OR" label between the two buttons
        Label orLabel = new Label("OR");
        orLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
        orLabel.setAlignment(Pos.CENTER); // Center the label text within its container
        vbox.getChildren().add(orLabel);
        vbox.setMargin(orLabel, new Insets(10, 0, 10, 0));

        // Create and add "Create New Account" button
        Button createAccountButton = new Button("Create New Account");
        createAccountButton.getStyleClass().add("create-account-button");
        createAccountButton.setStyle("-fx-font-size: 16;");
<<<<<<< HEAD
        createAccountButton.setMaxWidth(200);
        vbox.getChildren().add(createAccountButton);

=======
        vbox.getChildren().add(createAccountButton);
>>>>>>> remotes/origin/main
        vbox.setMargin(createAccountButton, new Insets(0, 10, 0, 10));
        createAccountButton.getStyleClass().add("submit");
        // Set action for create account button
        createAccountButton.setOnAction(event -> {
            createAccountRequested.set(1);
        });

        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(vbox);

    } //end constructor
    public int HandleLogin(ArrayList<Buyer> buyers,ArrayList<Seller> sellers,ArrayList<Admin> admins, String username, String password){
        // loops through buyers, sellers, and admins, returns 1 if buyer , 2 for seller, 3 for admin, -1 for failed login
        for (Buyer buyer : buyers) {
            if(buyer.getID().equals(username) && buyer.getPassword().equals(password)){
                return 1;
            }
        }
        for (Seller seller : sellers) {
            if(seller.getID().equals(username) && seller.getPassword().equals(password)){
                return 2;
            }
        }
        for (Admin admin : admins) {
            if(admin.getID().equals(username) && admin.getPassword().equals(password)){
                return 3;
            }
        }

        return -1;
    }
    //gb token: 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
    // updates global state so that main application switches scenes if login successful
    public ObservableValue<? extends Number> getLoggedInAs() {
        return loggedInAs;
    }
    // Getter for createAccountRequested
    public ObservableValue<? extends Number> getCreateAccountRequested() {
        return createAccountRequested;
    }

    // Handles event that show password is toggled
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if (showpass.isSelected()) {
                String pass = passwordField.getText();
                psdField.setText(pass);
                passwordField.setVisible(false);
                psdField.setVisible(true);
                selImgView.setVisible(true);
                unImgView.setVisible(false);
                psdField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Handle text changes here
                    passwordField.setText(newValue);
                    System.out.println("New text: " + newValue);
                });

            } else {
                String pass = psdField.getText();
                passwordField.setText(pass);
                psdField.setVisible(false);
                passwordField.setVisible(true);
                selImgView.setVisible(false);
                unImgView.setVisible(true);
            }
        }
    };


}
