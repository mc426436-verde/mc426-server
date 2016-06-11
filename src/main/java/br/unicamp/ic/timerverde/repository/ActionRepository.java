package br.unicamp.ic.timerverde.repository;

import br.unicamp.ic.timerverde.domain.Action;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Action entity.
 */
@SuppressWarnings("unused")
public interface ActionRepository extends JpaRepository<Action,Long> {

}
