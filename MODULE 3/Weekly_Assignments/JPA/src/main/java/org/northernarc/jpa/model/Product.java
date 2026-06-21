package org.northernarc.jpa.model;

public class Product {
    private int product_id;
    private String product_name;
    private Double price;

    public Product(){};

    public Product(String product_name, Double price){
        this.product_name=product_name;
        this.price=price;
    }

    public Product(int product_id, String product_name, Double price){
        this.product_id=product_id;
        this.product_name=product_name;
        this.price=price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public String toString(){
        return ("ID:"+product_id+" NAME:"+product_name+" Price:"+price);
    }
}

