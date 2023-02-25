package com.wahlhalla.worldbuilder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wahlhalla.worldbuilder.actor.Actor;
import com.wahlhalla.worldbuilder.actor.ActorRepository;
import com.wahlhalla.worldbuilder.creature.Creature;
import com.wahlhalla.worldbuilder.creature.CreatureRepository;
import com.wahlhalla.worldbuilder.geography.Geography;
import com.wahlhalla.worldbuilder.geography.GeographyRepository;
import com.wahlhalla.worldbuilder.god.God;
import com.wahlhalla.worldbuilder.god.GodRepository;
import com.wahlhalla.worldbuilder.language.Language;
import com.wahlhalla.worldbuilder.language.LanguageRepository;
import com.wahlhalla.worldbuilder.magicsystem.MagicSystem;
import com.wahlhalla.worldbuilder.magicsystem.MagicSystemRepository;
import com.wahlhalla.worldbuilder.moon.Moon;
import com.wahlhalla.worldbuilder.moon.MoonRepository;
import com.wahlhalla.worldbuilder.plant.Plant;
import com.wahlhalla.worldbuilder.plant.PlantRepository;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystem;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystemRepository;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.race.RaceRepository;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.region.RegionRepository;
import com.wahlhalla.worldbuilder.religion.Religion;
import com.wahlhalla.worldbuilder.religion.ReligionRepository;
import com.wahlhalla.worldbuilder.resource.Resource;
import com.wahlhalla.worldbuilder.resource.ResourceRepository;
import com.wahlhalla.worldbuilder.socialclass.SocialClass;
import com.wahlhalla.worldbuilder.socialclass.SocialClassRepository;
import com.wahlhalla.worldbuilder.sun.Sun;
import com.wahlhalla.worldbuilder.sun.SunRepository;
import com.wahlhalla.worldbuilder.technology.Technology;
import com.wahlhalla.worldbuilder.technology.TechnologyRepository;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldRepository;

@Component
public class CheckPrivacy {

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    TechnologyRepository technologyRepository;

    @Autowired
    MagicSystemRepository magicSystemRepository;

    @Autowired
    PoliticalSystemRepository politicalSystemRepository;
    
    @Autowired
    GeographyRepository geographyRepository;

    @Autowired
    ResourceRepository resourceRepository;
    
    @Autowired
    SunRepository sunRepository;

    @Autowired
    MoonRepository moonRepository;

    @Autowired
    CreatureRepository creatureRepository;

    @Autowired
    GodRepository godRepository;

    @Autowired
    PlantRepository plantRepository;

    @Autowired
    ReligionRepository religionRepository;

    @Autowired
    SocialClassRepository socialClassRepository;

    @Autowired
    LanguageRepository languageRepository;
   
    @Autowired
    RegionRepository regionRepository;

    @Autowired
    RaceRepository raceRepository;

    @Autowired
    WorldRepository worldRepository;

    public Boolean byWorldId(final Long id) {
        World world = this.worldRepository.findById(id).get();
        return !world.getIsPrivate();
    }

    public Boolean byRaceId(final Long id) {
        Race race = this.raceRepository.findById(id).get();
        return !race.getWorld().getIsPrivate();
    }

    public Boolean byRegionId(final Long id) {
        Region region = this.regionRepository.findById(id).get();
        return !region.getWorld().getIsPrivate();
    }
    
    public Boolean byLanguageId(final Long id) {
        Language language = this.languageRepository.findById(id).get();
        return !language.getWorld().getIsPrivate();
    }

    public Boolean byCreatureId(final Long id) {
        Creature creature = this.creatureRepository.findById(id).get();
        return !creature.getWorld().getIsPrivate();
    }

    public Boolean byPlantId(final Long id) {
        Plant plant = this.plantRepository.findById(id).get();
        return !plant.getWorld().getIsPrivate();
    }
    
    public Boolean byGodId(final Long id) {
        God god = this.godRepository.findById(id).get();
        return !god.getWorld().getIsPrivate();
    }
    
    public Boolean byReligionId(final Long id) {
        Religion religion = this.religionRepository.findById(id).get();
        return !religion.getWorld().getIsPrivate();
    }

    public Boolean bySocialClassId(final Long id) {
        SocialClass socialClass = this.socialClassRepository.findById(id).get();
        return !socialClass.getWorld().getIsPrivate();
    }

    public Boolean byMoonId(final Long id) {
        Moon moon = this.moonRepository.findById(id).get();
        return !moon.getWorld().getIsPrivate();
    }

    public Boolean bySunId(final Long id) {
        Sun sun = this.sunRepository.findById(id).get();
        return !sun.getWorld().getIsPrivate();
    }

    public Boolean byResourceId(final Long id) {
        Resource resource = this.resourceRepository.findById(id).get();
        return !resource.getWorld().getIsPrivate();
    }

    public Boolean byGeographyId(final Long id) {
        Geography geography = this.geographyRepository.findById(id).get();
        return !geography.getWorld().getIsPrivate();
    }

    public Boolean byPoliticalSystemId(final Long id) {
        PoliticalSystem politicalSystem = this.politicalSystemRepository.findById(id).get();
        return !politicalSystem.getWorld().getIsPrivate();
    }

    public Boolean byMagicSystemId(final Long id) {
        MagicSystem magicSystem = this.magicSystemRepository.findById(id).get();
        return !magicSystem.getWorld().getIsPrivate();
    }

    public Boolean byTechnologyId(final Long id) {
        Technology technology = this.technologyRepository.findById(id).get();
        return !technology.getWorld().getIsPrivate();
    }

    public Boolean byActorId(final Long id) {
        Actor actor = this.actorRepository.findById(id).get();
        return !actor.getWorld().getIsPrivate();
    }
}
