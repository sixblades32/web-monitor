package io.enigmasolutions.webmonitor.authservice.models.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenDto {

    private String jwt;
}
