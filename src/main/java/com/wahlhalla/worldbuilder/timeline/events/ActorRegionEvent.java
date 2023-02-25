package com.wahlhalla.worldbuilder.timeline.events;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.actor.Actor;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.timeline.TimelineEvent;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ACTOR_REGION_EVENT")
public class ActorRegionEvent extends TimelineEvent {

    @ManyToMany
    @JoinTable(
        name = "ACTOR_REGIONS", 
        joinColumns = @JoinColumn(name = "ACTOR_ID"), 
        inverseJoinColumns = @JoinColumn(name = "ACTOR_REGION_EVENT_ID"))
    private Set<Actor> actors = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties({ "actorRegionEvents" })
    @JoinColumn(name = "REGION_ID", nullable = false)
    private Region region;


    public Set<Actor> getActors() {
        return this.actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors.addAll(actors);
    }

    public Region getRegion() {
        return this.region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    

    @Override
    public String toString() {
        return "{" +
            " actors='" + getActors() + "'" +
            ", region='" + getRegion() + "'" +
            "}";
    }

}
