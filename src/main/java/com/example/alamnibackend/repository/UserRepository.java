package com.example.alamnibackend.repository;

import java.util.Optional; // Import Optional for handling optional values

import com.example.alamnibackend.models.User; // Import User model
import org.springframework.data.mongodb.repository.MongoRepository; // Import MongoRepository for MongoDB operations

/**
 * Repository interface for accessing User entities in the MongoDB database.
 * It extends MongoRepository, providing CRUD operations for User objects.
 */
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  User findByEmail(String email);
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
