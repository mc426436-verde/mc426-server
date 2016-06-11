package br.unicamp.ic.timerverde.web.rest;

import br.unicamp.ic.timerverde.DinoApp;
import br.unicamp.ic.timerverde.domain.Macro;
import br.unicamp.ic.timerverde.repository.MacroRepository;

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
 * Test class for the MacroResource REST controller.
 *
 * @see MacroResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DinoApp.class)
@WebAppConfiguration
@IntegrationTest
public class MacroResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private MacroRepository macroRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMacroMockMvc;

    private Macro macro;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MacroResource macroResource = new MacroResource();
        ReflectionTestUtils.setField(macroResource, "macroRepository", macroRepository);
        this.restMacroMockMvc = MockMvcBuilders.standaloneSetup(macroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        macro = new Macro();
        macro.setName(DEFAULT_NAME);
        macro.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMacro() throws Exception {
        int databaseSizeBeforeCreate = macroRepository.findAll().size();

        // Create the Macro

        restMacroMockMvc.perform(post("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macro)))
                .andExpect(status().isCreated());

        // Validate the Macro in the database
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeCreate + 1);
        Macro testMacro = macros.get(macros.size() - 1);
        assertThat(testMacro.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacro.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMacros() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);

        // Get all the macros
        restMacroMockMvc.perform(get("/api/macros?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(macro.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);

        // Get the macro
        restMacroMockMvc.perform(get("/api/macros/{id}", macro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(macro.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMacro() throws Exception {
        // Get the macro
        restMacroMockMvc.perform(get("/api/macros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);
        int databaseSizeBeforeUpdate = macroRepository.findAll().size();

        // Update the macro
        Macro updatedMacro = new Macro();
        updatedMacro.setId(macro.getId());
        updatedMacro.setName(UPDATED_NAME);
        updatedMacro.setDescription(UPDATED_DESCRIPTION);

        restMacroMockMvc.perform(put("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMacro)))
                .andExpect(status().isOk());

        // Validate the Macro in the database
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeUpdate);
        Macro testMacro = macros.get(macros.size() - 1);
        assertThat(testMacro.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacro.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);
        int databaseSizeBeforeDelete = macroRepository.findAll().size();

        // Get the macro
        restMacroMockMvc.perform(delete("/api/macros/{id}", macro.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeDelete - 1);
    }
}
