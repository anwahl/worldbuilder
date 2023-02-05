package com.wahlhalla.worldbuilder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.race.RaceRepository;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldRepository;

@Component
public class CheckPrivacy {
    
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
}
