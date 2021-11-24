package io.enigmasolutions.webmonitor.dictionaryservice.db.repositories;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findCustomerByDiscordGuild_GuildId(String guildId);
}
