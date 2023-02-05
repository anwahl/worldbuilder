package com.wahlhalla.worldbuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.wahlhalla.worldbuilder.user.UserRepository;
import com.wahlhalla.worldbuilder.world.World;
import com.wahlhalla.worldbuilder.world.WorldController;
import com.wahlhalla.worldbuilder.world.WorldRepository;
import com.wahlhalla.worldbuilder.user.User;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
class WorldControllerTests {

	@Autowired
  private MockMvc mvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WorldController worldController;

  @Autowired
  WorldRepository worldRepository;

  @BeforeAll
  void createUsers() {
    userRepository.save(new User("admin", "admin", "password"));
    userRepository.save(new User("user", "user", "password"));
  }

  @AfterEach
  void clearWorlds() {
    worldRepository.deleteAll();
  }

  @Test
	public void contextLoads() throws Exception {
		assertThat(worldController).isNotNull();
	}

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
}
