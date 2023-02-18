package com.wahlhalla.worldbuilder.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;

public class WithMockCustomUserSecurityContextFactory
	implements WithSecurityContextFactory<WithMockCustomUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities1 = new ArrayList<>();
        authorities1.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities1.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		UserDetailsImpl principal = new UserDetailsImpl(customUser.id(), customUser.username(), customUser.email(), customUser.password(), authorities1);
		Authentication auth =
			new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
		context.setAuthentication(auth);
		return context;
	}
}
