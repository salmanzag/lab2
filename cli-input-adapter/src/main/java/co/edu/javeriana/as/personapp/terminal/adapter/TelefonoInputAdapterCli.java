package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	PhoneInputPort phoneInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial TelefonoEntity in Input Adapter");
		phoneInputPort.findAll().stream()
			.map(telefonoMapperCli::fromDomainToAdapterCli)
			.forEach(System.out::println);
	}

	public void crear(Scanner keyboard) {
		System.out.print("Ingrese el número de teléfono: ");
		String numero = keyboard.nextLine();
		System.out.print("Ingrese el operador: ");
		String operador = keyboard.nextLine();
		System.out.print("Ingrese la cédula del dueño: ");
		Integer duenioCc = keyboard.nextInt(); keyboard.nextLine();

		Phone phone = new Phone();
		phone.setNumber(numero);
		phone.setCompany(operador);
		Person owner = new Person();
		owner.setIdentification(duenioCc);
		phone.setOwner(owner);

		phoneInputPort.create(phone);
		System.out.println("Teléfono creado exitosamente.");
	}

	public void buscar(Scanner keyboard) {
		System.out.print("Ingrese el número de teléfono: ");
		String numero = keyboard.nextLine();
		try {
			Phone phone = phoneInputPort.findOne(numero);
			System.out.println(telefonoMapperCli.fromDomainToAdapterCli(phone));
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void editar(Scanner keyboard) {
		System.out.print("Ingrese el número de teléfono a editar: ");
		String numero = keyboard.nextLine();
		System.out.print("Ingrese el nuevo operador: ");
		String operador = keyboard.nextLine();
		System.out.print("Ingrese la nueva cédula del dueño: ");
		Integer duenioCc = keyboard.nextInt(); keyboard.nextLine();

		Phone phone = new Phone();
		phone.setNumber(numero);
		phone.setCompany(operador);
		Person owner = new Person();
		owner.setIdentification(duenioCc);
		phone.setOwner(owner);

		try {
			phoneInputPort.edit(numero, phone);
			System.out.println("Teléfono editado exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}

	public void eliminar(Scanner keyboard) {
		System.out.print("Ingrese el número de teléfono a eliminar: ");
		String numero = keyboard.nextLine();
		try {
			phoneInputPort.drop(numero);
			System.out.println("Teléfono eliminado exitosamente.");
		} catch (NoExistException e) {
			System.out.println(e.getMessage());
		}
	}
}
