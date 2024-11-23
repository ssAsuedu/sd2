package com.example.sundevilslibrary;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;


import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;


import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BuyerPage extends HBox{ // extends Parent
    ArrayList<Book> allBooks = new ArrayList<Book>();
    ArrayList<Book> soldBooks = new ArrayList<Book>();
    ArrayList<Book> cart = new ArrayList<Book>();
    Label TF = new Label("Search the Library");
    Font font = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 48);


    private IntegerProperty loggedOut = new SimpleIntegerProperty(0);


    int removed = 0;


    //    URL url = getClass().getResource("/Books/Books.txt");
    public BuyerPage(ArrayList<Book> books) {
        VBox filters = new VBox();
        VBox accessCart = new VBox(30);
        allBooks = books;
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20));
        TF.setFont(font);
        TF.getStyleClass().add("title");
        vbox.getChildren().add(TF);
        VBox bookBox = new VBox();


        displayBooks(books, bookBox );
        vbox.getChildren().addAll(bookBox);
        // filter functionality


        ToggleGroup groupFilter = new ToggleGroup();


        RadioButton AllBooks = new RadioButton("All Books");
        RadioButton NaturalScience = new RadioButton("Natural Science");
        RadioButton Computer = new RadioButton("Computer");
        RadioButton Math = new RadioButton("Math");
        RadioButton EnglishLanguage = new RadioButton("English Language");
        RadioButton Other = new RadioButton("Other");


        AllBooks.setToggleGroup(groupFilter);
        AllBooks.setSelected(true);
        NaturalScience.setToggleGroup(groupFilter);
        Computer.setToggleGroup(groupFilter);
        Math.setToggleGroup(groupFilter);
        EnglishLanguage.setToggleGroup(groupFilter);
        Other.setToggleGroup(groupFilter);


        groupFilter.selectedToggleProperty().addListener(observable -> {
            RadioButton selectedRadioButton = (RadioButton) groupFilter.getSelectedToggle();
            String selectedValue = selectedRadioButton.getText();
            if (selectedValue.equals("All Books")){
                bookBox.getChildren().clear();


                displayBooks(books, bookBox );


            } else {
                ArrayList<Book> filteredBooks = new ArrayList<Book>();
                for (Book book: books){
                    if (book.getCategory().equals(selectedValue)){


                        filteredBooks.add(book);
                    }
                }
                bookBox.getChildren().clear();
                if (filteredBooks.size() == 0){
                    bookBox.getChildren().add(new Label("0 results - No books match that category"));
                } else {
                    displayBooks(filteredBooks, bookBox);
                }
            }
//            vbox.getChildren().addAll(bookBox);
        });


        Label Filters = new Label("Filters");
        Filters.getStyleClass().add("title");










        //
        Button viewCart = new Button("View My Cart");
        viewCart.getStyleClass().add("viewCart");
        accessCart.getChildren().add(viewCart);
