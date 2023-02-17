package com.wahlhalla.worldbuilder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

	long id() default 1L;
    
    String email() default "user@email.com";

    String password() default "password";

	String username() default "user";
}
