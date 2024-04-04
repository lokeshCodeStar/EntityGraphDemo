package com.jpademo.util;

import com.jpademo.model.Customer;
import com.jpademo.model.Order;
import com.jpademo.model.OrderDetail;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;

public class CustomerSpecifications {
    
    public static Specification<Customer> hasPlacedOrder() {
        return (root, query, criteriaBuilder) -> {
            // Join with the Orders table
            root.join("orders", JoinType.INNER);
            // Ensure that the customer has at least one order
            return criteriaBuilder.isNotEmpty(root.get("orders"));
        };
    }

    public static Specification<Customer> ordersPlacedBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            // Join with the Orders table
            Join<Customer, Order> orderJoin = root.join("orders", JoinType.INNER);
            // Create predicates for order date range
            Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(orderJoin.get("orderDate"), startDate);
            Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(orderJoin.get("orderDate"), endDate);
            // Combine predicates using AND
            return criteriaBuilder.and(startDatePredicate, endDatePredicate);
        };
    }

    public static Specification<Customer> ordersForProduct(String productCode) {
        return (root, query, criteriaBuilder) -> {
            // Join with the Orders table
            Join<Customer, Order> orderJoin = root.join("orders", JoinType.INNER);
            // Join with the OrderDetail table to access product details
            Join<Order, OrderDetail> orderDetailJoin = orderJoin.join("orderDetail", JoinType.INNER);
            // Create predicate for product code
            Predicate productCodePredicate = criteriaBuilder.equal(orderDetailJoin.get("product").get("productCode"), productCode);
            return productCodePredicate;
        };
    }




}
