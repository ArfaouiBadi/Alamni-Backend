package com.example.alamnibackend.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @NotBlank
  @Size(max = 20)
  private String firstName;
  @NotBlank
  @Size(max = 20)
  private String lastName;
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
  @NotBlank
  @Size(max = 20)
  private String username;
  @NotBlank
  @Size(max = 120)
  private String password;
  @NotBlank
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;
  @DBRef
  private Set<Role> roles = new HashSet<>();
  private boolean enabled = false;

  public User(String email, String firstName, String lastName, String username, ERole role, String encode, LocalDate dateOfBirth) {
    this.username = username;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.password = encode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.roles = new HashSet<>();
    this.roles.add(new Role(role)); // Assuming Role has a constructor that accepts ERole
    this.enabled = false;
  }

}
