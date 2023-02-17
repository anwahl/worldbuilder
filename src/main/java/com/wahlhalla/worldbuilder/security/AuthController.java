package com.wahlhalla.worldbuilder.security;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
import com.wahlhalla.worldbuilder.security.PasswordReset.PasswordResetToken;
import com.wahlhalla.worldbuilder.security.PasswordReset.PasswordResetTokenRepository;
import com.wahlhalla.worldbuilder.security.payload.request.EmailChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.LoginRequest;
import com.wahlhalla.worldbuilder.security.payload.request.PasswordChangeRequest;
import com.wahlhalla.worldbuilder.security.payload.request.SignupRequest;
import com.wahlhalla.worldbuilder.security.payload.response.MessageResponse;
import com.wahlhalla.worldbuilder.security.payload.response.UserInfoResponse;
import com.wahlhalla.worldbuilder.user.User;
import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;
import com.wahlhalla.worldbuilder.util.email.EmailService;
import com.wahlhalla.worldbuilder.util.exceptions.EntityNotFoundException;
import com.wahlhalla.worldbuilder.world.WorldRepository;

import jakarta.mail.MessagingException;

import com.wahlhalla.worldbuilder.security.payload.response.GenericResponse;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	WorldRepository worldRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
    MessageSource messages;

	@Autowired
	EmailService emailService;
	
	@Value("${app.audience}")
	private String[] audience;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername().toLowerCase(), loginRequest.getPassword()));
	
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
		if (userRepository.existsByUsernameIgnoreCase(signUpRequest.getUsername().toLowerCase())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Username is already taken!"));
		}

		if (userRepository.existsByEmailIgnoreCase(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername().toLowerCase(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ADMIN":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
					roles.add(adminRole);

					break;
				case "MODERATOR":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
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
		if (userRepository.existsByEmailIgnoreCase(emailChangeRequest.getNewEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Email is already in use!"));
		} 

		User user = this.userRepository.findById(userId).get();
		user.setEmail(emailChangeRequest.getNewEmail());
		this.userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Email changed successfully!"));
	}

	@PostMapping("/resetPassword/{email}")
	public GenericResponse resetPassword(@PathVariable("email") String email) throws MessagingException {
		try {
			User user = userRepository.findByEmailIgnoreCase(email).get();
			String token = UUID.randomUUID().toString();
			PasswordResetToken myToken = new PasswordResetToken(token, user);
			passwordResetTokenRepository.save(myToken);
			
			String url = audience[0] + "/resetPassword?token=" + token;
			String message = messages.getMessage("message.resetPassword", null, null);
			this.emailService.sendMail("Reset Password", message + " \r\n" + url, user.getEmail());
			
			return new GenericResponse(
				messages.getMessage("message.resetPasswordEmail", null, null));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("{\"error\":\""+messages.getMessage("user.notFound", null, null)+ "\"}");
		}
	}

	@PostMapping("/savePassword/{token}")
	public GenericResponse savePassword(@PathVariable("token") String token, @RequestBody PasswordChangeRequest passwordChangeRequest) {
		String result = this.validatePasswordResetToken(token);
		if(result != null) {
			return new GenericResponse(messages.getMessage(
				"auth.message." + result, null, null));
		}
		try {
			User user = Optional.ofNullable(this.passwordResetTokenRepository.findByToken(token).getUser()).get();
			user.setPassword(encoder.encode(passwordChangeRequest.getNewPassword()));
			this.userRepository.save(user);
			return new GenericResponse(messages.getMessage(
				"message.resetPasswordSuc", null, null));
		} catch (EntityNotFoundException e) {
			return new GenericResponse(messages.getMessage(
				"auth.message.invalid", null, null));
		}
	}

	private String validatePasswordResetToken(String token) {
		final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
		return !isTokenFound(passToken) ? "invalidToken"
				: isTokenExpired(passToken) ? "expired"
				: null;
	}
	
	private boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}
	
	private boolean isTokenExpired(PasswordResetToken passToken) {
		final Calendar cal = Calendar.getInstance();
		return passToken.getExpiryDate().before(cal.getTime());
	}
}
