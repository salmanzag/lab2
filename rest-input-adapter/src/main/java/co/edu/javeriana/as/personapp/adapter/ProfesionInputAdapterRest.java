package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	ProfessionInputPort professionInputPort;

	private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<ProfesionResponse> historial(String database) {
		log.info("Into historial ProfesionEntity in Input Adapter");
		try {
			if (setProfessionOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return professionInputPort.findAll().stream().map(profesionMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			} else {
				return professionInputPort.findAll().stream().map(profesionMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<ProfesionResponse>();
		}
	}

	public ProfesionResponse crearProfesion(ProfesionRequest request) {
		try {
			String db = setProfessionOutputPortInjection(request.getDatabase());
			Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? profesionMapperRest.fromDomainToAdapterRestMaria(profession)
					: profesionMapperRest.fromDomainToAdapterRestMongo(profession);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse obtenerProfesion(String database, Integer id) {
		try {
			String db = setProfessionOutputPortInjection(database);
			Profession profession = professionInputPort.findOne(id);
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? profesionMapperRest.fromDomainToAdapterRestMaria(profession)
					: profesionMapperRest.fromDomainToAdapterRestMongo(profession);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse editarProfesion(ProfesionRequest request) {
		try {
			String db = setProfessionOutputPortInjection(request.getDatabase());
			Profession profession = professionInputPort.edit(Integer.parseInt(request.getId()),
					profesionMapperRest.fromAdapterToDomain(request));
			return db.equalsIgnoreCase(DatabaseOption.MARIA.toString())
					? profesionMapperRest.fromDomainToAdapterRestMaria(profession)
					: profesionMapperRest.fromDomainToAdapterRestMongo(profession);
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}

	public ProfesionResponse eliminarProfesion(String database, Integer id) {
		try {
			setProfessionOutputPortInjection(database);
			professionInputPort.drop(id);
			return new ProfesionResponse(id + "", "", "", database, "DELETED");
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
		}
		return null;
	}
}
