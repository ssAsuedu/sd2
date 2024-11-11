package com.example.sundevilslibrary;

public class Admin {
    private String ASU_ID;
    private String password;
    private String name;

    public Admin(String ID, String passwd, String name1){
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
