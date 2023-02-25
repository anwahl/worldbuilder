package com.wahlhalla.worldbuilder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import com.wahlhalla.worldbuilder.user.User;
import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldRepository;

@Component
public class CheckUser {
    
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

    @Autowired
    UserRepository userRepository;

    public Boolean byUserId(final Authentication authentication, final Long id) {
        User user = this.userRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return user.getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byWorldId(final Authentication authentication, final Long id) {
        World world = this.worldRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return world.getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byRaceId(final Authentication authentication, final Long id) {
        Race race = this.raceRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return race.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byRegionId(final Authentication authentication, final Long id) {
        Region region = this.regionRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return region.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byLanguageId(final Authentication authentication, final Long id) {
        Language language = this.languageRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return language.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byCreatureId(final Authentication authentication, final Long id) {
        Creature creature = this.creatureRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return creature.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }
    
    public Boolean byPlantId(final Authentication authentication, final Long id) {
        Plant plant = this.plantRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return plant.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    
    public Boolean byGodId(final Authentication authentication, final Long id) {
        God god = this.godRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return god.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    
    public Boolean byReligionId(final Authentication authentication, final Long id) {
        Religion religion = this.religionRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return religion.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    
    public Boolean bySocialClassId(final Authentication authentication, final Long id) {
        SocialClass socialClass = this.socialClassRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return socialClass.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }
    
    public Boolean byMoonId(final Authentication authentication, final Long id) {
        Moon moon = this.moonRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return moon.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean bySunId(final Authentication authentication, final Long id) {
        Sun sun = this.sunRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return sun.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }
    
    public Boolean byResourceId(final Authentication authentication, final Long id) {
        Resource resource = this.resourceRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return resource.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byGeographyId(final Authentication authentication, final Long id) {
        Geography geography = this.geographyRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return geography.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }
    
    public Boolean byPoliticalSystemId(final Authentication authentication, final Long id) {
        PoliticalSystem politicalSystem = this.politicalSystemRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return politicalSystem.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byMagicSystemId(final Authentication authentication, final Long id) {
        MagicSystem magicSystem = this.magicSystemRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return magicSystem.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byTechnologyId(final Authentication authentication, final Long id) {
        Technology technology = this.technologyRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return technology.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }

    public Boolean byActorId(final Authentication authentication, final Long id) {
        Actor actor = this.actorRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return actor.getWorld().getUser().getId().equals(userDetails.getId());
        } else {
            return false;
        }
    }
}
