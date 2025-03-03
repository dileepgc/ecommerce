package com.shop.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User extends Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    @JsonProperty("Email")
    private String email;
    private String password;



    private String role;




    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> ord;



    public User(String firstName, String lastName, String password, String role,String email) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.role=role;
        this.email=email;
    }

//    public User(String firstName, String lastName, String password, String role, Object o, String email) {
//        this.firstName=firstName;
//        this.lastName=lastName;
//        this.password=password;
//        this.role=role;
//        this.email=email;
//    }

    public List<Order> getOrd() {
        return ord;
    }

    public void setOrd(List<Order> ord) {
        this.ord = ord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public User(String name, String email, String password) {

        this.email = email;
        this.password = password;

    }

    public User() {}

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public User(String created_at, String updated_at, boolean is_deleted, String created_by, String updated_by, String firstName, String lastName, String email, String password, String role) {
        super(created_at, updated_at, is_deleted, created_by, updated_by);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setWallet(Wallet wallet) {
    }


}
