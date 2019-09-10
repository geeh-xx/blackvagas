package br.com.blackvagas.service.impl;

import br.com.blackvagas.service.VacancyService;
import br.com.blackvagas.domain.Vacancy;
import br.com.blackvagas.repository.VacancyRepository;
import br.com.blackvagas.service.dto.VacancyDTO;
import br.com.blackvagas.service.mapper.VacancyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Vacancy}.
 */
@Service
@Transactional
public class VacancyServiceImpl implements VacancyService {

    private final Logger log = LoggerFactory.getLogger(VacancyServiceImpl.class);

    private final VacancyRepository vacancyRepository;

    private final VacancyMapper vacancyMapper;

    public VacancyServiceImpl(VacancyRepository vacancyRepository, VacancyMapper vacancyMapper) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyMapper = vacancyMapper;
    }

    /**
     * Save a vacancy.
     *
     * @param vacancyDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VacancyDTO save(VacancyDTO vacancyDTO) {
        log.debug("Request to save Vacancy : {}", vacancyDTO);
        Vacancy vacancy = vacancyMapper.toEntity(vacancyDTO);
        vacancy = vacancyRepository.save(vacancy);
        return vacancyMapper.toDto(vacancy);
    }

    /**
     * Get all the vacancies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VacancyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vacancies");
        return vacancyRepository.findAll(pageable)
            .map(vacancyMapper::toDto);
    }


    /**
     * Get one vacancy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VacancyDTO> findOne(Long id) {
        log.debug("Request to get Vacancy : {}", id);
        return vacancyRepository.findById(id)
            .map(vacancyMapper::toDto);
    }

    /**
     * Delete the vacancy by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vacancy : {}", id);
        vacancyRepository.deleteById(id);
    }
}
