package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial PersonaEntity in Input Adapter");
		personInputPort.findAll().stream()
			.map(personaMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void crear(Scanner keyboard) {
		System.out.print("Ingrese la cédula: ");
		Integer cc = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el apellido: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese el género (M/F): ");
		String genero = keyboard.nextLine();
		System.out.print("Ingrese la edad: ");
		Integer edad = keyboard.nextInt(); keyboard.nextLine();

		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender("F".equalsIgnoreCase(genero) ? Gender.FEMALE : Gender.MALE);
		person.setAge(edad);

		personInputPort.create(person);
		System.out.println("Persona creada exitosamente.");
	}

	public void buscar(Scanner keyboard) {
		System.out.print("Ingrese la cédula a buscar: ");
		Integer cc = keyboard.nextInt(); keyboard.nextLine();
		try {
			Person person = personInputPort.findOne(cc);
			System.out.println(personaMapperCli.fromDomainToAdapterCli(person));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void editar(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona a editar: ");
		Integer cc = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el nuevo nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el nuevo apellido: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese el nuevo género (M/F): ");
		String genero = keyboard.nextLine();
		System.out.print("Ingrese la nueva edad: ");
		Integer edad = keyboard.nextInt(); keyboard.nextLine();

		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender("F".equalsIgnoreCase(genero) ? Gender.FEMALE : Gender.MALE);
		person.setAge(edad);

		try {
			personInputPort.edit(cc, person);
			System.out.println("Persona editada exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Scanner keyboard) {
		System.out.print("Ingrese la cédula de la persona a eliminar: ");
		Integer cc = keyboard.nextInt(); keyboard.nextLine();
		try {
			personInputPort.drop(cc);
			System.out.println("Persona eliminada exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}
}
