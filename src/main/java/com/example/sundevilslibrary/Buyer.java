package com.example.sundevilslibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Buyer implements Serializable {
    private String ASU_ID;
    private String password;
    private String name;

    public Buyer(String ID, String passwd, String name1){
        this.ASU_ID = ID;
        this.password = passwd;
        this.name = name1;
    }

    public String getName(){
        return this.name;
    }
    public String getID(){
        return this.ASU_ID;
    }
    public String getPassword(){
        return this.password;
    }
    public void setASU_ID(String id){
        this.ASU_ID = id;

    }
    public void setName(String newName){
        this.name = newName;
    }
    public void setPassword(String passwd){
        this.password = password;
    }

}
