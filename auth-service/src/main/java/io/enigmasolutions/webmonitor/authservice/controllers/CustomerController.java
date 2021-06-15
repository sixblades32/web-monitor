package io.enigmasolutions.webmonitor.authservice.controllers;

import io.enigmasolutions.webmonitor.authservice.models.JwtTokenDto;
import io.enigmasolutions.webmonitor.authservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PutMapping("/token")
    public JwtTokenDto refreshJwtToken() {
        return customerService.refreshJwtToken();
    }
}
