package io.enigmasolutions.webmonitor.authservice.controllers;

import io.enigmasolutions.webmonitor.authservice.dto.JwtTokenDto;
import io.enigmasolutions.webmonitor.authservice.services.JwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/generate")
    public JwtTokenDto generateJwtToken() {
        return jwtService.generateJwtToken();
    }

    @PutMapping("/refresh")
    public JwtTokenDto refreshJwtToken(
            @RequestHeader("Authorization") String jwtToken
    ) {
        return jwtService.refreshJwtToken(jwtToken);
    }
}
