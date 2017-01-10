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
    public String manufacturer;

    @Column(nullable = false)
    public String brand;

    @ManyToOne
    public User user;

    public Phone() {
    }

    public Phone(String manufacturer, String brand, User user) {
        this.manufacturer = manufacturer;
        this.brand = brand;
        this.user = user;
    }
}
