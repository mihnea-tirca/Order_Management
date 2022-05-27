package model;

public class Client {
    private int id;
    private String name;
    private String address;
    private String email;

    public Client(){
        this.id = -1;
        this.name = "";
        this.address = "";
        this.email = "";
    }

    public Client(int id){
        this();
        this.id = id;
    }

    public Client(String name, String address, String email) {
        this();
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public Client(int id, String name, String address, String email){
        this(name, address, email);
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
