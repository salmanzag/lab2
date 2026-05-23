package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {

	public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, "MariaDB");
	}

	public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, "MongoDB");
	}

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		return new EstudioResponse(
				study.getPerson() != null ? study.getPerson().getIdentification() + "" : "",
				study.getProfession() != null ? study.getProfession().getIdentification() + "" : "",
				study.getGraduationDate() != null ? study.getGraduationDate().toString() : "",
				study.getUniversityName() != null ? study.getUniversityName() : "",
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		Study study = new Study();
		Person person = new Person();
		person.setIdentification(Integer.parseInt(request.getPersonId()));
		study.setPerson(person);
		Profession profession = new Profession();
		profession.setIdentification(Integer.parseInt(request.getProfessionId()));
		study.setProfession(profession);
		study.setGraduationDate(
				request.getGraduationDate() != null && !request.getGraduationDate().isEmpty()
						? LocalDate.parse(request.getGraduationDate())
						: null);
		study.setUniversityName(request.getUniversityName());
		return study;
	}
}
