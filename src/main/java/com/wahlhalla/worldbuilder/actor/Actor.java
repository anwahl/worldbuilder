package com.wahlhalla.worldbuilder.actor;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.language.Language;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.religion.Religion;
import com.wahlhalla.worldbuilder.socialclass.SocialClass;
import com.wahlhalla.worldbuilder.timeline.events.ActorRegionEvent;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name="ACTOR", uniqueConstraints = { @UniqueConstraint(columnNames = { "FIRST_NAME", "LAST_NAME", "WORLD_ID" }) })
public class Actor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "FIRST_NAME", length = 64, nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME", length = 64, nullable = false)
    private String lastName;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "actors","world" }, allowSetters = true)
    @JoinColumn(name = "RACE_ID", nullable = false)
    private Race race;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "actors","world" }, allowSetters = true)
    @JoinColumn(name = "SOCIAL_CLASS_ID", nullable = true)
    private SocialClass socialClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "actors","world" }, allowSetters = true)
    @JoinColumn(name = "RELIGION_ID", nullable = true)
    private Religion religion;
    @ManyToMany(mappedBy = "actors")
    private Set<ActorRegionEvent> actorRegionEvents = new HashSet<>();
    @JsonIgnoreProperties(value = { "actors" }, allowSetters = true)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ACTOR_LANGUAGES", 
        joinColumns = @JoinColumn(name = "ACTOR_ID"), 
        inverseJoinColumns = @JoinColumn(name = "LANGUAGE_ID"))
    private Set<Language> languages = new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "actors","languages" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Actor() {
    }

    public Actor(String firstName, String lastName, String description, Race race, SocialClass socialClass, Religion religion, Set<Language> languages, World world) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.race = race;
        this.socialClass = socialClass;
        this.religion = religion;
        this.languages = languages;
        this.world = world;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public SocialClass getSocialClass() {
        return this.socialClass;
    }

    public void setSocialClass(SocialClass socialClass) {
        this.socialClass = socialClass;
    }

    public Religion getReligion() {
        return this.religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Set<Language> getLanguages() {
        return this.languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Set<ActorRegionEvent> getActorRegionEvents() {
        return this.actorRegionEvents;
    }

    public void setActorRegionEvents(Set<ActorRegionEvent> actorRegionEvents) {
        this.actorRegionEvents.addAll(actorRegionEvents);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", description='" + getDescription() + "'" +
            ", race='" + getRace() + "'" +
            ", socialClass='" + getSocialClass() + "'" +
            ", religion='" + getReligion() + "'" +
            ", languages='" + getLanguages() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
    
}