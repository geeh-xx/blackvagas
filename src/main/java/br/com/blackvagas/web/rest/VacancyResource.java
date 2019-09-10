package br.com.blackvagas.web.rest;

import br.com.blackvagas.service.VacancyService;
import br.com.blackvagas.web.rest.errors.BadRequestAlertException;
import br.com.blackvagas.service.dto.VacancyDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.blackvagas.domain.Vacancy}.
 */
@RestController
@RequestMapping("/api")
public class VacancyResource {

    private final Logger log = LoggerFactory.getLogger(VacancyResource.class);

    private static final String ENTITY_NAME = "vacancy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VacancyService vacancyService;

    public VacancyResource(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    /**
     * {@code POST  /vacancies} : Create a new vacancy.
     *
     * @param vacancyDTO the vacancyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vacancyDTO, or with status {@code 400 (Bad Request)} if the vacancy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vacancies")
    public ResponseEntity<VacancyDTO> createVacancy(@Valid @RequestBody VacancyDTO vacancyDTO) throws URISyntaxException {
        log.debug("REST request to save Vacancy : {}", vacancyDTO);
        if (vacancyDTO.getId() != null) {
            throw new BadRequestAlertException("A new vacancy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VacancyDTO result = vacancyService.save(vacancyDTO);
        return ResponseEntity.created(new URI("/api/vacancies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vacancies} : Updates an existing vacancy.
     *
     * @param vacancyDTO the vacancyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vacancyDTO,
     * or with status {@code 400 (Bad Request)} if the vacancyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vacancyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vacancies")
    public ResponseEntity<VacancyDTO> updateVacancy(@Valid @RequestBody VacancyDTO vacancyDTO) throws URISyntaxException {
        log.debug("REST request to update Vacancy : {}", vacancyDTO);
        if (vacancyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VacancyDTO result = vacancyService.save(vacancyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vacancyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vacancies} : get all the vacancies.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vacancies in body.
     */
    @GetMapping("/vacancies")
    public ResponseEntity<List<VacancyDTO>> getAllVacancies(Pageable pageable) {
        log.debug("REST request to get a page of Vacancies");
        Page<VacancyDTO> page = vacancyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vacancies/:id} : get the "id" vacancy.
     *
     * @param id the id of the vacancyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vacancyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vacancies/{id}")
    public ResponseEntity<VacancyDTO> getVacancy(@PathVariable Long id) {
        log.debug("REST request to get Vacancy : {}", id);
        Optional<VacancyDTO> vacancyDTO = vacancyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vacancyDTO);
    }

    /**
     * {@code DELETE  /vacancies/:id} : delete the "id" vacancy.
     *
     * @param id the id of the vacancyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vacancies/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        log.debug("REST request to delete Vacancy : {}", id);
        vacancyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
