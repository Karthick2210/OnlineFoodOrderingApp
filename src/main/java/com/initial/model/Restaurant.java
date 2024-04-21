package com.initial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long  id;

    @OneToOne
    private User owner;

    private String name;

    private String description;

    @OneToOne
    private Address address;

    @Embedded
    private ContactInformation contactInformation;

    private String openingTime;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)//don't create a separate table
    private List<Order> orders =  new ArrayList<>();

    @ElementCollection
    @Column(length = 1000)
    private  List<String> images;

    private Date registrationDate;

    private boolean open;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Food> foods =  new ArrayList<>();


}