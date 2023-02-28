package com.wahlhalla.worldbuilder.resource;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.geography.Geography;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="RESOURCE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @ManyToMany
    @JoinTable(
        name = "GEOGRAPHY_RESOURCE", 
        joinColumns = @JoinColumn(name = "RESOURCE_ID"), 
        inverseJoinColumns = @JoinColumn(name = "GEOGRAPHY_ID"))
    private Set<Geography> geographies = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={ "resources" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Resource() {
    }

    public Resource(String name, String description, World world) {
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

    public Set<Geography> getGeographies() {
        return this.geographies;
    }

    public void setGeographies(Set<Geography> geographies) {
        this.geographies.addAll(geographies);
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
    
}
