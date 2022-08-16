package com.educational_app.model;

public class Order {
    private  String ProductId;
    private  String ImageUrl;
    private String ProductName;
    private String InstructorName;
    private String Quantity;
    private String Price;


    public Order() {
    }

    public Order(String productId, String imageUrl, String productName, String instructorName, String quantity, String price) {
        ProductId = productId;
        ImageUrl = imageUrl;
        ProductName = productName;
        InstructorName = instructorName;
        Quantity = quantity;
        Price = price;

    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getInstructorName() {
        return InstructorName;
    }

    public void setInstructorName(String instructorName) {
        InstructorName = instructorName;
    }
}
