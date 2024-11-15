package com.example.sundevilslibrary;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AdminPage extends HBox{ // extends Parent
    private IntegerProperty loggedOut = new SimpleIntegerProperty(0);

    ArrayList<Buyer> GlobalBuyers;
    ArrayList<Seller> GlobalSellers;
    ArrayList<Admin> GlobalAdmins;
    Label TF = new Label("Administrator's Menu");

    public AdminPage(ArrayList<Buyer> Buyers, ArrayList<Seller> Sellers, ArrayList<Admin> Admins){
        GlobalBuyers = Buyers;
        GlobalAdmins = Admins;
        GlobalSellers = Sellers;

        TF.getStyleClass().add("title");

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

            ManageUsers((VBox)this.getChildren().get(0),(VBox) this.getChildren().get(1));

        });



        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(logOutBox, vbox);

    }




    public ObservableValue<? extends Number> getLoggedOut() {
        return loggedOut;
    }

    public void ManageUsers(VBox logoutBox, VBox menuBox){

        this.getChildren().removeAll(this.getChildren().get(0), this.getChildren().get(1));

        VBox goBackBox = new VBox(30);
        Button goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("submit");
        goBackButton.setOnAction(event-> {
            this.getChildren().clear();
            this.getChildren().addAll(logoutBox, menuBox);
            this.setAlignment(Pos.TOP_CENTER);
        });

        goBackBox.getChildren().add(goBackButton);
        goBackBox.setAlignment(Pos.TOP_LEFT);
        goBackBox.setPadding(new Insets(30));

        ToggleGroup userGroup = new ToggleGroup();
        Label userFilter = new Label("Filters");
        RadioButton buyers = new RadioButton("Buyers");
        RadioButton sellers = new RadioButton("Sellers");
        RadioButton admins = new RadioButton("Admins");
        buyers.setToggleGroup(userGroup);
        buyers.setSelected(true);
        sellers.setToggleGroup(userGroup);
        admins.setToggleGroup(userGroup);

        VBox userBox = new VBox(30);
        goBackBox.getChildren().addAll(userFilter, buyers, sellers, admins);
        this.getChildren().add(goBackBox);
        displayBuyers(GlobalBuyers, userBox);
        userGroup.selectedToggleProperty().addListener(observable -> {
                    RadioButton selectedRadioButton = (RadioButton) userGroup.getSelectedToggle();
                    String selectedValue = selectedRadioButton.getText();
                    if(selectedValue.equals("Buyers")) {
                        displayBuyers(GlobalBuyers, userBox);
                    }
                    else if(selectedValue.equals("Sellers")) {
                        displaySellers(GlobalSellers, userBox);
                    }
                    else if(selectedValue.equals("Admins")) {
                        displayAdmins(GlobalAdmins, userBox);
                    }



                });




        VBox ManageUserBox = new VBox(30);
        Label manageLabel = new Label("Manage Users");
        manageLabel.getStyleClass().add("title");

        ManageUserBox.getChildren().addAll(manageLabel, userBox);
        ManageUserBox.setPadding(new Insets(30));
        ManageUserBox.setAlignment(Pos.TOP_CENTER);
        this.getChildren().add(ManageUserBox);


    };
    public VBox displayBuyers(ArrayList<Buyer> buyers, VBox userBox){
        userBox.getChildren().clear();
        if (GlobalBuyers.size()==0){
            Label empty = new Label("There are currently no buyers to view");
            userBox.getChildren().add(empty);
            return userBox;
        }
        final int ITEMS_PER_PAGE = 5;
        int totalPages = (int) Math.ceil((double) buyers.size() / ITEMS_PER_PAGE);

        // Create Pagination control
        Pagination pagination = new Pagination(totalPages);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                VBox pageContent = new VBox(10);

                int startIndex = pageIndex * ITEMS_PER_PAGE;
                int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, buyers.size());

                // Add buyer information for current page
                for (int i = startIndex; i < endIndex; i++) {
                    Buyer buyer = buyers.get(i);
                    HBox userItem = createBuyerItem(buyer);
                    pageContent.getChildren().add(userItem);
                }

                return pageContent;
            }
        });
        userBox.getChildren().add(pagination);
        return userBox;
    };
    public VBox displaySellers(ArrayList<Seller> sellers , VBox userBox){
        userBox.getChildren().clear();
        if (GlobalSellers.size()==0){
            Label empty = new Label("There are currently no sellers to view");
            userBox.getChildren().add(empty);
            return userBox;
        }
        final int ITEMS_PER_PAGE = 5;
        int totalPages = (int) Math.ceil((double) sellers.size() / ITEMS_PER_PAGE);

        // Create Pagination control
        Pagination pagination = new Pagination(totalPages);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                VBox pageContent = new VBox(10);

                int startIndex = pageIndex * ITEMS_PER_PAGE;
                int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, sellers.size());

                // Add buyer information for current page
                for (int i = startIndex; i < endIndex; i++) {
                    Seller seller = sellers.get(i);
                    HBox userItem = createSellerItem(seller);
                    pageContent.getChildren().add(userItem);
                }

                return pageContent;
            }
        });
        userBox.getChildren().add(pagination);
        return userBox;
    };
    public VBox displayAdmins(ArrayList<Admin> admins, VBox userBox){
        userBox.getChildren().clear();
        Button createNewAdmin = new Button("Create New Admin");
        createNewAdmin.getStyleClass().add("submit");
        userBox.getChildren().add(createNewAdmin);

        createNewAdmin.setOnAction(e->{
            VBox firstNode = (VBox)this.getChildren().get(1);
            firstNode.getChildren().remove(1);
            VBox userBox2 = new VBox(30);
            // Create the content for the popup

            userBox2.setPadding(new Insets(20));
            userBox2.setAlignment(Pos.CENTER_LEFT);
            Label createLabel = new Label("Create New Admin");
            createLabel.getStyleClass().add("title");
            Label nameLabel = new Label("Name");
            TextField name = new TextField();
            Label idLabel = new Label("ASU ID");
            TextField asu_id = new TextField();
            Label passLabel = new Label("Password");
            TextField password = new TextField();

            name.getStyleClass().add("textfield");
            asu_id.getStyleClass().add("textfield");
            password.getStyleClass().add("textfield");



            Button create = new Button("Create Admin");
            create.setOnAction(event2 ->{
               if(!name.getText().strip().isEmpty() && !asu_id.getText().strip().isEmpty() && !password.getText().strip().isEmpty()) {
                   createNewAdmin(name.getText().strip(), asu_id.getText().strip(), password.getText().strip());
                   firstNode.getChildren().remove(userBox2);

                   Admin newAdmin = new Admin(asu_id.getText().strip(),name.getText().strip(),  password.getText().strip());
                   GlobalAdmins.add(newAdmin);
                   displayAdmins(GlobalAdmins, userBox);
                   firstNode.getChildren().add(userBox);

               }

            });

            Button close = new Button("Cancel");
            close.getStyleClass().add("submit");
            create.getStyleClass().add("submit");
            close.setOnAction(event->{firstNode.getChildren().remove(userBox2); firstNode.getChildren().add(userBox);});
            userBox2.getChildren().addAll(createLabel, nameLabel, name, idLabel , asu_id,passLabel, password, create, close);

           firstNode.getChildren().add(1,userBox2);


            // Show the popup stage



        });


        for (Admin admin: admins) {
        HBox userItem = new HBox(140);
        VBox leftPane = new VBox(20);
        VBox rightPane = new VBox(20);

        Label title = new Label("Name: " + admin.getName());
        Label id = new Label("ASU ID: " + admin.getID());
        Label password = new Label("Password: " + admin.getPassword());


        leftPane.getChildren().addAll(title, id, password);
        Button deleteUser = new Button("Delete This User");


        deleteUser.setOnAction(event -> {

            if(admins.size()-1 ==0 ){
                Stage popupStage = new Stage();
                popupStage.initStyle(StageStyle.UTILITY); // Make the popup look like a utility window

                // Center the popup on the screen
                popupStage.centerOnScreen();

                // Create the content for the popup
                VBox popupContent = new VBox(20);
                popupContent.setPadding(new Insets(20));
                Label confirmDelete = new Label("Deletion Request Declined: There must be a minimum of one Admin user!");
                Button close = new Button("Close");
                close.setOnAction(e->{popupStage.close();});
                popupContent.getChildren().addAll(confirmDelete, close);

                Scene popupScene = new Scene(popupContent, 300, 200);
                popupStage.setScene(popupScene);

                // Show the popup stage
                popupStage.show();

            } else {
                GlobalAdmins.remove(admin);
                deleteAdmin(GlobalAdmins);
                deleteUser.setText("Deleted");
                deleteUser.getStyleClass().clear();
                deleteUser.setAlignment(Pos.CENTER);
                deleteUser.getStyleClass().add("added");
            }
        });
        deleteUser.getStyleClass().add("addToCart");
        deleteUser.setAlignment(Pos.CENTER);
        rightPane.getChildren().addAll(deleteUser);

        leftPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
        userItem.setAlignment(Pos.CENTER);
        userItem.getChildren().addAll(leftPane, rightPane);
        userItem.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        userItem.setPadding(new Insets(10, 0, 10, 0));

        userItem.getStyleClass().add("userCard");

        userBox.getChildren().add(userItem);


    }
        userBox.setAlignment(Pos.TOP_CENTER);
        return userBox;
    };
    public void createNewAdmin(String name, String id, String password){
        String filepath = "src/users/Admins.txt";
        try{
            FileWriter fw = new FileWriter(filepath, true);



            String newText = "{" + "\n" + "name: " + name + "\n" + "id: " + id + "\n" + "password: " + password + "\n" + "}\n" ;
            fw.write(newText);


            fw.close();

            System.out.println("Completed admin creation. ");
        } catch (IOException error) {
            System.out.println("Failed to open file Admins.txt");
        }
    }
    public void deleteBuyer(ArrayList<Buyer>buyers){

        String filepath = "src/users/Buyers.txt";
        try{
            FileWriter fw = new FileWriter(filepath);


            for (Buyer buyer : buyers) {
                String newText = "{" + "\n" + "name: " + buyer.getName() + "\n" + "id: " + buyer.getID() + "\n" + "password: " + buyer.getPassword() + "\n" + "}" + "\n";
                fw.write(newText);
            }

            fw.close();

            System.out.println("Completed buyer deletion. ");
        } catch (IOException error) {
            System.out.println("Failed to open file Buyers.txt");
        }
    };
    public void deleteSeller(ArrayList<Seller> sellers){
        String filepath = "src/users/Sellers.txt";
        try{
            FileWriter fw = new FileWriter(filepath);


            for (Seller seller : sellers) {
                String newText = "{" + "\n" + "name: " + seller.getName() + "\n" + "id: " + seller.getID() + "\n" + "password: " + seller.getPassword() + "\n" + "}" + "\n";
                fw.write(newText);
            }

            fw.close();

            System.out.println("Completed seller deletion. ");
        } catch (IOException error) {
            System.out.println("Failed to open file Sellers.txt");
        }
    };
    public void deleteAdmin(ArrayList<Admin> admins){
        String filepath = "src/users/Admins.txt";
        try{
            FileWriter fw = new FileWriter(filepath);


            for (Admin admin : admins) {
                String newText = "{" + "\n" + "name: " + admin.getName() + "\n" + "id: " + admin.getID() + "\n" + "password: " + admin.getPassword() + "\n" + "}" + "\n";
                fw.write(newText);
            }

            fw.close();

            System.out.println("Completed admin deletion. ");
        } catch (IOException error) {
            System.out.println("Failed to open file Admins.txt");
        }
    };

    public HBox createBuyerItem(Buyer buyer){
        HBox userItem = new HBox(140);
        VBox leftPane = new VBox(20);
        VBox rightPane = new VBox(20);

        Label title = new Label("Name: " + buyer.getName());
        Label id = new Label("ASU ID: "+ buyer.getID());
        Label password = new Label("Password: "+ buyer.getPassword());


        leftPane.getChildren().addAll(title, id, password);
        Button deleteUser = new Button("Delete This User");


        deleteUser.setOnAction(event -> {
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UTILITY); // Make the popup look like a utility window

            // Center the popup on the screen
            popupStage.centerOnScreen();

            // Create the content for the popup
            VBox popupContent = new VBox(20);
            popupContent.setPadding(new Insets(20));
            Label confirmDelete = new Label("Are you sure you want to delete this user?");
            Button cancel = new Button("Cancel");
            Button delete = new Button("Delete");
            popupContent.getChildren().addAll(confirmDelete, delete,cancel);

            // Add a close button action
            cancel.setOnAction(e -> popupStage.close());

           delete.setOnAction(e -> {


            GlobalBuyers.remove(buyer);
            deleteBuyer(GlobalBuyers);
            deleteUser.setText("Deleted");
            deleteUser.getStyleClass().clear();
            deleteUser.setAlignment(Pos.CENTER);
            deleteUser.getStyleClass().add("added");
            popupStage.close();
           });
            // Set the scene for the popup stage
            Scene popupScene = new Scene(popupContent, 300, 200);
            popupStage.setScene(popupScene);

            // Show the popup stage
            popupStage.show();



        });
        deleteUser.getStyleClass().add("addToCart");
        deleteUser.setAlignment(Pos.CENTER);
        rightPane.getChildren().addAll( deleteUser);

        leftPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
        userItem.setAlignment(Pos.CENTER);
        userItem.getChildren().addAll(leftPane, rightPane);
        userItem.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        userItem.setPadding(new Insets(10,0,10,0));

        userItem.getStyleClass().add("userCard");


        return userItem;
    };

    public HBox createSellerItem(Seller seller){
        HBox userItem = new HBox(140);
        VBox leftPane = new VBox(20);
        VBox rightPane = new VBox(20);

        Label title = new Label("Name: " + seller.getName());
        Label id = new Label("ASU ID: "+ seller.getID());
        Label password = new Label("Password: "+ seller.getPassword());


        leftPane.getChildren().addAll(title, id, password);
        Button deleteUser = new Button("Delete This User");


        deleteUser.setOnAction(event -> {
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UTILITY); // Make the popup look like a utility window

            // Center the popup on the screen
            popupStage.centerOnScreen();

            // Create the content for the popup
            VBox popupContent = new VBox(20);
            popupContent.setPadding(new Insets(20));
            Label confirmDelete = new Label("Are you sure you want to delete this user?");
            Button cancel = new Button("Cancel");
            Button delete = new Button("Delete");
            popupContent.getChildren().addAll(confirmDelete, delete, cancel);

            // Add a close button action
            cancel.setOnAction(e -> popupStage.close());

            delete.setOnAction(e -> {


                GlobalSellers.remove(seller);
                deleteSeller(GlobalSellers);
                deleteUser.setText("Deleted");
                deleteUser.getStyleClass().clear();
                deleteUser.setAlignment(Pos.CENTER);
                deleteUser.getStyleClass().add("added");
                popupStage.close();
            });
            // Set the scene for the popup stage
            Scene popupScene = new Scene(popupContent, 300, 200);
            popupStage.setScene(popupScene);

            // Show the popup stage
            popupStage.show();

        });
        deleteUser.getStyleClass().add("addToCart");
        deleteUser.setAlignment(Pos.CENTER);
        rightPane.getChildren().addAll( deleteUser);

        leftPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
        userItem.setAlignment(Pos.CENTER);
        userItem.getChildren().addAll(leftPane, rightPane);
        userItem.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        userItem.setPadding(new Insets(10,0,10,0));

        userItem.getStyleClass().add("userCard");


        return userItem;

    };

}