package com.wahlhalla.worldbuilder;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.wahlhalla.worldbuilder.config.WithMockCustomUser;
import com.wahlhalla.worldbuilder.geography.Climate;
import com.wahlhalla.worldbuilder.geography.Geography;
import com.wahlhalla.worldbuilder.geography.GeographyController;
import com.wahlhalla.worldbuilder.geography.GeographyType;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystem;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystemController;
import com.wahlhalla.worldbuilder.politicalsystem.PoliticalSystemType;
import com.wahlhalla.worldbuilder.region.Municipality;
import com.wahlhalla.worldbuilder.region.Region;
import com.wahlhalla.worldbuilder.region.RegionController;
import com.wahlhalla.worldbuilder.region.RegionRepository;
import com.wahlhalla.worldbuilder.user.User;
import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldController;
import com.wahlhalla.worldbuilder.world.WorldRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RegionControllerTests {
    
    @Autowired
    UserDetailsService userDetailsService;
  
    @Autowired
    private MockMvc mvc;
  
    @Autowired
    GeographyController geographyController;
  
    @Autowired
    PoliticalSystemController politicalSystemController;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RegionController regionController;

    @Autowired
    WorldController worldController;
  
    @Autowired
    WorldRepository worldRepository;
  
    @Autowired
    RegionRepository regionRepository;
  
    @Autowired
    EntityManager entityManager;

    World world;

    Region region;
    
    Geography geography;

    PoliticalSystem politicalSystem;

    @BeforeAll
    void createUsers() {
        userRepository.save(new User("admin", "admin", "password"));
        userRepository.save(new User("user", "user@email.com", "password"));
    }
  
    @WithMockCustomUser
    @BeforeEach
    void createWorld() {
        this.world = new World("World Name", "World Description", false);
        worldController.create(world);
        this.geography = new Geography("Geography", "Description", GeographyType.CONTINENT, Climate.DESERT_CLIMATE, null, this.world);
        this.geographyController.create(geography);
        this.politicalSystem = new PoliticalSystem("Political System", "Description", PoliticalSystemType.DEMOCRACY, this.world);
        this.politicalSystemController.create(this.politicalSystem);
        this.region = new Region("Region1","Region Description",Municipality.COUNTRY,null, this.geography, this.politicalSystem, this.world);
        regionController.create(region);
    }

    @AfterEach
    void clearWorlds() {
        regionRepository.deleteAll();
        worldRepository.deleteAll();
    }
    
    @AfterAll
    void clearUsers() {
        this.userRepository.deleteAll();
    }
    
    @Test
    @WithMockCustomUser
    public void contextLoads() throws Exception {
        assertThat(regionController).isNotNull();
    }

    @Transactional
    @Test
    @WithMockCustomUser
    public void givenRegion_whenGetRegions_thenStatus200()
    throws Exception {
        Region region = new Region("Region Name", "Region Description", Municipality.CITY, this.region, this.geography, this.politicalSystem, this.world);
        regionController.create(region);

        mvc.perform(get("/api/region")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[1].name", is("Region Name")))
        .andExpect(jsonPath("$[1].description", is("Region Description")))
        .andExpect(jsonPath("$[1].municipality", is(Municipality.CITY.toString())))
        .andExpect(jsonPath("$[1].jurisdiction.id", is((int) this.region.getId())))
        .andExpect(jsonPath("$[1].world.id", is((int) this.world.getId())));
    }
}
