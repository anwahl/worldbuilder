package com.wahlhalla.worldbuilder.geography;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.resource.Resource;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="GEOGRAPHY", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class Geography {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private GeographyType type;
    @Column(name = "CLIMATE", nullable = false)
    private Climate climate;
    @ManyToMany
    @JoinTable(
        name = "GEOGRAPHY_RESOURCE", 
        joinColumns = @JoinColumn(name = "GEOGRAPHY_ID"), 
        inverseJoinColumns = @JoinColumn(name = "RESOURCE_ID"))
    @JsonIgnoreProperties(value={ "geographies" }, allowSetters = true)
    private Set<Resource> resources = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={ "childGeographies" }, allowSetters = true)
    @JoinColumn(name = "PARENT_GEOGRAPHY_ID", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Geography parentGeography;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.MERGE, mappedBy = "parentGeography")
    private Set<Geography> childGeographies = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={"races","actors","languages","geographies","regions","politicalSystems","resources"}, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Geography() {
    }

    public Geography(String name, String description, GeographyType type,
    Climate climate, Geography parentGeography, World world) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.climate = climate;
        this.parentGeography = parentGeography;
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

    public GeographyType getType() {
        return this.type;
    }

    public void setType(GeographyType type) {
        this.type = type;
    }

    public Climate getClimate() {
        return this.climate;
    }

    public void setClimate(Climate climate) {
        this.climate = climate;
    }

    public Geography getParentGeography() {
        return this.parentGeography;
    }

    public void setParentGeography(Geography parentGeography) {
        this.parentGeography = parentGeography;
    }


    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Set<Geography> getChildGeographies() {
        return this.childGeographies;
    }

    public void setChildGeographies(Set<Geography> childGeographies) {
        this.childGeographies.addAll(childGeographies);
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
            ", type='" + getType() + "'" +
            ", climate='" + getClimate() + "'" +
            ", parentGeography='" + getParentGeography() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
    
}
