package com.initial.Repository;

import com.initial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {

    public  User findByEmail(String emial);
}
