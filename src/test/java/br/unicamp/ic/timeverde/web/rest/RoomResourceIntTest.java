package br.unicamp.ic.timeverde.web.rest;

import br.unicamp.ic.timeverde.DinoApp;
import br.unicamp.ic.timeverde.domain.Room;
import br.unicamp.ic.timeverde.repository.RoomRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.unicamp.ic.timeverde.domain.enumeration.RoomTypeEnum;

/**
 * Test class for the RoomResource REST controller.
 *
 * @see RoomResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DinoApp.class)
@WebAppConfiguration
@IntegrationTest
public class RoomResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final RoomTypeEnum DEFAULT_TYPE = RoomTypeEnum.BEDROOM;
    private static final RoomTypeEnum UPDATED_TYPE = RoomTypeEnum.GARDEN;

    @Inject
    private RoomRepository roomRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRoomMockMvc;

    private Room room;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoomResource roomResource = new RoomResource();
        ReflectionTestUtils.setField(roomResource, "roomRepository", roomRepository);
        this.restRoomMockMvc = MockMvcBuilders.standaloneSetup(roomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        room = new Room();
        room.setName(DEFAULT_NAME);
        room.setDescription(DEFAULT_DESCRIPTION);
        room.setType(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // Create the Room

        restRoomMockMvc.perform(post("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(room)))
                .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = rooms.get(rooms.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoom.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the rooms
        restRoomMockMvc.perform(get("/api/rooms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = new Room();
        updatedRoom.setId(room.getId());
        updatedRoom.setName(UPDATED_NAME);
        updatedRoom.setDescription(UPDATED_DESCRIPTION);
        updatedRoom.setType(UPDATED_TYPE);

        restRoomMockMvc.perform(put("/api/rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRoom)))
                .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = rooms.get(rooms.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoom.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoom.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);
        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Get the room
        restRoomMockMvc.perform(delete("/api/rooms/{id}", room.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Room> rooms = roomRepository.findAll();
        assertThat(rooms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
