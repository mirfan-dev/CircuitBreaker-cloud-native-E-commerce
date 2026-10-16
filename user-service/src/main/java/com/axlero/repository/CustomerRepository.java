package com.axlero.repository;

import com.axlero.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer,String> {

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}
