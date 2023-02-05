package com.wahlhalla.worldbuilder.world;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="WORLD")
public class World {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name="NAME", length=64, nullable=false, unique=true)
    private String name;
    @Column(name="DESCRIPTION", length=1024, nullable=false, unique=true)
    private String description;
    @ManyToOne
    @JsonIgnoreProperties({"username", "email", "password", "roles"})
    @JoinColumn(name="USER_ID", nullable=false, updatable = false)
    private User user;
    @Column(name="IS_PRIVATE", nullable = false, unique = false)
    private Boolean isPrivate;

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

    @Override
    public String toString() {
        return String.format(
            "World[id=%d, name='%s', description='%s', user='%s', private='%s']",
            id, this.name, this.description, this.user.getUsername(), this.getIsPrivate());
    }
    
}
