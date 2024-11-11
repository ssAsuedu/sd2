package com.example.sundevilslibrary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadAdmins {
    public static ArrayList<Admin> admins = new ArrayList<Admin>();


    public static ArrayList<Admin> readAdminsFromFile(String filepath) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            String nameData = "";
            String idData = "";
            String passData = "";
            // loops through each object in text file and adds to array list

            while ((line = reader.readLine()) != null) {


                if (line.contains("name")) {
                    String[] keyValuePair = line.split(":");
                    nameData = keyValuePair[1].trim();
                    line = reader.readLine();
                    if (line.contains("id")) {
                        keyValuePair = line.split(":");
                        idData = keyValuePair[1].trim();
                        line = reader.readLine();
                        if (line.contains("password")) {
                            keyValuePair = line.split(":");
                            passData = keyValuePair[1].trim();
                            if (nameData != "" && idData != "" && passData != "") {
                                Admin admin = new Admin(idData, passData, nameData);
                                admins.add(admin);
                                nameData = "";
                                idData = "";
                                passData = "";

                            }
                        }
                    }

                    continue;
                }

            }
                for (Admin admin : admins) {
                    System.out.println("Name: " + admin.getName());
                    System.out.println("ASU ID: " + admin.getID());
                    System.out.println("Password: " + admin.getPassword());
                }


            } catch(IOException e){
                e.printStackTrace();
            }
            return admins;
        }

        public static ArrayList<Admin> getAdmins () {

            return admins;
        }
    }

