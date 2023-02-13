package com.wahlhalla.worldbuilder;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/test")
	public String testAccess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName() + " || " + auth.getAuthorities() + " || " + auth.getCredentials() + " || " + auth.getDetails() + " ||" + auth.getPrincipal();
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/userTest/{userId}")
	@PreAuthorize("principal.id.equals(#userId)")
	public String test1(@PathVariable Long userId) {
		return "Yep: " + userId;
	}
}
