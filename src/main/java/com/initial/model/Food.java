package com.initial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Food {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String foodName;

    private String description;

    private Long price;

    @ManyToOne
    private Category foodCategory;

    @Column(length =1000)
    @ElementCollection
    private List<String> images;//create separate table for food images

    private boolean available;

    @ManyToOne
    private Restaurant restaurant;


    private boolean isVegetarian;

    private boolean isSeasonal;

    @ManyToMany
    private List<Ingredients> ingredients = new ArrayList<>();

    private Date creationDate;





}
