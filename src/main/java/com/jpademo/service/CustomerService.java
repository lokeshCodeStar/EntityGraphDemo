package com.jpademo.service;

import com.jpademo.model.Customer;
import com.jpademo.model.CustomerRelation;
import com.jpademo.repository.CustomerRepository;
import com.jpademo.util.CustomerSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer getById(Integer id) {
        return this.customerRepository.getOne(id);
    }

    public Customer getCustomerWithGraphById(Integer id, CustomerRelation relation) {
        return this.customerRepository.findWithGraph(id, relation.getName());
    }

    public List<Customer> getCustomersWithOrders(Specification<Customer> spec){
        return customerRepository.findAll(spec);
    }

    public List<Customer> findCustomersWithOrdersWithinDateRange(LocalDate startDate, LocalDate endDate) {
        Specification<Customer> spec = CustomerSpecifications.ordersPlacedBetween(startDate, endDate);
        return customerRepository.findAll(spec);
    }

    public List<Customer> findCustomersWithOrdersForProduct(String productCode) {
        Specification<Customer> spec = CustomerSpecifications.ordersForProduct(productCode);
        return customerRepository.findAll(spec);
    }


}
