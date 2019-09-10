package br.com.blackvagas.service;

import br.com.blackvagas.service.dto.VacancyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link br.com.blackvagas.domain.Vacancy}.
 */
public interface VacancyService {

    /**
     * Save a vacancy.
     *
     * @param vacancyDTO the entity to save.
     * @return the persisted entity.
     */
    VacancyDTO save(VacancyDTO vacancyDTO);

    /**
     * Get all the vacancies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VacancyDTO> findAll(Pageable pageable);


    /**
     * Get the "id" vacancy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VacancyDTO> findOne(Long id);

    /**
     * Delete the "id" vacancy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
