package com.wahlhalla.worldbuilder.politicalsystem;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="POLITICAL_SYSTEM", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class PoliticalSystem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = true)
    private PoliticalSystemType type;
    @OneToMany(mappedBy = "politicalSystem")
    @JsonIgnoreProperties(value={ "politicalSystems" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={"races","actors","languages","geographies","regions","politicalSystems","resources"}, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public PoliticalSystem() {
    }

    public PoliticalSystem(String name, String description, PoliticalSystemType type, World world) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public PoliticalSystemType getType() {
        return this.type;
    }

    public void setType(PoliticalSystemType type) {
        this.type = type;
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
            ", world='" + getWorld() + "'" +
            "}";
    }
    
}
