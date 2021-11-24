package io.enigmasolutions.webmonitor.webbroadcastservice.controllers;

import io.enigmasolutions.dictionarymodels.CustomerTheme;
import io.enigmasolutions.webmonitor.webbroadcastservice.services.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/theme")
  public Mono<CustomerTheme> getCustomerTheme() {
    return customerService.retrieveCustomerTheme();
  }
}
