package com.jpademo.controller;

import com.jpademo.dto.*;
import com.jpademo.model.Customer;
import com.jpademo.model.CustomerRelation;
import com.jpademo.service.CustomerService;
import com.jpademo.util.CustomerSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable("id") Integer id){

        Customer customer = customerService.getById(id);
        return modelMapper.map(customer, CustomerDto.class);
    }

    @GetMapping("/{id}/with-orders")
    public CustomerWithOrdersDto getCustomerAndOrders(@PathVariable("id") Integer id){

        Customer customer = customerService.getCustomerWithGraphById(id, CustomerRelation.CUSTOMER_WITH_ORDERS);
        CustomerWithOrdersDto customerWithOrders = modelMapper.map(customer, CustomerWithOrdersDto.class);
        List<OrderDto> orders = customer.getOrders().stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        customerWithOrders.setOrders(orders);
        return customerWithOrders;
    }

    @GetMapping("/{id}/with-orders-and-details")
    public CustomerWithOrdersDto getCustomerWithOrdersAndDetails(@PathVariable("id") Integer id){

        Customer customer = customerService.getCustomerWithGraphById(id, CustomerRelation.CUSTOMER_WITH_ORDERS_AND_DETAILS);
        CustomerWithOrdersDto customerDto = modelMapper.map(customer, CustomerWithOrdersDto.class);
        List<OrderDto> orders = customer.getOrders().stream()
                .map(order -> {
                    OrderWithDetailDto orderWithDetails = modelMapper.map(order, OrderWithDetailDto.class);
                    List<OrderDetailDto> orderDetails = order.getOrderDetail().stream().map(orderDetail -> {
                        OrderDetailDto orderDetailDto = new OrderDetailDto();
                        orderDetailDto.setOrderLineNumber(orderDetail.getOrderLineNumber());
                        orderDetailDto.setPriceEach(orderDetail.getPriceEach());
                        orderDetailDto.setProductCode(orderDetail.getProduct().getProductCode());
                        orderDetailDto.setQuantityOrdered(orderDetail.getQuantityOrdered());
                        return orderDetailDto;
                    }).collect(Collectors.toList());

                    orderWithDetails.setOrderDetails(orderDetails);
                    return orderWithDetails;
                }).collect(Collectors.toList());
        customerDto.setOrders(orders);
        return customerDto;
    }

    @GetMapping("/customers/with-orders")
    public List<Customer> getCustomersWithOrders() {
        Specification<Customer> spec = CustomerSpecifications.hasPlacedOrder();
        return customerService.getCustomersWithOrders(spec);
    }


}
