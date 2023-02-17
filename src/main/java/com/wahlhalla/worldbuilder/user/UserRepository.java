package com.wahlhalla.worldbuilder.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameIgnoreCase(String username);
	Optional<User> findByEmailIgnoreCase(String email);
	Boolean existsByUsernameIgnoreCase(String username);
	Boolean existsByEmailIgnoreCase(String email);

}
