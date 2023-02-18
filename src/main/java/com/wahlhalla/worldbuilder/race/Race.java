package com.wahlhalla.worldbuilder.race;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="RACE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "WORLD_ID" }) })
public class Race {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name="NAME", length=64, nullable=false)
    private String name;
    @Column(name="DESCRIPTION", length=1024, nullable=false)
    private String description;
    @Column(name="TRAIT", length=256, nullable=true)
    private String trait;
    @ManyToOne
    @JsonIgnoreProperties({"races"})
    @JoinColumn(name="WORLD_ID", nullable=false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Race() {

    }

    public Race(final String name, final String description, final String trait, final World world) {
        this.name = name;
        this.description = description;
        this.trait = trait;
        this.world = world;
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

    public String getTrait() {
        return this.trait;
    }

    public void setTrait(final String trait) {
        this.trait = trait;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    @Override
    public String toString() {
        return String.format(
            "Race[id=%d, name='%s', description='%s', trait='%s']",
            id, this.name, this.description, this.trait);
    }
}
