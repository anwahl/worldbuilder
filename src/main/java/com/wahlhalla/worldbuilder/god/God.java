package com.wahlhalla.worldbuilder.god;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.religion.Religion;
import com.wahlhalla.worldbuilder.world.World;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="GOD", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class God {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @ManyToOne
    @JsonIgnoreProperties(value={ "gods" }, allowSetters = true)
    @JoinColumn(name = "RELIGION_ID", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Religion religion;
    @ManyToOne
    @JsonIgnoreProperties(value={ "gods" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public God() {
    }

    public God(String name, String description, Religion religion, World world) {
        this.name = name;
        this.description = description;
        this.religion = religion;
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

    public Religion getReligion() {
        return this.religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
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
            ", religion='" + getReligion() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }

}
