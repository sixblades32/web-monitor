package io.enigmarobotics.discordbroadcastservice.db.repositories;


import io.enigmarobotics.discordbroadcastservice.db.models.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {


}
