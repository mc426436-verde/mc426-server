package br.unicamp.ic.timerverde.repository;

import br.unicamp.ic.timerverde.domain.Share;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Share entity.
 */
@SuppressWarnings("unused")
public interface ShareRepository extends JpaRepository<Share,Long> {

    @Query("select share from Share share where share.user.login = ?#{principal.username}")
    List<Share> findByUserIsCurrentUser();

}
