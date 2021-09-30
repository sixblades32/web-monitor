package io.enigmasolutions.webmonitor.dictionaryservice.db.repositories;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents.MonitoringTarget;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MonitoringTargetRepository extends
    ReactiveMongoRepository<MonitoringTarget, String> {

}
