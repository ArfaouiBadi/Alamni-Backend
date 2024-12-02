package com.example.alamnibackend.controllers;

import org.springframework.security.access.prepost.PreAuthorize; // Import PreAuthorize for role-based access control
import org.springframework.web.bind.annotation.CrossOrigin; // Import CrossOrigin for handling CORS
import org.springframework.web.bind.annotation.GetMapping; // Import GetMapping for handling GET requests
import org.springframework.web.bind.annotation.RequestMapping; // Import RequestMapping for defining request paths
import org.springframework.web.bind.annotation.RestController; // Import RestController for creating RESTful controllers

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests from any origin for 1 hour
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/test") // Base URL for test-related endpoints
public class TestController {

	@GetMapping("/all") // Map GET requests to "/api/test/all"
	public String allAccess() {
		return "Public Content.";
	}}
