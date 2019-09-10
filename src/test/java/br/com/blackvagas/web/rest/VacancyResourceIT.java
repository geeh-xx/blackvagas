package br.com.blackvagas.web.rest;

import br.com.blackvagas.BlackvagasApp;
import br.com.blackvagas.domain.Vacancy;
import br.com.blackvagas.repository.VacancyRepository;
import br.com.blackvagas.service.VacancyService;
import br.com.blackvagas.service.dto.VacancyDTO;
import br.com.blackvagas.service.mapper.VacancyMapper;
import br.com.blackvagas.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.com.blackvagas.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VacancyResource} REST controller.
 */
@SpringBootTest(classes = BlackvagasApp.class)
public class VacancyResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_CANDIDATS = 1;
    private static final Integer UPDATED_NUMBER_CANDIDATS = 2;
    private static final Integer SMALLER_NUMBER_CANDIDATS = 1 - 1;

    private static final LocalDate DEFAULT_DATE_LIMIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_LIMIT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_LIMIT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_PUBLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUBLICATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PUBLICATION = LocalDate.ofEpochDay(-1L);

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private VacancyMapper vacancyMapper;

    @Autowired
    private VacancyService vacancyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVacancyMockMvc;

    private Vacancy vacancy;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VacancyResource vacancyResource = new VacancyResource(vacancyService);
        this.restVacancyMockMvc = MockMvcBuilders.standaloneSetup(vacancyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .description(DEFAULT_DESCRIPTION)
            .numberCandidats(DEFAULT_NUMBER_CANDIDATS)
            .dateLimit(DEFAULT_DATE_LIMIT)
            .datePublication(DEFAULT_DATE_PUBLICATION);
        return vacancy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacancy createUpdatedEntity(EntityManager em) {
        Vacancy vacancy = new Vacancy()
            .description(UPDATED_DESCRIPTION)
            .numberCandidats(UPDATED_NUMBER_CANDIDATS)
            .dateLimit(UPDATED_DATE_LIMIT)
            .datePublication(UPDATED_DATE_PUBLICATION);
        return vacancy;
    }

    @BeforeEach
    public void initTest() {
        vacancy = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacancy() throws Exception {
        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);
        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isCreated());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate + 1);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVacancy.getNumberCandidats()).isEqualTo(DEFAULT_NUMBER_CANDIDATS);
        assertThat(testVacancy.getDateLimit()).isEqualTo(DEFAULT_DATE_LIMIT);
        assertThat(testVacancy.getDatePublication()).isEqualTo(DEFAULT_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void createVacancyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacancyRepository.findAll().size();

        // Create the Vacancy with an existing ID
        vacancy.setId(1L);
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacancyRepository.findAll().size();
        // set the field null
        vacancy.setDescription(null);

        // Create the Vacancy, which fails.
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isBadRequest());

        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberCandidatsIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacancyRepository.findAll().size();
        // set the field null
        vacancy.setNumberCandidats(null);

        // Create the Vacancy, which fails.
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        restVacancyMockMvc.perform(post("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isBadRequest());

        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacancies() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get all the vacancyList
        restVacancyMockMvc.perform(get("/api/vacancies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].numberCandidats").value(hasItem(DEFAULT_NUMBER_CANDIDATS)))
            .andExpect(jsonPath("$.[*].dateLimit").value(hasItem(DEFAULT_DATE_LIMIT.toString())))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        // Get the vacancy
        restVacancyMockMvc.perform(get("/api/vacancies/{id}", vacancy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vacancy.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.numberCandidats").value(DEFAULT_NUMBER_CANDIDATS))
            .andExpect(jsonPath("$.dateLimit").value(DEFAULT_DATE_LIMIT.toString()))
            .andExpect(jsonPath("$.datePublication").value(DEFAULT_DATE_PUBLICATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVacancy() throws Exception {
        // Get the vacancy
        restVacancyMockMvc.perform(get("/api/vacancies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Update the vacancy
        Vacancy updatedVacancy = vacancyRepository.findById(vacancy.getId()).get();
        // Disconnect from session so that the updates on updatedVacancy are not directly saved in db
        em.detach(updatedVacancy);
        updatedVacancy
            .description(UPDATED_DESCRIPTION)
            .numberCandidats(UPDATED_NUMBER_CANDIDATS)
            .dateLimit(UPDATED_DATE_LIMIT)
            .datePublication(UPDATED_DATE_PUBLICATION);
        VacancyDTO vacancyDTO = vacancyMapper.toDto(updatedVacancy);

        restVacancyMockMvc.perform(put("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isOk());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
        Vacancy testVacancy = vacancyList.get(vacancyList.size() - 1);
        assertThat(testVacancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVacancy.getNumberCandidats()).isEqualTo(UPDATED_NUMBER_CANDIDATS);
        assertThat(testVacancy.getDateLimit()).isEqualTo(UPDATED_DATE_LIMIT);
        assertThat(testVacancy.getDatePublication()).isEqualTo(UPDATED_DATE_PUBLICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingVacancy() throws Exception {
        int databaseSizeBeforeUpdate = vacancyRepository.findAll().size();

        // Create the Vacancy
        VacancyDTO vacancyDTO = vacancyMapper.toDto(vacancy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVacancyMockMvc.perform(put("/api/vacancies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacancyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vacancy in the database
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVacancy() throws Exception {
        // Initialize the database
        vacancyRepository.saveAndFlush(vacancy);

        int databaseSizeBeforeDelete = vacancyRepository.findAll().size();

        // Delete the vacancy
        restVacancyMockMvc.perform(delete("/api/vacancies/{id}", vacancy.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        assertThat(vacancyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vacancy.class);
        Vacancy vacancy1 = new Vacancy();
        vacancy1.setId(1L);
        Vacancy vacancy2 = new Vacancy();
        vacancy2.setId(vacancy1.getId());
        assertThat(vacancy1).isEqualTo(vacancy2);
        vacancy2.setId(2L);
        assertThat(vacancy1).isNotEqualTo(vacancy2);
        vacancy1.setId(null);
        assertThat(vacancy1).isNotEqualTo(vacancy2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacancyDTO.class);
        VacancyDTO vacancyDTO1 = new VacancyDTO();
        vacancyDTO1.setId(1L);
        VacancyDTO vacancyDTO2 = new VacancyDTO();
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
        vacancyDTO2.setId(vacancyDTO1.getId());
        assertThat(vacancyDTO1).isEqualTo(vacancyDTO2);
        vacancyDTO2.setId(2L);
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
        vacancyDTO1.setId(null);
        assertThat(vacancyDTO1).isNotEqualTo(vacancyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(vacancyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(vacancyMapper.fromId(null)).isNull();
    }
}
