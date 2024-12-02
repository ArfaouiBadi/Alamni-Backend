package com.example.alamnibackend.repository;

import java.util.Optional; // Import Optional for handling optional values

import com.example.alamnibackend.models.ERole; // Import EmployeeRole enumeration
import com.example.alamnibackend.models.Role; // Import Role model
import org.springframework.data.mongodb.repository.MongoRepository; // Import MongoRepository for MongoDB operations

/**
 * Repository interface for accessing Role entities in the MongoDB database.
 * It extends MongoRepository, providing CRUD operations for Role objects.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
