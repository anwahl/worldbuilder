package com.wahlhalla.worldbuilder.region;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wahlhalla.worldbuilder.geography.Geography;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystem;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name="REGION", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "MUNICIPALITY", "WORLD_ID" }) })
public class Region {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION", length = 1024, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "MUNICIPALITY", nullable = false)
    private Municipality municipality;
    @ManyToOne
    @JsonIgnoreProperties(value={ "childRegions" }, allowSetters = true)
    @JoinColumn(name = "JURISDICTION_ID", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Region jurisdiction;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "jurisdiction")
    private Set<Region> childRegions = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties(value={ "regions" }, allowSetters = true)
    @JoinColumn(name = "GEOGRAPHY_ID", nullable = false)
    private Geography geography;
    @ManyToOne
    @JsonIgnoreProperties(value={ "regions" }, allowSetters = true)
    @JoinColumn(name = "POLITICAL_SYSTEM_ID", nullable = false)
    private PoliticalSystem politicalSystem;
    @ManyToOne
    @JsonIgnoreProperties(value={ "regions" }, allowSetters = true)
    @JoinColumn(name = "WORLD_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private World world;

    public Region() {

    }

    public Region(final String name, final String description, final Municipality municipality,
        final Region jurisdiction, final Geography geography, final PoliticalSystem politicalSystem,
         final World world) {
            this.name = name;
            this.description = description;
            this.municipality = municipality;
            this.jurisdiction = jurisdiction;
            this.geography = geography;
            this.politicalSystem = politicalSystem;
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

    public Municipality getMunicipality() {
        return this.municipality;
    }

    public void setMunicipality(final Municipality municipality) {
        this.municipality = municipality;
    }

    public Region getJurisdiction() {
        return this.jurisdiction;
    }

    public void setJurisdiction(final Region jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Geography getGeography() {
        return this.geography;
    }

    public void setGeography(Geography geography) {
        this.geography = geography;
    }
    

    public World getWorld() {
        return this.world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }


    public Set<Region> getChildRegions() {
        return this.childRegions;
    }

    public void setChildRegions(Set<Region> childRegions) {
        this.childRegions.addAll(childRegions);
    }

    public PoliticalSystem getPoliticalSystem() {
        return this.politicalSystem;
    }

    public void setPoliticalSystem(PoliticalSystem politicalSystem) {
        this.politicalSystem = politicalSystem;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", municipality='" + getMunicipality() + "'" +
            ", jurisdiction='" + getJurisdiction() + "'" +
            ", geography='" + getGeography() + "'" +
            ", politicalSystem='" + getPoliticalSystem() + "'" +
            ", world='" + getWorld() + "'" +
            "}";
    }


}
