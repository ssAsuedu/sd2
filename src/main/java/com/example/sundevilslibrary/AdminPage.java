package com.example.sundevilslibrary;

import javafx.application.Platform;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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

        button2.setOnAction(event -> {
            try {
                MonitorActivity((VBox)this.getChildren().get(0),(VBox) this.getChildren().get(1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        button3.setOnAction(event -> {
            try {
                GenerateStatistics((VBox)this.getChildren().get(0),(VBox) this.getChildren().get(1));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        button4.setOnAction(event -> {
            ModifyPriceCalculator((VBox)this.getChildren().get(0),(VBox) this.getChildren().get(1));
        });



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







    public void MonitorActivity(VBox logoutBox, VBox menuBox) throws IOException {


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


        this.getChildren().add(goBackBox);
        VBox activityBox = new VBox(30);

        String filepath = "src/bookDatabase/SoldBooks.txt";
        ArrayList<Book> SoldBooks = LoadBooks.readBooksFromFile(filepath);

        if (SoldBooks.size()==0){
            activityBox.getChildren().add(new Label("There is no transaction history."));
        }




        final int ITEMS_PER_PAGE = 5;
        int totalPages = (int) Math.ceil((double) SoldBooks.size() / ITEMS_PER_PAGE);


        // Create Pagination control
        Pagination pagination = new Pagination(totalPages);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                VBox pageContent = new VBox(10);


                int startIndex = pageIndex * ITEMS_PER_PAGE;
                int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, SoldBooks.size());


                // Add buyer information for current page
                for (int i = startIndex; i < endIndex; i++) {
                    Book book = SoldBooks.get(i);
                    HBox userItem = createBookItem(book);
                    pageContent.getChildren().add(userItem);
                }


                return pageContent;
            }
        });
        activityBox.getChildren().add(pagination);




        VBox parentBox = new VBox(30);
        Label titleLabel = new Label("View Book Transaction History");
        titleLabel.getStyleClass().add("title");
        parentBox.getChildren().addAll(titleLabel, activityBox);
        parentBox.setAlignment(Pos.TOP_CENTER);
        parentBox.setPadding(new Insets(30));
        this.getChildren().add(parentBox);










    }
    public HBox createBookItem(Book book){
        HBox bookItem = new HBox(140);
        VBox leftPane = new VBox(20);
        VBox rightPane = new VBox(20);




        Label title = new Label(book.getTitle());
        Label condition = new Label(book.getCondition());
        Label category = new Label(book.getCategory());
        Label price = new Label("$" + book.getPrice().toString());




        leftPane.getChildren().addAll(title, condition, category);




        rightPane.getChildren().addAll(price);




        leftPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
        bookItem.setAlignment(Pos.CENTER);
        bookItem.getChildren().addAll(leftPane, rightPane);
        bookItem.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        bookItem.setPadding(new Insets(10,0,10,0));




        bookItem.getStyleClass().add("userCard");
        return bookItem;
    }
    public void ModifyPriceCalculator(VBox logoutBox, VBox menuBox){
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


        this.getChildren().add(goBackBox);


        VBox modifyFormBox = new VBox(30);
        Label titleLabel = new Label ("Modify Price Calculator");
        titleLabel.getStyleClass().add("title");
        modifyFormBox.getChildren().add(titleLabel);


        VBox textfieldsBox = new VBox(30);
        Label usdnewLabel = new Label("Used - Like New");
        TextField usedLikeNew = new TextField();
        Label goodLabel = new Label("Good");
        TextField good = new TextField();
        Label moderateLabel = new Label("Moderate");
        TextField moderate = new TextField();
        Label poorLabel = new Label("Poor");
        TextField poor = new TextField();




        textfieldsBox.getChildren().addAll(usdnewLabel, usedLikeNew, goodLabel, good, moderateLabel, moderate, poorLabel, poor);




        usedLikeNew.getStyleClass().add("textfield");
        good.getStyleClass().add("textfield");
        moderate.getStyleClass().add("textfield");
        poor.getStyleClass().add("textfield");
        // make the textfields only accept numbers:
        usedLikeNew.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                try {
                    // Attempt to parse the input as a double
                    Double.parseDouble(newValue);
                    // If parsing is successful, the input is valid
                } catch (NumberFormatException e) {
                    // If parsing fails, remove invalid characters
                    usedLikeNew.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        });
        good.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                try {
                    // Attempt to parse the input as a double
                    Double.parseDouble(newValue);
                    // If parsing is successful, the input is valid
                } catch (NumberFormatException e) {
                    // If parsing fails, remove invalid characters
                    good.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        });
        moderate.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                try {
                    // Attempt to parse the input as a double
                    Double.parseDouble(newValue);
                    // If parsing is successful, the input is valid
                } catch (NumberFormatException e) {
                    // If parsing fails, remove invalid characters
                    moderate.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        });
        poor.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                try {
                    // Attempt to parse the input as a double
                    Double.parseDouble(newValue);
                    // If parsing is successful, the input is valid
                } catch (NumberFormatException e) {
                    // If parsing fails, remove invalid characters
                    poor.setText(newValue.replaceAll("[^\\d.]", ""));
                }
            });
        });










        textfieldsBox.setAlignment(Pos.CENTER_LEFT);
        textfieldsBox.setPadding(new Insets(30));




        Button submitForm = new Button("Save Changes");
        submitForm.getStyleClass().add("submit");




        modifyFormBox.getChildren().addAll(textfieldsBox, submitForm);
        modifyFormBox.setAlignment(Pos.TOP_CENTER);
        modifyFormBox.setPadding(new Insets(30));
        // adds the new formBox to the screen
        this.getChildren().add(modifyFormBox);


        submitForm.setOnAction(event -> {
           // get whatever new values the user entered
            String newUsed = usedLikeNew.getText();
            String newGood = good.getText();
            String newModerate = moderate.getText();
            String newPoor = poor.getText();


             // code opens the file you created and writes the new values
            String filepath = "src/bookDatabase/PriceCalculator.txt";
            try {
                FileWriter fw = new FileWriter(filepath);
                fw.write("Used - Like New: " + newUsed + "\n" + "Good: " + newGood + "\n" + "Moderate: " + newModerate + "\n" + "Poor: " + newPoor + "\n" );
                fw.close();




            } catch (IOException error) {
                System.out.println("Failed to open file PriceCalculator.txt");
            }


            modifyFormBox.getChildren().clear();
            modifyFormBox.getChildren().add( new Label("Successfully Changed Price Calculator!"));


        });



    }
    public void GenerateStatistics(VBox logoutBox, VBox menuBox) throws IOException {
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


        this.getChildren().add(goBackBox);

        VBox parentBox = new VBox(20);
        Label titleLabel = new Label("View The Library's Statistics");
        titleLabel.getStyleClass().add("title");

        String filepath = "src/bookDatabase/SoldBooks.txt";
        ArrayList<Book> SoldBooks = LoadBooks.readBooksFromFile(filepath);

        String filepath2 = "src/bookDatabase/SoldBooks.txt";
        ArrayList<Book> books = LoadBooks.readBooksFromFile(filepath2);

        VBox dashboard = new VBox (20);

        HBox hbox1 = new HBox(40);
        HBox hbox2 = new HBox(40);
        HBox hbox3 = new HBox();

        VBox netSales = new VBox(10);
        VBox totalSold = new VBox(10);
        VBox totalListed = new VBox(10);
        VBox totalAdmins = new VBox(10);
        VBox totalBuyers = new VBox(10);
        VBox totalSellers = new VBox(10);

        Double netSale = 0.00;
        for (Book book: SoldBooks){
            netSale += book.getPrice();
        }
        Label salesLabel = new Label("Total Revenue: ");
        Label sales = new Label("$" + netSale.toString());
        netSales.getChildren().addAll(salesLabel, sales);

        Label soldLabel = new Label("Total Sold: ");
        Label sold = new Label(String.valueOf(SoldBooks.size()));
        totalSold.getChildren().addAll(soldLabel, sold);

        Label listedLbl = new Label("Books Listed:");
        Label listed = new Label(String.valueOf(books.size()));
        totalListed.getChildren().addAll(listedLbl, listed);
        listed.getStyleClass().add("title");


        Label admLabel = new Label("Total Admins:");
        Label adm = new Label(String.valueOf(GlobalAdmins.size()));
        totalAdmins.getChildren().addAll(admLabel, adm);


        Label buyLabel = new Label("Total Buyers:");
        Label buy = new Label(String.valueOf(GlobalBuyers.size()));
        totalBuyers.getChildren().addAll(buyLabel, buy);

        Label sellLabel = new Label("Total Sellers:");
        Label sell = new Label(String.valueOf(GlobalSellers.size()));
        totalSellers.getChildren().addAll(sellLabel, sell);

        sales.getStyleClass().add("title");
        sold.getStyleClass().add("title");
        adm.getStyleClass().add("title");
        buy.getStyleClass().add("title");
        sell.getStyleClass().add("title");


        totalListed.getStyleClass().add("userCard");
        totalSellers.getStyleClass().add("userCard");
        totalBuyers.getStyleClass().add("userCard");
        totalAdmins.getStyleClass().add("userCard");
        totalSold.getStyleClass().add("userCard");
        netSales.getStyleClass().add("userCard");



        hbox1.getChildren().addAll(netSales, totalSold, totalListed );
        hbox2.getChildren().addAll(  totalBuyers, totalSellers, totalAdmins);


        final CategoryAxis x = new CategoryAxis();
        final NumberAxis y = new NumberAxis();
        x.setLabel("Month");
        y.setLabel("Revenue");

        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(x,y);

        lineChart.setTitle("Revenue Over Time");

        XYChart.Series series = new XYChart.Series();
        series.setName("Gross Sales ($)");

        series.getData().add(new XYChart.Data("Jan", 0));
        series.getData().add(new XYChart.Data("Feb", 45.99));
        series.getData().add(new XYChart.Data("Mar", 45.99));
        series.getData().add(new XYChart.Data("Apr", 141.98));
        series.getData().add(new XYChart.Data("May", 154.97));
        series.getData().add(new XYChart.Data("Jun", 250.96));
        series.getData().add(new XYChart.Data("Jul", 285.96));
        series.getData().add(new XYChart.Data("Aug", 405.96));



        lineChart.getData().add(series);

        lineChart.setMinHeight(250);
        hbox3.getChildren().add(lineChart);


        dashboard.getChildren().addAll(hbox3, hbox1, hbox2);
        hbox2.setAlignment(Pos.CENTER_RIGHT);
        hbox1.setAlignment(Pos.CENTER_RIGHT);
        parentBox.getChildren().addAll(titleLabel, dashboard);

        this.getChildren().add(parentBox);
        this.setAlignment(Pos.TOP_CENTER);
        parentBox.setAlignment(Pos.TOP_CENTER);
        titleLabel.setPadding(new Insets(30, 0, 0,0));

        dashboard.setAlignment(Pos.CENTER);
        dashboard.setPadding(new Insets(20));

        hbox1.setPadding(new Insets(40));
        hbox2.setPadding(new Insets(40));
        hbox3.setPadding(new Insets(40));




    }


}