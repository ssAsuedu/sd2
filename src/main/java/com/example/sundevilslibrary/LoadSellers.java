package com.example.sundevilslibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadSellers {
    public static ArrayList<Seller> sellers = new ArrayList<Seller>();


    public static ArrayList<Seller> readSellersFromFile(String filepath) throws IOException {

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
                               Seller seller = new Seller(idData, passData, nameData);
                                sellers.add(seller);
                                nameData = "";
                                idData = "";
                                passData = "";

                            }
                        }
                    }

                    continue;
                }


            }




            for (Seller seller : sellers) {
                System.out.println("Name: " + seller.getName());
                System.out.println("ASU ID: " + seller.getID());
                System.out.println("Password: " + seller.getPassword());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return sellers;
    }

    public static ArrayList<Seller> getSellers() {

        return sellers;
    }
}
