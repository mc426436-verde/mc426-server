package br.unicamp.ic.timeverde.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import br.unicamp.ic.timeverde.domain.enumeration.DeviceStatusEnum;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_description")
    private String deviceDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatusEnum status;

    @ManyToOne
    private Room room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
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
            ", deviceName='" + deviceName + "'" +
            ", deviceDescription='" + deviceDescription + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
