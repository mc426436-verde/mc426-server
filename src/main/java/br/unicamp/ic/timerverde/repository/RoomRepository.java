package br.unicamp.ic.timerverde.repository;

import br.unicamp.ic.timerverde.domain.Room;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Room entity.
 */
public interface RoomRepository extends JpaRepository<Room,Long> {

}
