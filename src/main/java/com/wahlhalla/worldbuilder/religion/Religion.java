package com.wahlhalla.worldbuilder.religion;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.actor.Actor;
import com.wahlhalla.worldbuilder.god.God;
import com.wahlhalla.worldbuilder.world.World;

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
@Table(name="RELIGION", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class Religion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "religion")
    private Set<God> gods = new HashSet<>();
    @OneToMany(mappedBy = "religion")
    private Set<Actor> actors = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={ "religions" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Religion() {
    }

    public Religion(String name, String description, World world) {
        this.name = name;
        this.description = description;
        this.world = world;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<God> getGods() {
        return this.gods;
    }

    public void setGods(Set<God> gods) {
        this.gods = gods;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Set<Actor> getActors() {
        return this.actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors.addAll(actors);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", gods='" + getGods() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
}
