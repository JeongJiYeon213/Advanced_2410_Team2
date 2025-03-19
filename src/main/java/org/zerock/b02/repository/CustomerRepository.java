package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.b02.domain.Customer;
import org.zerock.b02.repository.search.CustomerSearch;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, CustomerSearch {
   Optional<Customer> findByCustomerId(String customerName);
}