package br.unicamp.ic.timerverde.repository;

import br.unicamp.ic.timerverde.domain.Device;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Device entity.
 */
public interface DeviceRepository extends JpaRepository<Device,Long> {

    @Query("select distinct device from Device device left join fetch device.shares")
    List<Device> findAllWithEagerRelationships();

    @Query("select device from Device device left join fetch device.shares where device.id =:id")
    Device findOneWithEagerRelationships(@Param("id") Long id);

}
