package io.enigmarobotics.discordbroadcastservice.db.repositories;

import io.enigmarobotics.discordbroadcastservice.db.models.Target;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TargetRepository extends MongoRepository<Target, String> {

}
