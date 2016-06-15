package br.unicamp.ic.timeverde.domain;

import br.unicamp.ic.timeverde.repository.DeviceRepository;
import br.unicamp.ic.timeverde.web.rest.DeviceResource;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.unicamp.ic.timeverde.domain.enumeration.DeviceStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
public class Device implements Serializable {

    @Transient
    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    @Inject
    @Transient
    private Environment env;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatusEnum status;

    @ManyToOne
    private Room room;

    @ManyToMany
    @JoinTable(name = "device_user",
               joinColumns = @JoinColumn(name="devices_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DeviceStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DeviceStatusEnum status) {
        this.status = status;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Device device = (Device) o;
        if(device.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }

    public boolean updateArduino() {
        try {
            String ip = this.env.getProperty("dino.arduino.ip");
            String port = this.env.getProperty("dino.arduino.port");
            Socket clientSocket = new Socket(ip, Integer.valueOf(port));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(this.getId() + ":" + this.getStatus().value() + '\n');
            clientSocket.close();
        } catch (IOException e) {
            log.error("Failed to update arduino");
            return false;
        }
        return true;
    }
}
