package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial ProfesionEntity in Input Adapter");
		professionInputPort.findAll().stream()
			.map(profesionMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void crear(Scanner keyboard) {
		System.out.print("Ingrese el ID de la profesión: ");
		Integer id = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese la descripción: ");
		String descripcion = keyboard.nextLine();

		Profession profession = new Profession();
		profession.setIdentification(id);
		profession.setName(nombre);
		profession.setDescription(descripcion);

		professionInputPort.create(profession);
		System.out.println("Profesión creada exitosamente.");
	}

	public void buscar(Scanner keyboard) {
		System.out.print("Ingrese el ID de la profesión: ");
		Integer id = keyboard.nextInt(); keyboard.nextLine();
		try {
			Profession profession = professionInputPort.findOne(id);
			System.out.println(profesionMapperCli.fromDomainToAdapterCli(profession));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void editar(Scanner keyboard) {
		System.out.print("Ingrese el ID de la profesión a editar: ");
		Integer id = keyboard.nextInt(); keyboard.nextLine();
		System.out.print("Ingrese el nuevo nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese la nueva descripción: ");
		String descripcion = keyboard.nextLine();

		Profession profession = new Profession();
		profession.setIdentification(id);
		profession.setName(nombre);
		profession.setDescription(descripcion);

		try {
			professionInputPort.edit(id, profession);
			System.out.println("Profesión editada exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Scanner keyboard) {
		System.out.print("Ingrese el ID de la profesión a eliminar: ");
		Integer id = keyboard.nextInt(); keyboard.nextLine();
		try {
			professionInputPort.drop(id);
			System.out.println("Profesión eliminada exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}
}
