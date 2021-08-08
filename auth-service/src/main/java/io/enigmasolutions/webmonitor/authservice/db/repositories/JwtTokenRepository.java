package io.enigmasolutions.webmonitor.authservice.db.repositories;

import io.enigmasolutions.webmonitor.authservice.db.models.documents.JwtToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JwtTokenRepository extends MongoRepository<JwtToken, String> {

    Optional<JwtToken> findJwtTokenByValue(String value);
}
