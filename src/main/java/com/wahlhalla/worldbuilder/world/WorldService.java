package com.wahlhalla.worldbuilder.world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wahlhalla.worldbuilder.user.UserRepository;

@Service
public class WorldService {
    
    private final WorldRepository worldRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorldService(final WorldRepository worldRepository,
                        final UserRepository userRepository) {
        this.worldRepository = worldRepository;
        this.userRepository = userRepository;
    }

    public World save(World world) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        world.setUser(userRepository.findByUsernameIgnoreCase(auth.getName()).get());
        return worldRepository.save(world);
    }
}
