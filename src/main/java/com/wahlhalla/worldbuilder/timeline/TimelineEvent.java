package com.wahlhalla.worldbuilder.timeline;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public abstract class TimelineEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @Column(name = "DATE", nullable = false)
    private double date;
    @ManyToOne
    @JsonIgnoreProperties(value={ "timelineEvents" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public TimelineEvent() {
    }

    public TimelineEvent(String description, double date, World world) {
        this.description = description;
        this.date = date;
        this.world = world;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDate() {
        return this.date;
    }

    public void setDate(double date) {
        this.date = date;
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
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }
    
}
