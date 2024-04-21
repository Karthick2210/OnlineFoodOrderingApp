package com.initial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.initial.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String firstName;


    private String email;


    private String password;


    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "customer")//avoid creation of separate table
    private List<Order> orders =  new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favourite =  new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)//when ever we delete the user address should also be removed
    private List<Address> addresses = new ArrayList<>();

}
