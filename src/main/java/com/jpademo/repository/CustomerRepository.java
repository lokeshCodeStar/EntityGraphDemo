package com.jpademo.repository;

import com.jpademo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer, Integer>,
        BaseRepository<Customer, Integer> , JpaSpecificationExecutor<Customer>{


}
