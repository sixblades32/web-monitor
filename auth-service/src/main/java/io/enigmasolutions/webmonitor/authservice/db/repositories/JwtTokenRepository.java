package io.enigmasolutions.webmonitor.authservice.db.repositories;

import io.enigmasolutions.webmonitor.authservice.db.models.documents.JwtToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JwtTokenRepository extends MongoRepository<JwtToken, String> {

  Optional<JwtToken> findJwtTokenByValue(String value);
}
