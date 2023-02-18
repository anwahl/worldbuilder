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
import org.springframework.transaction.annotation.Transactional;

import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.util.Util;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldController;
import com.wahlhalla.worldbuilder.world.WorldRepository;

import jakarta.persistence.EntityManager;

import com.wahlhalla.worldbuilder.config.WithMockCustomUser;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.race.RaceController;
import com.wahlhalla.worldbuilder.race.RaceRepository;
import com.wahlhalla.worldbuilder.user.User;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RaceControllerTests {
    
    @Autowired
    UserDetailsService userDetailsService;
  
    @Autowired
    private MockMvc mvc;
  
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RaceController raceController;

    @Autowired
    WorldController worldController;
  
    @Autowired
    WorldRepository worldRepository;
  
    @Autowired
    RaceRepository raceRepository;
  
    @Autowired
    EntityManager entityManager;

    World world;
  
    @BeforeAll
    void createUsers() {
        userRepository.deleteAll();
        userRepository.save(new User("admin", "admin", "password"));
        userRepository.save(new User("user", "user@email.com", "password"));
    }
  
    @WithMockCustomUser
    @BeforeEach
    void createWorld() {
        this.world = new World("World Name", "World Description", false);
        worldController.create(world);

    }

    @AfterEach
    void clearWorlds() {
        raceRepository.deleteAll();
        worldRepository.deleteAll();
    }
  
    @Test
    @WithMockCustomUser
      public void contextLoads() throws Exception {
          assertThat(raceController).isNotNull();
      }
  
    @Transactional
    @Test
    @WithMockCustomUser
    public void givenRace_whenGetRaces_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);

        mvc.perform(get("/api/race")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content()
          .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].name", is("Race Name")))
          .andExpect(jsonPath("$[0].description", is("Race Description")))
          .andExpect(jsonPath("$[0].trait", is("Race Trait")))
          .andExpect(jsonPath("$[0].world.id", is((int) this.world.getId())));
    }
  
    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenGetRace_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);
  
        mvc.perform(get("/api/race/" + race.getId())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content()
          .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("name", is("Race Name")))
          .andExpect(jsonPath("description", is("Race Description")))
          .andExpect(jsonPath("trait", is("Race Trait")))
          .andExpect(jsonPath("world.id", is((int) this.world.getId())));
    }
  
    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenGetRacesByWorld_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);
  
        mvc.perform(get("/api/race/world/"+this.world.getId())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content()
          .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].name", is("Race Name")))
          .andExpect(jsonPath("$[0].description", is("Race Description")))
          .andExpect(jsonPath("$[0].trait", is("Race Trait")))
          .andExpect(jsonPath("$[0].world.id", is((int) this.world.getId())));
    }

    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenGetRacesByName_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);
  
        mvc.perform(get("/api/race/name/name")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content()
          .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$[0].name", is("Race Name")))
          .andExpect(jsonPath("$[0].description", is("Race Description")))
          .andExpect(jsonPath("$[0].trait", is("Race Trait")))
          .andExpect(jsonPath("$[0].world.id", is((int) this.world.getId())));
    }
  
    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenPostRace_thenStatus201()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
  
        mvc.perform(post("/api/race")
          .content(Util.asJsonString(race))
          .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("name", is("Race Name")))
          .andExpect(jsonPath("description", is("Race Description")))
          .andExpect(jsonPath("trait", is("Race Trait")))
          .andExpect(jsonPath("world.id", is((int) this.world.getId())));
          assertThat(raceRepository.count()).isEqualTo(1);
    }
  
    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenDeleteRace_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);
  
        mvc.perform(delete("/api/race/" + race.getId())
          .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
        assertThat(raceRepository.count()).isEqualTo(0);
    }
  
  
    @Transactional
    @WithMockCustomUser
    @Test
    public void givenRace_whenPutRace_thenStatus200()
      throws Exception {
        Race race = new Race("Race Name", "Race Description", "Race Trait", this.world);
        raceController.create(race);
  
        race.setName("New Name");
        race.setDescription("New Description");
        race.setTrait("New Trait");
  
        mvc.perform(put("/api/race/" + race.getId())
          .content(Util.asJsonString(race))
          .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("name", is("New Name")))
          .andExpect(jsonPath("description", is("New Description")))
          .andExpect(jsonPath("trait", is("New Trait")))
          .andExpect(jsonPath("world.id", is((int) this.world.getId())));
    }
}
