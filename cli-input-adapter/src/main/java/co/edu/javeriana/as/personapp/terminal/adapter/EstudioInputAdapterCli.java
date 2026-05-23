package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	private EstudioMapperCli estudioMapperCli;

	StudyInputPort studyInputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial EstudioEntity in Input Adapter");
		studyInputPort.findAll().stream()
			.map(estudioMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void crear(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona: ");
		Integer ccPersona = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el ID de la profesión: ");
		Integer idProfesion = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese la fecha de graduación (YYYY-MM-DD) o vacío: ");
		String fecha = keyboard.nextLine();
		System.out.print("Ingrese el nombre de la universidad: ");
		String universidad = keyboard.nextLine();

		Study study = new Study();
		Person person = new Person();
		person.setIdentification(ccPersona);
		study.setPerson(person);
		
		Profession profession = new Profession();
		profession.setIdentification(idProfesion);
		study.setProfession(profession);
		
		if (!fecha.isBlank()) {
			study.setGraduationDate(LocalDate.parse(fecha));
		}
		study.setUniversityName(universidad);

		studyInputPort.create(study);
		System.out.println("Estudio creado exitosamente.");
	}

	public void buscar(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona: ");
		Integer ccPersona = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el ID de la profesión: ");
		Integer idProfesion = keyboard.nextInt(); keyboard.nextLine();
		try {
			Study study = studyInputPort.findOne(ccPersona, idProfesion);
			System.out.println(estudioMapperCli.fromDomainToAdapterCli(study));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void editar(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona del estudio a editar: ");
		Integer ccPersona = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el ID de la profesión del estudio a editar: ");
		Integer idProfesion = keyboard.nextInt(); keyboard.nextLine();
		
		System.out.print("Ingrese la nueva fecha de graduación (YYYY-MM-DD) o vacío: ");
		String fecha = keyboard.nextLine();
		System.out.print("Ingrese el nuevo nombre de la universidad: ");
		String universidad = keyboard.nextLine();

		Study study = new Study();
		Person person = new Person();
		person.setIdentification(ccPersona);
		study.setPerson(person);
		
		Profession profession = new Profession();
		profession.setIdentification(idProfesion);
		study.setProfession(profession);
		
		if (!fecha.isBlank()) {
			study.setGraduationDate(LocalDate.parse(fecha));
		}
		study.setUniversityName(universidad);

		try {
			studyInputPort.edit(ccPersona, idProfesion, study);
			System.out.println("Estudio editado exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona del estudio a eliminar: ");
		Integer ccPersona = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el ID de la profesión del estudio a eliminar: ");
		Integer idProfesion = keyboard.nextInt(); keyboard.nextLine();
		try {
			studyInputPort.drop(ccPersona, idProfesion);
			System.out.println("Estudio eliminado exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}
}
