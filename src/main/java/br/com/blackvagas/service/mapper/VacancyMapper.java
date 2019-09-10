package br.com.blackvagas.service.mapper;

import br.com.blackvagas.domain.*;
import br.com.blackvagas.service.dto.VacancyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vacancy} and its DTO {@link VacancyDTO}.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface VacancyMapper extends EntityMapper<VacancyDTO, Vacancy> {

    @Mappings({@Mapping(source = "company.id", target = "companyId" ),
    			@Mapping(source = "company.name", target = "companyName")})
    VacancyDTO toDto(Vacancy vacancy);

    @Mapping(source = "companyId", target = "company")
    Vacancy toEntity(VacancyDTO vacancyDTO);

    default Vacancy fromId(Long id) {
        if (id == null) {
            return null;
        }
        Vacancy vacancy = new Vacancy();
        vacancy.setId(id);
        return vacancy;
    }
}
