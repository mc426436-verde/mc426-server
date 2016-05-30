package br.unicamp.ic.timerverde.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.unicamp.ic.timerverde.domain.Share;
import br.unicamp.ic.timerverde.repository.ShareRepository;
import br.unicamp.ic.timerverde.web.rest.util.HeaderUtil;
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
 * REST controller for managing Share.
 */
@RestController
@RequestMapping("/api")
public class ShareResource {

    private final Logger log = LoggerFactory.getLogger(ShareResource.class);
        
    @Inject
    private ShareRepository shareRepository;
    
    /**
     * POST  /shares : Create a new share.
     *
     * @param share the share to create
     * @return the ResponseEntity with status 201 (Created) and with body the new share, or with status 400 (Bad Request) if the share has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shares",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Share> createShare(@RequestBody Share share) throws URISyntaxException {
        log.debug("REST request to save Share : {}", share);
        if (share.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("share", "idexists", "A new share cannot already have an ID")).body(null);
        }
        Share result = shareRepository.save(share);
        return ResponseEntity.created(new URI("/api/shares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("share", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shares : Updates an existing share.
     *
     * @param share the share to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated share,
     * or with status 400 (Bad Request) if the share is not valid,
     * or with status 500 (Internal Server Error) if the share couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shares",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Share> updateShare(@RequestBody Share share) throws URISyntaxException {
        log.debug("REST request to update Share : {}", share);
        if (share.getId() == null) {
            return createShare(share);
        }
        Share result = shareRepository.save(share);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("share", share.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shares : get all the shares.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shares in body
     */
    @RequestMapping(value = "/shares",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Share> getAllShares() {
        log.debug("REST request to get all Shares");
        List<Share> shares = shareRepository.findAll();
        return shares;
    }

    /**
     * GET  /shares/:id : get the "id" share.
     *
     * @param id the id of the share to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the share, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shares/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Share> getShare(@PathVariable Long id) {
        log.debug("REST request to get Share : {}", id);
        Share share = shareRepository.findOne(id);
        return Optional.ofNullable(share)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shares/:id : delete the "id" share.
     *
     * @param id the id of the share to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shares/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShare(@PathVariable Long id) {
        log.debug("REST request to delete Share : {}", id);
        shareRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("share", id.toString())).build();
    }

}
