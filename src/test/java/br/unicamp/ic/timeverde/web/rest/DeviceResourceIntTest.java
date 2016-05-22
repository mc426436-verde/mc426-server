package br.unicamp.ic.timeverde.web.rest;

import br.unicamp.ic.timeverde.DinoApp;
import br.unicamp.ic.timeverde.domain.Device;
import br.unicamp.ic.timeverde.repository.DeviceRepository;

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

import br.unicamp.ic.timeverde.domain.enumeration.DeviceStatusEnum;

/**
 * Test class for the DeviceResource REST controller.
 *
 * @see DeviceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DinoApp.class)
@WebAppConfiguration
@IntegrationTest
public class DeviceResourceIntTest {

    private static final String DEFAULT_DEVICE_NAME = "AAAAA";
    private static final String UPDATED_DEVICE_NAME = "BBBBB";
    private static final String DEFAULT_DEVICE_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DEVICE_DESCRIPTION = "BBBBB";

    private static final DeviceStatusEnum DEFAULT_STATUS = DeviceStatusEnum.ON;
    private static final DeviceStatusEnum UPDATED_STATUS = DeviceStatusEnum.OFF;

    @Inject
    private DeviceRepository deviceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDeviceMockMvc;

    private Device device;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeviceResource deviceResource = new DeviceResource();
        ReflectionTestUtils.setField(deviceResource, "deviceRepository", deviceRepository);
        this.restDeviceMockMvc = MockMvcBuilders.standaloneSetup(deviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        device = new Device();
        device.setDeviceName(DEFAULT_DEVICE_NAME);
        device.setDeviceDescription(DEFAULT_DEVICE_DESCRIPTION);
        device.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // Create the Device

        restDeviceMockMvc.perform(post("/api/devices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> devices = deviceRepository.findAll();
        assertThat(devices).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = devices.get(devices.size() - 1);
        assertThat(testDevice.getDeviceName()).isEqualTo(DEFAULT_DEVICE_NAME);
        assertThat(testDevice.getDeviceDescription()).isEqualTo(DEFAULT_DEVICE_DESCRIPTION);
        assertThat(testDevice.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the devices
        restDeviceMockMvc.perform(get("/api/devices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
                .andExpect(jsonPath("$.[*].deviceName").value(hasItem(DEFAULT_DEVICE_NAME.toString())))
                .andExpect(jsonPath("$.[*].deviceDescription").value(hasItem(DEFAULT_DEVICE_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.deviceName").value(DEFAULT_DEVICE_NAME.toString()))
            .andExpect(jsonPath("$.deviceDescription").value(DEFAULT_DEVICE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = new Device();
        updatedDevice.setId(device.getId());
        updatedDevice.setDeviceName(UPDATED_DEVICE_NAME);
        updatedDevice.setDeviceDescription(UPDATED_DEVICE_DESCRIPTION);
        updatedDevice.setStatus(UPDATED_STATUS);

        restDeviceMockMvc.perform(put("/api/devices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDevice)))
                .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> devices = deviceRepository.findAll();
        assertThat(devices).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = devices.get(devices.size() - 1);
        assertThat(testDevice.getDeviceName()).isEqualTo(UPDATED_DEVICE_NAME);
        assertThat(testDevice.getDeviceDescription()).isEqualTo(UPDATED_DEVICE_DESCRIPTION);
        assertThat(testDevice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);
        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Get the device
        restDeviceMockMvc.perform(delete("/api/devices/{id}", device.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Device> devices = deviceRepository.findAll();
        assertThat(devices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
