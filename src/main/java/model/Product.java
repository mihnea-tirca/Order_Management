package model;

public class Product {
    private int id;
    private String name;
    private int stock;
    private double price;

    public Product(){
        this.id = -1;
        this.name = "";
        this.stock = -1;
        this.price = -1;
    }

    public Product(int id){
        super();
        this.id = id;
    }

    public Product(String name, int stock, double price) {
        super();
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Product(int id, String name, int stock, double price){
        this(name, stock, price);
        this.id = id;
    }

    public Product(String name, int stock){
        this();
        this.name = name;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
