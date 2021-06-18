package io.enigmasolutions.webmonitor.authservice.controllers;

import io.enigmasolutions.webmonitor.authservice.models.JwtTokenDto;
import io.enigmasolutions.webmonitor.authservice.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private final CustomerService customerService;

    @Autowired
    public GuestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/token")
    public JwtTokenDto generateJwtToken() {
        return customerService.generateJwtToken();
    }
}
