package com.wahlhalla.worldbuilder.world;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cascade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="WORLD", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "USER_ID" }) })
public class World {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name="NAME", length=64, nullable=false)
    private String name;
    @Column(name="DESCRIPTION")
    private String description;
    @ManyToOne
    @JsonIgnoreProperties({"email", "password", "roles", "worlds"})
    @JoinColumn(name="USER_ID", nullable=false, updatable = false)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    private User user;
    @Column(name="IS_PRIVATE", nullable = false, unique = false)
    private Boolean isPrivate;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Race> races = new HashSet<>();

    public World () {

    }
    
    public World(final String name, final String description, final Boolean isPrivate) {
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
    }

    public World(final String name, final String description, final User user, final Boolean isPrivate) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.isPrivate = isPrivate;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
    
    public Boolean getIsPrivate() {
        return this.isPrivate;
    }

    public void setIsPrivate(final Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

	public Set<Race> getRaces() {
		return races;
	}

	public void setRaces(Set<Race> races) {
		this.races.addAll(races);
	}

    @Override
    public String toString() {
        return String.format(
            "World[id=%d, name='%s', description='%s', user='%s', private='%s']",
            id, this.name, this.description, (this.user != null ? this.user.getUsername() : "null"), this.getIsPrivate());
    }
    
}
