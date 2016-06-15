package br.unicamp.ic.timeverde.repository;

import br.unicamp.ic.timeverde.domain.Macro;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Macro entity.
 */
@SuppressWarnings("unused")
public interface MacroRepository extends JpaRepository<Macro,Long> {

    @Query("select distinct macro from Macro macro left join fetch macro.actions")
    List<Macro> findAllWithEagerRelationships();

    @Query("select macro from Macro macro left join fetch macro.actions where macro.id =:id")
    Macro findOneWithEagerRelationships(@Param("id") Long id);

}
