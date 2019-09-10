package br.com.blackvagas.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.blackvagas.domain.Vacancy} entity.
 */
public class VacancyDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull
    private String description;

    @NotNull
    private Integer numberCandidats;

    private LocalDate dateLimit;

    private LocalDate datePublication;


    private Long companyId;
    
    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumberCandidats() {
        return numberCandidats;
    }

    public void setNumberCandidats(Integer numberCandidats) {
        this.numberCandidats = numberCandidats;
    }

    public LocalDate getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(LocalDate dateLimit) {
        this.dateLimit = dateLimit;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VacancyDTO vacancyDTO = (VacancyDTO) o;
        if (vacancyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vacancyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VacancyDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", numberCandidats=" + getNumberCandidats() +
            ", dateLimit='" + getDateLimit() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", company=" + getCompanyId() +
            "}";
    }

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
