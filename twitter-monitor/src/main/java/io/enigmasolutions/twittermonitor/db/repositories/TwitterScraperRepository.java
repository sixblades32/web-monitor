package io.enigmasolutions.twittermonitor.db.repositories;

import io.enigmasolutions.twittermonitor.db.models.documents.TwitterScraper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterScraperRepository extends MongoRepository<TwitterScraper, String> {
}
