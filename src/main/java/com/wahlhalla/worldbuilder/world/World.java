package com.wahlhalla.worldbuilder.world;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.actor.Actor;
import com.wahlhalla.worldbuilder.creature.Creature;
import com.wahlhalla.worldbuilder.geography.Geography;
import com.wahlhalla.worldbuilder.god.God;
import com.wahlhalla.worldbuilder.language.Language;
import com.wahlhalla.worldbuilder.magicsystem.MagicSystem;
import com.wahlhalla.worldbuilder.moon.Moon;
import com.wahlhalla.worldbuilder.plant.Plant;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystem;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.religion.Religion;
import com.wahlhalla.worldbuilder.resource.Resource;
import com.wahlhalla.worldbuilder.socialclass.SocialClass;
import com.wahlhalla.worldbuilder.sun.Sun;
import com.wahlhalla.worldbuilder.technology.Technology;
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
    @Column(name="DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @ManyToOne
    @JsonIgnoreProperties(value={"email", "password", "roles", "worlds"}, allowSetters = true)
    @JoinColumn(name="USER_ID", nullable=false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @Column(name="IS_PRIVATE", nullable = false, unique = false)
    private Boolean isPrivate;
    @JsonIgnoreProperties(value={"actors"}, allowSetters = true)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Race> races = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Region> regions = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Language> languages = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Religion> religions = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<God> gods = new HashSet<>();
    @JsonIgnoreProperties(value={"races","regions"}, allowSetters = true)
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<SocialClass> socialClasses = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Creature> creatures = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Plant> plants = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Sun> suns = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Moon> moons = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<PoliticalSystem> politicalSystems = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Geography> geographies = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Resource> resources = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<MagicSystem> magicSystems = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Technology> technologies = new HashSet<>();
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "world")
    private Set<Actor> actors = new HashSet<>();

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
		return this.races;
	}

	public void setRaces(Set<Race> races) {
		this.races.addAll(races);
	}

	public Set<Region> getRegions() {
		return this.regions;
	}

	public void setRegions(Set<Region> regions) {
		this.regions.addAll(regions);
	}
    
	public Set<Language> getLanguages() {
		return this.languages;
	}

	public void setLanguages(Set<Language> languages) {
		this.languages.addAll(languages);
	}
    
//TODO:  religions and gods and etc

    @Override
    public String toString() {
        return String.format(
            "World[id=%d, name='%s', description='%s', user='%s', private='%s']",
            id, this.name, this.description, (this.user != null ? this.user.getUsername() : "null"), this.getIsPrivate());
    }
    
}
