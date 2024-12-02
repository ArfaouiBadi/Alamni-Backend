package com.example.alamnibackend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Document(collection = "verification_tokens")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {

    @Id
    private String id;
    private String token;
    @DBRef
    private User user;
    private Date expiryDate;
}