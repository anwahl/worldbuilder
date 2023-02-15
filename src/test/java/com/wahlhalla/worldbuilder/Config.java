package com.wahlhalla.worldbuilder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;

@EnableWebSecurity
@TestConfiguration
public class Config {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        List<GrantedAuthority> authorities1 = new ArrayList<>();
        authorities1.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities1.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        List<GrantedAuthority> authorities2 = new ArrayList<>();
        authorities2.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetailsImpl admin = new UserDetailsImpl(1L, "admin", "admin", "password", authorities1);
        UserDetailsImpl user = new UserDetailsImpl(2L, "user", "user", "password", authorities2);
        UserDetailsImpl test = new UserDetailsImpl(3L, "test", "test", "pass", authorities1);

        return new InMemoryUserDetailsManager(admin, user, test);
    }

}
