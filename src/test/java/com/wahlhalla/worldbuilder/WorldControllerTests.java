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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.util.Util;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldController;
import com.wahlhalla.worldbuilder.world.WorldRepository;

import jakarta.persistence.EntityManager;

import com.wahlhalla.worldbuilder.config.Config;
import com.wahlhalla.worldbuilder.race.Race;
import com.wahlhalla.worldbuilder.race.RaceRepository;
import com.wahlhalla.worldbuilder.user.User;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Import(Config.class)
class WorldControllerTests {
  
  @Autowired
  UserDetailsService userDetailsService;

	@Autowired
  private MockMvc mvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WorldController worldController;

  @Autowired
  WorldRepository worldRepository;

  @Autowired
  RaceRepository raceRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeAll
  void createUsers() {
    userRepository.save(new User("admin", "admin", "password"));
    userRepository.save(new User("user", "user", "password"));
    userRepository.save(new User("test", "test", "pass"));
  }

  @BeforeEach
  void authenticate() {
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(authentication.getPrincipal()).thenReturn(userDetailsService.loadUserByUsername("admin"));
  }

  @AfterEach
  void clearWorlds() {
    worldRepository.deleteAll();
  }

  @Test
	public void contextLoads() throws Exception {
		assertThat(worldController).isNotNull();
	}

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenGetWorldsAsAdmin_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);

      worldController.create(world);

      mvc.perform(get("/api/world")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("World Name")))
        .andExpect(jsonPath("$[0].description", is("World Description")))
        .andExpect(jsonPath("$[0].isPrivate", is(false)))
        .andExpect(jsonPath("$[0].user.id", is(1)));
  }

  @Transactional
  @WithUserDetails(value = "user")
  @Test
  public void givenWorld_whenGetWorldsAsUser_thenStatus403()
    throws Exception {
      World world = new World("World Name", "World Description", false);
    
      worldController.create(world);

      mvc.perform(get("/api/world")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(403));
  }

  @Transactional
  @WithUserDetails(value = "user")
  @Test
  public void givenWorld_whenGetWorld_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);

      worldController.create(world);

      mvc.perform(get("/api/world/" + world.getId())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("name", is("World Name")))
        .andExpect(jsonPath("description", is("World Description")))
        .andExpect(jsonPath("isPrivate", is(false)))
        .andExpect(jsonPath("user.id", is(2)));
  }

  @Transactional
  @WithUserDetails(value = "user")
  @Test
  public void givenPrivateWorld_whenGetPublicWorlds_thenEmpty()
    throws Exception {
      World world = new World("World Name", "World Description", true);
    
      worldController.create(world);

      mvc.perform(get("/api/world/public")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").doesNotExist());
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenGetWorldByUser_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);

      worldController.create(world);

      mvc.perform(get("/api/world/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("World Name")))
        .andExpect(jsonPath("$[0].description", is("World Description")))
        .andExpect(jsonPath("$[0].isPrivate", is(false)))
        .andExpect(jsonPath("$[0].user.id", is(1)));
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenGetWorldByName_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);

      worldController.create(world);

      mvc.perform(get("/api/world/name/name")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].name", is("World Name")))
        .andExpect(jsonPath("$[0].description", is("World Description")))
        .andExpect(jsonPath("$[0].isPrivate", is(false)))
        .andExpect(jsonPath("$[0].user.id", is(1)));
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenPostWorld_thenStatus201()
    throws Exception {
      World world = new World("World Name", "World Description", false);

      mvc.perform(post("/api/world")
        .content(Util.asJsonString(world))
        .contentType(MediaType.APPLICATION_JSON)
	      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("name", is("World Name")))
        .andExpect(jsonPath("description", is("World Description")))
        .andExpect(jsonPath("isPrivate", is(false)))
        .andExpect(jsonPath("user.id", is(1)));
        assertThat(worldRepository.count()).isEqualTo(1);
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenDeleteWorld_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);
      worldController.create(world);

      mvc.perform(delete("/api/world/" + world.getId())
        .contentType(MediaType.APPLICATION_JSON)
	      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
      assertThat(worldRepository.count()).isEqualTo(0);
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void whenDeletingWorld_thenChildrenShouldBeDeleted() throws Exception {
      World world = new World("World Name", "World Description", false);
      worldController.create(world);
      Race race = new Race("raceName", "raceDescription", "raceTrait", world);
      raceRepository.save(race);
      assertThat(raceRepository.count()).isEqualTo(1);
      assertThat(worldRepository.count()).isEqualTo(1);
      mvc.perform(delete("/api/world/" + world.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
      assertThat(worldRepository.count()).isEqualTo(0);
      assertThat(raceRepository.count()).isEqualTo(0);
  }

  @Transactional
  @WithUserDetails(value = "admin")
  @Test
  public void givenWorld_whenPutWorld_thenStatus200()
    throws Exception {
      World world = new World("World Name", "World Description", false);
      
      worldController.create(world);

      world.setName("New Name");
      world.setDescription("New Description");
      world.setIsPrivate(true);

      mvc.perform(put("/api/world/" + world.getId())
        .content(Util.asJsonString(world))
        .contentType(MediaType.APPLICATION_JSON)
	      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name", is("New Name")))
        .andExpect(jsonPath("description", is("New Description")))
        .andExpect(jsonPath("isPrivate", is(true)))
        .andExpect(jsonPath("user.id", is(1)));
  }
}
