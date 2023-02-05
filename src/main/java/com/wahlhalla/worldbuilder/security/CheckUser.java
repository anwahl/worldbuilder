package com.wahlhalla.worldbuilder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.race.RaceRepository;
import com.wahlhalla.worldbuilder.user.impl.UserDetailsImpl;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldRepository;

@Component
public class CheckUser {
    
    @Autowired
    RaceRepository raceRepository;

    @Autowired
    WorldRepository worldRepository;

    public Boolean byWorldId(final Authentication authentication, final Long id) {
        World world = this.worldRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return world.getUser().getId() == userDetails.getId();
        } else {
            return false;
        }
    }

    public Boolean byRaceId(final Authentication authentication, final Long id) {
        Race race = this.raceRepository.findById(id).get();
        if (authentication.getPrincipal().getClass().equals(UserDetailsImpl.class)) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return race.getWorld().getUser().getId() == userDetails.getId();
        } else {
            return false;
        }
    }


}
