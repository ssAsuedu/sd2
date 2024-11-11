package com.example.sundevilslibrary;

public class Book {
    String title;
    String category;
    Double price;
    String condition;


    public Book(String title1, String category1, String condition1, Double price1){
        this.title = title1;
        this.category = category1;
        this.condition = condition1;
        this.price = price1;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setPrice(Double price){
        this.price = price;
    }
    public void setCondition(String condition){
        this.condition = condition;
    }
    public String getTitle(){
        return this.title;
    }
    public String getCategory(){
        return this.category;
    }
    public String getCondition(){
        return this.condition;
    }
    public Double getPrice(){
        return this.price;
    }
}
