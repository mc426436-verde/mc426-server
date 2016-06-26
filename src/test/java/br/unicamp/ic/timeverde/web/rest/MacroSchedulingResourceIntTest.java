package br.unicamp.ic.timeverde.web.rest;

import br.unicamp.ic.timeverde.DinoApp;
import br.unicamp.ic.timeverde.domain.MacroScheduling;
import br.unicamp.ic.timeverde.repository.MacroSchedulingRepository;

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

/**
 * Test class for the MacroSchedulingResource REST controller.
 *
 * @see MacroSchedulingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DinoApp.class)
@WebAppConfiguration
@IntegrationTest
public class MacroSchedulingResourceIntTest {


    private static final String DEFAULT_TIME = "AAAAA";
    private static final String UPDATED_TIME = "BBBBB";

    @Inject
    private MacroSchedulingRepository macroSchedulingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMacroSchedulingMockMvc;

    private MacroScheduling macroScheduling;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MacroSchedulingResource macroSchedulingResource = new MacroSchedulingResource();
        ReflectionTestUtils.setField(macroSchedulingResource, "macroSchedulingRepository", macroSchedulingRepository);
        this.restMacroSchedulingMockMvc = MockMvcBuilders.standaloneSetup(macroSchedulingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        macroScheduling = new MacroScheduling();
        macroScheduling.setTime(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createMacroScheduling() throws Exception {
        int databaseSizeBeforeCreate = macroSchedulingRepository.findAll().size();

        // Create the MacroScheduling

        restMacroSchedulingMockMvc.perform(post("/api/macro-schedulings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macroScheduling)))
                .andExpect(status().isCreated());

        // Validate the MacroScheduling in the database
        List<MacroScheduling> macroSchedulings = macroSchedulingRepository.findAll();
        assertThat(macroSchedulings).hasSize(databaseSizeBeforeCreate + 1);
        MacroScheduling testMacroScheduling = macroSchedulings.get(macroSchedulings.size() - 1);
        assertThat(testMacroScheduling.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void getAllMacroSchedulings() throws Exception {
        // Initialize the database
        macroSchedulingRepository.saveAndFlush(macroScheduling);

        // Get all the macroSchedulings
        restMacroSchedulingMockMvc.perform(get("/api/macro-schedulings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(macroScheduling.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())));
    }

    @Test
    @Transactional
    public void getMacroScheduling() throws Exception {
        // Initialize the database
        macroSchedulingRepository.saveAndFlush(macroScheduling);

        // Get the macroScheduling
        restMacroSchedulingMockMvc.perform(get("/api/macro-schedulings/{id}", macroScheduling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(macroScheduling.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMacroScheduling() throws Exception {
        // Get the macroScheduling
        restMacroSchedulingMockMvc.perform(get("/api/macro-schedulings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacroScheduling() throws Exception {
        // Initialize the database
        macroSchedulingRepository.saveAndFlush(macroScheduling);
        int databaseSizeBeforeUpdate = macroSchedulingRepository.findAll().size();

        // Update the macroScheduling
        MacroScheduling updatedMacroScheduling = new MacroScheduling();
        updatedMacroScheduling.setId(macroScheduling.getId());
        updatedMacroScheduling.setTime(UPDATED_TIME);

        restMacroSchedulingMockMvc.perform(put("/api/macro-schedulings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMacroScheduling)))
                .andExpect(status().isOk());

        // Validate the MacroScheduling in the database
        List<MacroScheduling> macroSchedulings = macroSchedulingRepository.findAll();
        assertThat(macroSchedulings).hasSize(databaseSizeBeforeUpdate);
        MacroScheduling testMacroScheduling = macroSchedulings.get(macroSchedulings.size() - 1);
        assertThat(testMacroScheduling.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void deleteMacroScheduling() throws Exception {
        // Initialize the database
        macroSchedulingRepository.saveAndFlush(macroScheduling);
        int databaseSizeBeforeDelete = macroSchedulingRepository.findAll().size();

        // Get the macroScheduling
        restMacroSchedulingMockMvc.perform(delete("/api/macro-schedulings/{id}", macroScheduling.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MacroScheduling> macroSchedulings = macroSchedulingRepository.findAll();
        assertThat(macroSchedulings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
