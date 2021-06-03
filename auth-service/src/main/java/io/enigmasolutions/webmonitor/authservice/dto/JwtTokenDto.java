package io.enigmasolutions.webmonitor.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {

    private String jwt;
}
