package br.unicamp.ic.timerverde.repository;

import br.unicamp.ic.timerverde.domain.Macro;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Macro entity.
 */
@SuppressWarnings("unused")
public interface MacroRepository extends JpaRepository<Macro,Long> {

}