//        accessCart.setAlignment(Pos.TOP_CENTER);
//        accessCart.setPadding(new Insets(20));
        viewCart.setOnAction(event -> {
            this.getChildren().remove(1);
            accessCart.getChildren().remove(0);
            // filters.getChildren().clear();
            this.getChildren().remove(filters);
            VBox filters2 = new VBox(30);
            this.getChildren().add(0,filters2);
            TF.setText("Complete Your Purchase");
            accessCart.getChildren().add(TF);


            displayCart(cart, accessCart);


            calculateTotal(cart, accessCart);
            checkEmptyCart(cart, accessCart);




            Button backtoSearch = new Button("Return to Search");
            backtoSearch.getStyleClass().add("viewCart");


            backtoSearch.setOnAction(newEvent -> {
                removed = 0;
                vbox.getChildren().clear();
                AllBooks.setSelected(true);
                TF.setText("Search The Library");
                vbox.getChildren().add(TF);
                bookBox.getChildren().clear();
                displayBooks(books, bookBox);
                vbox.getChildren().add(bookBox);
                this.getChildren().add(1, vbox);
                this.getChildren().remove(filters2);
                this.getChildren().add(0, filters);
//                filters.getChildren().clear();
//                filters.getChildren().addAll(Filters, AllBooks, NaturalScience, Computer, Math, EnglishLanguage, Other);








                accessCart.getChildren().clear();
                accessCart.getChildren().add(viewCart);


                filters.setAlignment(Pos.CENTER);
                filters.setPadding(new Insets(20));
                filters.setStyle("-fx-spacing: 20px;");
            });


            filters2.getChildren().add(backtoSearch);
            filters2.setAlignment(Pos.TOP_CENTER);
            filters2.setPadding(new Insets(20));
        });


        accessCart.setAlignment(Pos.TOP_CENTER);
        accessCart.setPadding(new Insets(20));
        //


        Button logOff = new Button("Log Out");
        logOff.setOnAction(event -> {
            loggedOut.set(1);
        });


        logOff.getStyleClass().add("viewCart");




        filters.getChildren().addAll(logOff, Filters, AllBooks, NaturalScience, Computer, Math, EnglishLanguage, Other);




        filters.setAlignment(Pos.CENTER);
        filters.setPadding(new Insets(20));
        filters.setStyle("-fx-spacing: 20px;");
        this.setAlignment(Pos.CENTER_LEFT);


        this.getChildren().addAll(filters, vbox, accessCart);








    }






    public VBox displayBooks(ArrayList<Book> books, VBox vbox){
        if (books.size()==0){
            checkEmptyCart(books, vbox);
            return vbox;
        }
        final int ITEMS_PER_PAGE = 5;
        int totalPages = (int) Math.ceil((double) books.size() / ITEMS_PER_PAGE);


        // Create Pagination control
        Pagination pagination = new Pagination(totalPages);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                VBox pageContent = new VBox(10);


                int startIndex = pageIndex * ITEMS_PER_PAGE;
                int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, books.size());


                // Add buyer information for current page
                for (int i = startIndex; i < endIndex; i++) {
                    Book book = books.get(i);
                    HBox userItem = createBookItem(book);
                    pageContent.getChildren().add(userItem);
                }


                return pageContent;
            }
        });
        vbox.getChildren().add(pagination);


        return vbox;
    };


    public VBox displayCart(ArrayList<Book> books, VBox vbox){
        for (Book book: books){
            HBox bookItem = new HBox(140);
            VBox leftPane = new VBox(20);
            VBox rightPane = new VBox(20);


            Label title = new Label(book.getTitle());
            Label condition = new Label(book.getCondition());
            Label category = new Label(book.getCategory());
            Label price = new Label("$" + book.getPrice().toString());


            leftPane.getChildren().addAll(title, condition, category);


            Button addToCart = new Button("Remove");
            addToCart.setOnAction(event -> {
                removed += 1;
                cart.remove(book);
                vbox.getChildren().remove(vbox.getChildren().size()-1);


                calculateTotal(cart, vbox);
                checkEmptyCart(cart, vbox);
                Button removed = (Button)event.getTarget();
                HBox parent =(HBox) removed.getParent().getParent();
                VBox parent2 = (VBox) parent.getParent();
                if (parent2 != null){
                    parent2.getChildren().remove(parent);
                }


                System.out.printf("Cart Contains: %d\n", cart.size());
                for (Book books2: cart){


                    System.out.printf("%s\n", books2.getTitle());
                }
                addToCart.setText("Added");
                addToCart.getStyleClass().clear();
                addToCart.setAlignment(Pos.CENTER);
                addToCart.getStyleClass().add("added");
            });
            addToCart.getStyleClass().add("addToCart");
            addToCart.setAlignment(Pos.CENTER);
            rightPane.getChildren().addAll(price, addToCart);
//            rightPane.setPrefWidth(70);
//            leftPane.setPrefWidth(70);
            leftPane.setAlignment(Pos.CENTER_LEFT);
            rightPane.setAlignment(Pos.CENTER_RIGHT);
            rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
            bookItem.setAlignment(Pos.CENTER);
            bookItem.getChildren().addAll(leftPane, rightPane);
            bookItem.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            bookItem.setPadding(new Insets(10,0,10,0));


            bookItem.getStyleClass().add("bookCard");
            vbox.getChildren().add(bookItem);
            vbox.setMargin(bookItem, new Insets(10, 0, 10, 0));




        }
        return vbox;
    }


    public void checkEmptyCart(ArrayList<Book> books, VBox cartBox){
        if (books.size() == 0){
            Label emptyCart = new Label("You currently have no items in your cart.");
            cartBox.getChildren().clear();
            cartBox.getChildren().addAll(TF, emptyCart);
            cartBox.setPadding(new Insets(20));
            cartBox.setAlignment(Pos.TOP_CENTER);
        }
    }
    public void calculateTotal(ArrayList<Book> books, VBox subtotalBox){
        if(removed > 0) {
            subtotalBox.getChildren().remove(subtotalBox.getChildren().size() - 1);
        }
        VBox subBox = new VBox(20);
        DecimalFormat formatPrices = new DecimalFormat("#.##");
        Double Subtotal = 0.00;
        Double total = 0.00;
        for (Book book: books){
            Subtotal += book.getPrice();
        }
        String SubtotalString = formatPrices.format(Subtotal);
        Label yourSubTotal = new Label("Subtotal:\t\t$" + SubtotalString);
        Double tax = Subtotal * 0.2;
        String TaxString = formatPrices.format(tax);
        Label yourTax = new Label("Tax:\t\t\t$" + TaxString);
        total = Subtotal + tax;
        String TotalString = formatPrices.format(total);
        Label yourTotal = new Label("Total:\t\t\t$" + TotalString);
        subBox.getChildren().addAll(yourSubTotal, yourTax, yourTotal);




        Button checkout = new Button("Complete Purchase");
        checkout.getStyleClass().add("viewCart");


//        subBox.getChildren().add(checkout);
        checkout.setOnAction(event -> {
            for (Book book: cart){
                soldBooks.add(book);


            }
            cart.clear();
            subtotalBox.getChildren().clear();
            Label Completed = new Label("Your Purchase Was completed!");
            Completed.getStyleClass().add("title");
            subtotalBox.getChildren().add(Completed);
            System.out.printf("\n Sold Books: %d", soldBooks.size());
            writeToDatabase(event, books);
        });


        subBox.setPadding(new Insets(30,5,30,5));
        subBox.setAlignment(Pos.CENTER_LEFT);
        checkout.setPrefWidth(350);


        subtotalBox.getChildren().addAll(subBox, checkout);


    }
    //    EventHandler<ActionEvent> writeToDatabase = new EventHandler<ActionEvent>(){
    public void writeToDatabase(ActionEvent e, ArrayList<Book> books) {


        String filepath = "src/bookDatabase/SoldBooks.txt";
        String filepath2 = "src/bookDatabase/Books.txt";
        try {


            FileWriter fw = new FileWriter(filepath, true);




//                    out = new BufferedWriter(fw);
            for (Book book : soldBooks) {
                books.remove(book);
                allBooks.remove(book);
                System.out.printf("\nBook Title to Write: %s\n", book.getTitle());


                fw.write("{" + "\n" + "Title: " + book.getTitle() + "\n" + "Category: " + book.getCategory() + "\n" + "Price: " + book.getPrice().toString() + "\n" + "Condition: " + book.getCondition() + "\n" + "}" + "\n");




            }
            fw.close();




        } catch (IOException error) {
            System.out.println("Failed to open file SoldBooks.txt");
        }
        try{
            FileWriter fw = new FileWriter(filepath2);




            for (Book book : allBooks) {
                System.out.printf("\nBook Title to Remove: %s\n", book.getTitle());
                String newText = "{" + "\n" + "Title: " + book.getTitle() + "\n" + "Category: " + book.getCategory() + "\n" + "Price: " + book.getPrice().toString() + "\n" + "Condition: " + book.getCondition() + "\n" + "}" + "\n";
                fw.write(newText);
            }


            fw.close();


            System.out.println("Book title written to file: Books.txt ");
        } catch (IOException error) {
            System.out.println("Failed to open file Books.txt");
        }
    }
    public ObservableValue<? extends Number> getLoggedOut() {
        return loggedOut;
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
            Button addToCart = new Button("Add to Cart");
            if (cart.contains(book)){
                addToCart.setText("Added");
                addToCart.getStyleClass().clear();
                addToCart.setAlignment(Pos.CENTER);
                addToCart.getStyleClass().add("added");
            }


            addToCart.setOnAction(event -> {
                cart.add(book);


                System.out.printf("Cart Contains: %d\n", cart.size());
                for (Book books2: cart){


                    System.out.printf("%s\n", books2.getTitle());
                }
                addToCart.setText("Added");
                addToCart.getStyleClass().clear();
                addToCart.setAlignment(Pos.CENTER);
                addToCart.getStyleClass().add("added");
            });
            addToCart.getStyleClass().add("addToCart");
            addToCart.setAlignment(Pos.CENTER);
            rightPane.getChildren().addAll(price, addToCart);
//            rightPane.setPrefWidth(70);
//            leftPane.setPrefWidth(70);
            leftPane.setAlignment(Pos.CENTER_LEFT);
            rightPane.setAlignment(Pos.CENTER_RIGHT);
            rightPane.prefWidthProperty().bind(leftPane.prefWidthProperty());
            bookItem.setAlignment(Pos.CENTER);
            bookItem.getChildren().addAll(leftPane, rightPane);
            bookItem.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            bookItem.setPadding(new Insets(10,0,10,0));


            bookItem.getStyleClass().add("bookCard");

            return bookItem;
    }
};
