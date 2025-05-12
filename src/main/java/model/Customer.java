package model;

import java.io.Serializable;

public class Customer implements Serializable {
    private int clientNum;       // מזהה ייחודי
    private String name;
    private String phone;
    private String address;
    private String notes;

    public Customer(int clientNum, String name, String phone, String address, String notes) {
        this.clientNum = clientNum;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.notes = notes;
    }

    public int getClientNum() { return clientNum; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }
}
