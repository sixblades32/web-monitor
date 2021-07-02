package io.enigmasolutions.twittermonitor.db.repositories;

import io.enigmasolutions.twittermonitor.db.models.documents.Target;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends MongoRepository<Target, String> {
    Target deleteTargetByIdentifier(String identifier);
}