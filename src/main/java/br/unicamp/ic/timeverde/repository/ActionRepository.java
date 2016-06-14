package br.unicamp.ic.timeverde.repository;

import br.unicamp.ic.timeverde.domain.Action;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Action entity.
 */
@SuppressWarnings("unused")
public interface ActionRepository extends JpaRepository<Action,Long> {

}
