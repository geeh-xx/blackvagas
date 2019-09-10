package br.com.blackvagas.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Vacancy.
 */
@Entity
@Table(name = "vacancy")
public class Vacancy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "number_candidats", nullable = false)
    private Integer numberCandidats;

    @Column(name = "date_limit")
    private LocalDate dateLimit;

    @Column(name = "date_publication")
    private LocalDate datePublication;

    @ManyToOne
    @JsonIgnoreProperties("vacancies")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Vacancy description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberCandidats() {
        return numberCandidats;
    }

    public Vacancy numberCandidats(Integer numberCandidats) {
        this.numberCandidats = numberCandidats;
        return this;
    }

    public void setNumberCandidats(Integer numberCandidats) {
        this.numberCandidats = numberCandidats;
    }

    public LocalDate getDateLimit() {
        return dateLimit;
    }

    public Vacancy dateLimit(LocalDate dateLimit) {
        this.dateLimit = dateLimit;
        return this;
    }

    public void setDateLimit(LocalDate dateLimit) {
        this.dateLimit = dateLimit;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public Vacancy datePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
        return this;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public Company getCompany() {
        return company;
    }

    public Vacancy company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy)) {
            return false;
        }
        return id != null && id.equals(((Vacancy) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", numberCandidats=" + getNumberCandidats() +
            ", dateLimit='" + getDateLimit() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            "}";
    }
}
