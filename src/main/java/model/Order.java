package model;

import bll.ClientBLL;
import bll.ProductBLL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Order {
    private int id;
    private int clientID;
    private int productID;
    private int quantity;

    public Order(){
        this.id = -1;
        this.clientID = -1;
        this.productID = -1;
        this.quantity = -1;
    }

    public Order(int id){
        this();
        this.id = id;
    }

    public Order(int clientID, int productID, int quantity) {
        this();
        this.clientID = clientID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public Order(int id, int clientID, int productID, int quantity){
        this(clientID, productID, quantity);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * exports this order to a text file in src/main/java/orders/
     */
    public void exportToTXT(){
        String filename = "src/main/java/orders/order_" + this.id + ".txt";
        try{
            File myFile = new File(filename);
            myFile.createNewFile();
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(this.toString());
            myWriter.close();
        } catch (IOException ignored){}

    }

    @Override
    public String toString() {
        Client client = new ClientBLL().findClientByID(clientID);
        Product product = new ProductBLL().findProductByID(productID);

        return  "Order ID: " + id +
                "\nClient ID: " + clientID + "; Client Name: " + client.getName() +
                "\nProduct ID: " + productID + "; Product Name: " + product.getName() + "; Price: " + product.getPrice() +
                "\nQuantity: " + quantity +
                "\n\nTotal Price: " + product.getPrice() * quantity;
    }
}
