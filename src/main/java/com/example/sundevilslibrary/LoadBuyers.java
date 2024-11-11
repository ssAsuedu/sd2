package com.example.sundevilslibrary;

import com.example.sundevilslibrary.Buyer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class LoadBuyers {
//    private final static URL BUYER_FILE_URL = getClass().getResource("/users/Buyers.txt");
    public static ArrayList<Buyer> buyers = new ArrayList<Buyer>();


    public static ArrayList<Buyer> readBuyersFromFile(String filepath) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            String line;
            String nameData = "";
            String idData = "";
            String passData = "";

            // loops through each object in text file and adds to array list
            while ((line = reader.readLine()) != null) {


                if (line.contains("name")){
                    String[] keyValuePair = line.split(":");
                    nameData = keyValuePair[1].trim();
                    line = reader.readLine();
                    if (line.contains("id")){
                        keyValuePair = line.split(":");
                        idData = keyValuePair[1].trim();
                        line = reader.readLine();
                        if (line.contains("password")){
                            keyValuePair = line.split(":");
                            passData = keyValuePair[1].trim();
                            if (nameData != "" && idData != "" && passData != ""){
                                Buyer buyer = new Buyer(idData, passData, nameData);
                                buyers.add(buyer);
                                nameData = "";
                                idData = "";
                                passData = "";

                            }
                        }
                    }

                   continue;
                }


            }




            for (Buyer buyer : buyers) {
                System.out.println("Name: " + buyer.getName());
                System.out.println("ASU ID: " + buyer.getID());
                System.out.println("Password: " + buyer.getPassword());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return buyers;
    }

    public static ArrayList<Buyer> getBuyers() {

        return buyers;
    }
}
