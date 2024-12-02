package com.example.alamnibackend.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.example.alamnibackend.models.ERole;
import com.example.alamnibackend.models.Role;
import com.example.alamnibackend.models.User;
import com.example.alamnibackend.models.VerificationToken;
import com.example.alamnibackend.payload.request.LoginRequest;
import com.example.alamnibackend.payload.request.SignupRequest;
import com.example.alamnibackend.payload.response.EmailVerificationResponse;
import com.example.alamnibackend.payload.response.JwtResponse;
import com.example.alamnibackend.payload.response.MessageResponse;
import com.example.alamnibackend.repository.RoleRepository;
import com.example.alamnibackend.repository.UserRepository;
import com.example.alamnibackend.repository.VerificationTokenRepository;
import com.example.alamnibackend.security.jwt.JwtUtils;
import com.example.alamnibackend.security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/auth") // Base URL for authentication-related endpoints
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager; // Handles user authentication

	@Autowired
	UserRepository userRepository; // Repository for user-related database operations

	@Autowired
	RoleRepository roleRepository; // Repository for role-related database operations

	@Autowired
	PasswordEncoder encoder; // Encoder for password hashing

	@Autowired
	JwtUtils jwtUtils; // Utility for generating JWT tokens

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private JavaMailSender mailSender;
	/**
	 * Authenticate user and return a JWT token if successful.
	 *
	 * @param loginRequest The login request containing username and password.
	 * @return A ResponseEntity containing the JWT response or an error message.
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println("Sign in");
		// Authenticate the user with the provided username and password
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
						loginRequest.getPassword()));

		// Set the authentication in the security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Generate JWT token based on the authentication
		String jwt = jwtUtils.generateJwtToken(String.valueOf(authentication));
		System.out.println(jwt);
		// Get user details from the authentication object
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// Extract user roles into a list
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		// Return a response containing the JWT and user details
		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
	}

	/**
	 * Register a new user account.
	 *
	 * @param signUpRequest The signup request containing user details.
	 * @return A ResponseEntity indicating success or error message.
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		System.out.println("Sign up");
		System.out.println(signUpRequest.getUsername());
		// Check if the username is already taken
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		// Check if the email is already in use
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create a new user's account
		User user = new User(
				signUpRequest.getEmail(),
				signUpRequest.getFirstName(),
				signUpRequest.getLastName(),
				signUpRequest.getUsername(),
				ERole.ROLE_USER,
				encoder.encode(signUpRequest.getPassword()), // Encode the password
				signUpRequest.getDateOfBirth()
		);

		// Assign the default role "ROLE_USER"
		System.out.println("Role: " + roleRepository.findAll());
		System.out.println("Role: " + roleRepository.findByName(ERole.ROLE_USER));
		System.out.println("Users :"+userRepository.findAll());
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		user.setRoles(Set.of(userRole));

		// Save the user to the database
		userRepository.save(user);

		// Generate verification token
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + 86400000)); // 24 hours
		tokenRepository.save(verificationToken);

		// Send verification email
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setText("To confirm your account, please click here : "
				+ "http://localhost:8000/api/auth/confirm-account?token=" + token);
		mailSender.send(mailMessage);
		// Return a success message upon successful registration
		return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for verification."));
	}
	@GetMapping("/confirm-account")
	public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String token) {
		VerificationToken verificationToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid token"));

		User user = verificationToken.getUser();
		System.out.println(user.isEnabled());
		user.setEnabled(true);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Account verified successfully!"));
	}

	/**
	 * Check if the user's email is verified (enabled).
	 *
	 * @param email The email of the user to check.
	 * @return A ResponseEntity indicating whether the user is enabled or not.
	 */
	@GetMapping("/check-email-verification")
	public ResponseEntity<?> checkEmailVerification(@RequestParam("email") String email) {
		Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			boolean isEnabled = user.isEnabled();
			return ResponseEntity.ok(new EmailVerificationResponse("User enabled status: " + isEnabled, isEnabled));
		} else {
			return ResponseEntity.badRequest().body(new EmailVerificationResponse("Error: User not found.", false));
		}
	}
}
