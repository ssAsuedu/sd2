package com.example.sundevilslibrary;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {
//    @Override
    private final IntegerProperty loggedOutBuyer= new SimpleIntegerProperty(-1);
    private final IntegerProperty loggedInAs = new SimpleIntegerProperty(-1);
    private final IntegerProperty loggedOutSeller= new SimpleIntegerProperty(-1);
    private final IntegerProperty loggedOutAdmin= new SimpleIntegerProperty(-1);
    private IntegerProperty createAccountRequested = new SimpleIntegerProperty(0);
    private StackPane root; // Make root a class-level variable

    public void start(Stage stage) throws IOException {
        // load data from text files to initialize buyers. sellers, and admins
        String url = "src/users/Buyers.txt";
        String urlSellers = "src/users/Sellers.txt";
        String urlAdmins = "src/users/Admins.txt";
        ArrayList<Buyer> buyers = LoadBuyers.readBuyersFromFile(url);
        ArrayList<Seller> sellers = LoadSellers.readSellersFromFile(urlSellers);
        ArrayList<Admin> admins = LoadAdmins.readAdminsFromFile(urlAdmins);

        // set up scene
        StackPane root = new StackPane();
        LoginPane loginPane = new LoginPane(LoadBuyers.getBuyers(), LoadAdmins.getAdmins(), LoadSellers.getSellers());
        root.getChildren().add(loginPane);
        root.setStyle("-fx-background-color: white;");
        Scene scene = new Scene(root, 800, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());

        stage.setTitle("Login");
        stage.setScene(scene);
        // binds loggedInAs variable to loginPane's variable, allows main to detect when to switch scenes
        loggedInAs.bind(loginPane.getLoggedInAs());


        // Bind createAccountRequested variable to loginPane's variable
        createAccountRequested.bind(loginPane.getCreateAccountRequested());
        // Listener for createAccountRequested
        createAccountRequested.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                // navigate to CreateAccountPane
                CreateAccountPane createAccountPane = new CreateAccountPane(() -> {
                    // On back to log in, replace CreateAccountPane with new LoginPane
                    LoginPane newLoginPane = new LoginPane(LoadBuyers.getBuyers(), LoadAdmins.getAdmins(), LoadSellers.getSellers());
                    root.getChildren().clear();
                    root.getChildren().add(newLoginPane);
                    stage.setTitle("Login");
                    loggedInAs.bind(newLoginPane.getLoggedInAs());
                    // Re-bind createAccountRequested to new LoginPane
                    createAccountRequested.bind(newLoginPane.getCreateAccountRequested());
                });
                root.getChildren().clear();

               createAccountPane.getStylesheets().add(getClass().getResource("/styles/createAcct.css").toExternalForm());

                root.getChildren().add(createAccountPane);
                stage.setTitle("Create Account");
            }
        });

        // listener event awaits change in loggedInAs variable, switches scenes accordingly
        loggedInAs.addListener((observable, oldValue, newValue) -> {
            System.out.println(oldValue);
            System.out.println(newValue);
            if (newValue.intValue() == 1) { // Switch to BuyerPage when logged in
                BuyerPage buyerPage = null;
                try {
                 ;
                    String filepath = "src/bookDatabase/Books.txt";
                    List<Book> books = LoadBooks.readBooksFromFile(filepath);


                    buyerPage = new BuyerPage(LoadBooks.getBooks());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                StackPane newRoot = new StackPane();
                newRoot.setStyle("-fx-background-color: white;");
                newRoot.getChildren().add(buyerPage);

                Scene buyerScene = new Scene(newRoot, 800, 800);
                buyerScene.getStylesheets().add(getClass().getResource("/styles/buyers.css").toExternalForm());
                loggedOutBuyer.bind(buyerPage.getLoggedOut());
                stage.setTitle("Buyer Search");
                stage.setScene(buyerScene);
            } else if (newValue.intValue() ==2){
                SellerPage sellerPage = new SellerPage();
                StackPane newRoot = new StackPane();
                newRoot.setStyle("-fx-background-color: white;");
                newRoot.getChildren().add(sellerPage);
                loggedOutSeller.bind(sellerPage.getLoggedOut());
                Scene sellerScene = new Scene(newRoot, 800, 800);
                sellerScene.getStylesheets().add(getClass().getResource("/styles/sellers.css").toExternalForm());
                stage.setTitle("Seller Page");
                stage.setScene(sellerScene);
            }else if (newValue.intValue() == 3){
                AdminPage adminPage = new AdminPage(buyers, sellers, admins);
                loggedOutAdmin.bind(adminPage.getLoggedOut());
                StackPane newRoot = new StackPane();
                newRoot.setStyle("-fx-background-color: white;");
                newRoot.getChildren().add(adminPage);
                Scene adminScene = new Scene(newRoot, 800, 800);
                adminScene.getStylesheets().add(getClass().getResource("/styles/admins.css").toExternalForm());
                stage.setTitle("Admin Page");
                stage.setScene(adminScene);
            }
        });

        loggedOutBuyer.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                root.getChildren().clear();
                LoginPane loginPane2 = new LoginPane(LoadBuyers.getBuyers(), LoadAdmins.getAdmins(), LoadSellers.getSellers());
                root.getChildren().add(loginPane2);
                stage.setTitle("Login");
                stage.setScene(scene);
                loggedInAs.bind(loginPane2.getLoggedInAs());
                createAccountRequested.bind(loginPane2.getCreateAccountRequested());
            }

        });
        loggedOutSeller.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                root.getChildren().clear();
                LoginPane loginPane2 = new LoginPane(LoadBuyers.getBuyers(), LoadAdmins.getAdmins(), LoadSellers.getSellers());
                root.getChildren().add(loginPane2);
                stage.setTitle("Login");
                stage.setScene(scene);
                loggedInAs.bind(loginPane2.getLoggedInAs());
                createAccountRequested.bind(loginPane2.getCreateAccountRequested());
            }

        });
        loggedOutAdmin.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 1) {
                root.getChildren().clear();
                LoginPane loginPane2 = new LoginPane(LoadBuyers.getBuyers(), LoadAdmins.getAdmins(), LoadSellers.getSellers());
                root.getChildren().add(loginPane2);
                stage.setTitle("Login");
                stage.setScene(scene);
                loggedInAs.bind(loginPane2.getLoggedInAs());
                createAccountRequested.bind(loginPane2.getCreateAccountRequested());
            }

        });
        stage.show();



    }

    public static void main(String[] args) {
        launch(args);
    }
}