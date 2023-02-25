package com.wahlhalla.worldbuilder.socialclass;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.actor.Actor;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.world.World;

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
@Table(name="SOCIAL_CLASS", uniqueConstraints = { @UniqueConstraint(
        columnNames = { "NAME", "REGION_ID", "RACE_ID", "WORLD_ID" }) })
public class SocialClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "REGION_ID", nullable = true)
    private Region region;
    @ManyToOne
    @JoinColumn(name = "RACE_ID", nullable = true)
    private Race race;
    @OneToMany(mappedBy = "socialClass")
    private Set<Actor> actors = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties({ "regions" })
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public SocialClass() {
    }
 
    public SocialClass(String name, String description, Region region, Race race, World world) {
        this.name = name;
        this.description = description;
        this.region = region;
        this.race = race;
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

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(Race race) {
        this.race = race;
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
            ", region='" + getRegion() + "'" +
            ", race='" + getRace() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
}
