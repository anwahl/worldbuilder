package com.wahlhalla.worldbuilder.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wahlhalla.worldbuilder.role.ERole;
import com.wahlhalla.worldbuilder.role.Role;
import com.wahlhalla.worldbuilder.role.RoleRepository;
import com.wahlhalla.worldbuilder.security.payload.request.EmailChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.LoginRequest;
import com.wahlhalla.worldbuilder.security.payload.request.PasswordChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.SignupRequest;
import com.wahlhalla.worldbuilder.security.payload.response.MessageResponse;
import com.wahlhalla.worldbuilder.security.payload.response.UserInfoResponse;
import com.wahlhalla.worldbuilder.user.User;
import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;
import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;
import com.wahlhalla.worldbuilder.world.WorldRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	WorldRepository worldRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	
		SecurityContextHolder.getContext().setAuthentication(authentication);
	
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
	
		List<String> roles = userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList());
	
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
			.body(new UserInfoResponse(userDetails.getId(),
									   userDetails.getUsername(),
									   userDetails.getEmail(),
									   roles));
	  }

	@PutMapping("/changePassword/{userId}")
	@PreAuthorize("hasRole('ADMIN') or @checkUser.byUserId(authentication, #userId)")
	public ResponseEntity<?> changePassword(@Validated @RequestBody PasswordChangeRequest passwordChangeRequest, @PathVariable Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			try {
				User user = this.userRepository.findById(userDetails.getId()).get();

				if (encoder.matches(passwordChangeRequest.getOldPassword(), user.getPassword())) {
					user.setPassword(encoder.encode(passwordChangeRequest.getNewPassword()));
					this.userRepository.save(user);
					return ResponseEntity.ok(new MessageResponse("Password changed successfully!"));
				} else {
					return ResponseEntity.badRequest().body(new MessageResponse("Password is incorrect!"));
				}
			} catch (EntityNotFoundException e) {
				return ResponseEntity.badRequest().body(new MessageResponse("User not found! " + e));
			}
		} else {
			return ResponseEntity.badRequest().body(new MessageResponse("User was not valid!"));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ADMIN":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "MODERATOR":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new MessageResponse("You've been signed out!"));
	}

	@DeleteMapping("/deleteUser/{userId}")
	@PreAuthorize("hasRole('ADMIN') or @checkUser.byUserId(authentication, #userId)")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		User user = this.userRepository.findById(userId).get();
		this.userRepository.delete(user);

		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new MessageResponse("You've been signed out!"));
	}

	@PutMapping("/changeEmail/{userId}")
	@PreAuthorize("hasRole('ADMIN') or @checkUser.byUserId(authentication, #userId)")
	public ResponseEntity<?> changeEmail(@Validated @RequestBody EmailChangeRequest emailChangeRequest, @PathVariable Long userId) {
		if (userRepository.existsByEmail(emailChangeRequest.getNewEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		} 

		User user = this.userRepository.findById(userId).get();
		user.setEmail(emailChangeRequest.getNewEmail());
		this.userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Email changed successfully!"));
	}
}
