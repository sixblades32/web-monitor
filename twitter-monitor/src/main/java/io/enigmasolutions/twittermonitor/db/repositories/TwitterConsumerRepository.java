package io.enigmasolutions.twittermonitor.db.repositories;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterConsumer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterConsumerRepository extends MongoRepository<TwitterConsumer, String> {
}
