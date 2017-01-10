package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by boun on 1/4/17.
 */
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue
    Integer id;

    @Column(nullable = false)
    String manufacturer;

    @Column(nullable = false)
    String brand;

    @Column(nullable = false)
    public String name;

    @ManyToOne
    public User user;

    public Phone() {
    }

    public Phone(String manufacturer, String brand, String name, User user) {
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.name = name;
        this.user = user;
    }
}
