package io.enigmasolutions.webmonitor.authservice.controllers;

import io.enigmasolutions.webmonitor.authservice.models.external.JwtTokenDto;
import io.enigmasolutions.webmonitor.authservice.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/{customerId}/token")
    public JwtTokenDto generateCustomerGuestJwtToken(
            @PathVariable String customerId,
            HttpServletRequest request
    ) {
        return guestService.generateJwtToken(request, customerId);
    }
}
