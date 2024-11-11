package com.example.sundevilslibrary;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.net.URL;
public class AdminPage extends HBox{ // extends Parent
    private IntegerProperty loggedOut = new SimpleIntegerProperty(0);


    Label TF = new Label("Admin Page");
    public AdminPage(){
        VBox vbox = new VBox(30);
        VBox logOutBox = new VBox(30);
        VBox buttonBox = new VBox(40);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(60));
        vbox.getChildren().add(TF);

        Button button1 = new Button("Manage Users");
        Button button2 = new Button("Monitor Activity");
        Button button3 = new Button("Generate Statistics");
        Button button4 = new Button("Modify Price Calculator");
        button1.getStyleClass().add("submit");
        button2.getStyleClass().add("submit");
        button3.getStyleClass().add("submit");
        button4.getStyleClass().add("submit");
        button1.setPrefWidth(350);
        button2.setPrefWidth(350);
        button3.setPrefWidth(350);
        button4.setPrefWidth(350);

        buttonBox.getChildren().addAll(button1, button2, button3, button4);
        buttonBox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(buttonBox);

        Button logOut = new Button("Log Out");
        logOut.getStyleClass().add("submit");
        logOutBox.getChildren().add(logOut);
        logOutBox.setAlignment(Pos.TOP_LEFT);
        logOutBox.setPadding(new Insets(30));

        logOut.setOnAction(event -> {
            loggedOut.set(1);
        });

        button1.setOnAction(event -> {
            ManageUsers();
        });



        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(logOutBox, vbox);

    }




    public ObservableValue<? extends Number> getLoggedOut() {
        return loggedOut;
    }

    public void ManageUsers(){

        this.getChildren().removeAll();






    };

}