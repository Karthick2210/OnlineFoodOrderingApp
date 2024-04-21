package com.initial.Repository;

import com.initial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    public User findByEmail(String emial);

}
