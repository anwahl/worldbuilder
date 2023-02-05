package com.wahlhalla.worldbuilder.user;

import java.util.HashSet;
import java.util.Set;

import com.wahlhalla.worldbuilder.role.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERS" )
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Column(name="USERNAME", length=64, nullable=false, unique=true)
	private String username;
    
    @Column(name="EMAIL", length=64, nullable=false, unique=true)
	private String email;
    
    @Column(name="PASSWORD", length=64, nullable=false, unique=false)
	private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="USER_ROLES",
               joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName="ID"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
}