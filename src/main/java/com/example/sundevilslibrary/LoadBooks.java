package com.example.sundevilslibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadBooks {

    public static ArrayList<Book> books = new ArrayList<Book>();

    public static ArrayList<Book> readBooksFromFile(String filepath) throws IOException {

        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            String titleData = "";
            String categoryData = "";
            String conditionData = "";
            String priceData = "";
            // loops through each object in text file and adds to array list

            while ((line = reader.readLine()) != null) {


                if (line.contains("Title")) {
                    String[] keyValuePair = line.split(":");
                    titleData = keyValuePair[1].trim();
                    line = reader.readLine();
                    if (line.contains("Category")) {
                        keyValuePair = line.split(":");
                        categoryData = keyValuePair[1].trim();
                        line = reader.readLine();
                        if (line.contains("Price")) {
                            keyValuePair = line.split(":");
                            priceData = keyValuePair[1].trim();
                            line = reader.readLine();
                            if (line.contains("Condition")) {
                                keyValuePair = line.split(":");
                                conditionData = keyValuePair[1].trim();
                                if (titleData != "" && priceData != "" && categoryData != "" && conditionData != "") {
                                    Book book = new Book(titleData, categoryData, conditionData,Double.parseDouble(priceData));
                                    books.add(book);
                                    titleData = "";
                                    categoryData = "";
                                    conditionData = "";
                                    priceData = "";

                                }
                            }

                        }
                    }

                    continue;
                }

            }
            for (Book book : books) {
                System.out.println("Title:  " + book.getTitle());
                System.out.println("Category:  " + book.getCategory());
                System.out.println("Condition: " + book.getCondition());
                System.out.println("Price:  " + book.getPrice());
            }


        } catch(IOException e){
            e.printStackTrace();
        }
        return books;
    }

    public static ArrayList<Book> getBooks() {

        return books;
    }
}
