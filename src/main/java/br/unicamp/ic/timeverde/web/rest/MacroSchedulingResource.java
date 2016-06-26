package br.unicamp.ic.timeverde.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.unicamp.ic.timeverde.domain.MacroScheduling;
import br.unicamp.ic.timeverde.repository.MacroSchedulingRepository;
import br.unicamp.ic.timeverde.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MacroScheduling.
 */
@RestController
@RequestMapping("/api")
public class MacroSchedulingResource {

    private final Logger log = LoggerFactory.getLogger(MacroSchedulingResource.class);
        
    @Inject
    private MacroSchedulingRepository macroSchedulingRepository;
    
    /**
     * POST  /macro-schedulings : Create a new macroScheduling.
     *
     * @param macroScheduling the macroScheduling to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macroScheduling, or with status 400 (Bad Request) if the macroScheduling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/macro-schedulings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MacroScheduling> createMacroScheduling(@RequestBody MacroScheduling macroScheduling) throws URISyntaxException {
        log.debug("REST request to save MacroScheduling : {}", macroScheduling);
        if (macroScheduling.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("macroScheduling", "idexists", "A new macroScheduling cannot already have an ID")).body(null);
        }
        MacroScheduling result = macroSchedulingRepository.save(macroScheduling);
        return ResponseEntity.created(new URI("/api/macro-schedulings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("macroScheduling", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macro-schedulings : Updates an existing macroScheduling.
     *
     * @param macroScheduling the macroScheduling to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macroScheduling,
     * or with status 400 (Bad Request) if the macroScheduling is not valid,
     * or with status 500 (Internal Server Error) if the macroScheduling couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/macro-schedulings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MacroScheduling> updateMacroScheduling(@RequestBody MacroScheduling macroScheduling) throws URISyntaxException {
        log.debug("REST request to update MacroScheduling : {}", macroScheduling);
        if (macroScheduling.getId() == null) {
            return createMacroScheduling(macroScheduling);
        }
        MacroScheduling result = macroSchedulingRepository.save(macroScheduling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("macroScheduling", macroScheduling.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macro-schedulings : get all the macroSchedulings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of macroSchedulings in body
     */
    @RequestMapping(value = "/macro-schedulings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MacroScheduling> getAllMacroSchedulings() {
        log.debug("REST request to get all MacroSchedulings");
        List<MacroScheduling> macroSchedulings = macroSchedulingRepository.findAll();
        return macroSchedulings;
    }

    /**
     * GET  /macro-schedulings/:id : get the "id" macroScheduling.
     *
     * @param id the id of the macroScheduling to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macroScheduling, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/macro-schedulings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MacroScheduling> getMacroScheduling(@PathVariable Long id) {
        log.debug("REST request to get MacroScheduling : {}", id);
        MacroScheduling macroScheduling = macroSchedulingRepository.findOne(id);
        return Optional.ofNullable(macroScheduling)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /macro-schedulings/:id : delete the "id" macroScheduling.
     *
     * @param id the id of the macroScheduling to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/macro-schedulings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMacroScheduling(@PathVariable Long id) {
        log.debug("REST request to delete MacroScheduling : {}", id);
        macroSchedulingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("macroScheduling", id.toString())).build();
    }

}
