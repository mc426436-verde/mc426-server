package br.unicamp.ic.timeverde.repository;

import br.unicamp.ic.timeverde.domain.MacroScheduling;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MacroScheduling entity.
 */
@SuppressWarnings("unused")
public interface MacroSchedulingRepository extends JpaRepository<MacroScheduling,Long> {

}
