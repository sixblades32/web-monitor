package io.enigmasolutions.twittermonitor.db.repositories;

import io.enigmasolutions.twittermonitor.db.models.Target;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends MongoRepository<Target, String> {

}