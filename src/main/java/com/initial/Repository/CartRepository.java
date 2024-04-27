package com.initial.Repository;

import com.initial.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart , Long> {
}
