package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

	public EstudioModelCli fromDomainToAdapterCli(Study study) {
		EstudioModelCli model = new EstudioModelCli();
		model.setIdProfesion(study.getProfession() != null ? study.getProfession().getIdentification() : null);
		model.setCcPersona(study.getPerson() != null ? study.getPerson().getIdentification() : null);
		model.setFecha(study.getGraduationDate() != null ? study.getGraduationDate().toString() : "");
		model.setUniversidad(study.getUniversityName());
		return model;
	}
}
