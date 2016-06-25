package br.unicamp.ic.timeverde.web.rest;

import br.unicamp.ic.timeverde.domain.Device;
import com.codahale.metrics.annotation.Timed;
import br.unicamp.ic.timeverde.domain.Macro;
import br.unicamp.ic.timeverde.repository.MacroRepository;
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
 * REST controller for managing Macro.
 */
@RestController
@RequestMapping("/api")
public class MacroResource {

    private final Logger log = LoggerFactory.getLogger(MacroResource.class);

    @Inject
    private MacroRepository macroRepository;

    @Inject
    private DeviceResource deviceResource;

    /**
     * POST  /macros : Create a new macro.
     *
     * @param macro the macro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new macro, or with status 400 (Bad Request) if the macro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/macros",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Macro> createMacro(@RequestBody Macro macro) throws URISyntaxException {
        log.debug("REST request to save Macro : {}", macro);
        if (macro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("macro", "idexists", "A new macro cannot already have an ID")).body(null);
        }
        Macro result = macroRepository.save(macro);
        return ResponseEntity.created(new URI("/api/macros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("macro", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /macros : Updates an existing macro.
     *
     * @param macro the macro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated macro,
     * or with status 400 (Bad Request) if the macro is not valid,
     * or with status 500 (Internal Server Error) if the macro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/macros",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Macro> updateMacro(@RequestBody Macro macro) throws URISyntaxException {
        log.debug("REST request to update Macro : {}", macro);
        if (macro.getId() == null) {
            return createMacro(macro);
        }
        Macro result = macroRepository.save(macro);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("macro", macro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /macros : get all the macros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of macros in body
     */
    @RequestMapping(value = "/macros",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Macro> getAllMacros() {
        log.debug("REST request to get all Macros");
        List<Macro> macros = macroRepository.findAllWithEagerRelationships();
        return macros;
    }

    /**
     * GET  /macros/:id : get the "id" macro.
     *
     * @param id the id of the macro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macro, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/macros/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Macro> getMacro(@PathVariable Long id) {
        log.debug("REST request to get Macro : {}", id);
        Macro macro = macroRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(macro)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /macros/:id : delete the "id" macro.
     *
     * @param id the id of the macro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/macros/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMacro(@PathVariable Long id) {
        log.debug("REST request to delete Macro : {}", id);
        macroRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("macro", id.toString())).build();
    }

    /**
     * GET  /macros/:id : runs the "id" macro.
     *
     * @param id the id of the macro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the macro, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/macros/run/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public boolean runMacro(@PathVariable Long id) {
        log.debug("REST request to get Macro : {}", id);
        Macro macro = macroRepository.findOneWithEagerRelationships(id);

        macro.getActions().forEach( action -> {
            Device device = action.getDevice();
            if(!device.getStatus().equals(action.getStatus())){
                deviceResource.toggleStatus(device.getId());
            }
        });
        return true;
    }
}
