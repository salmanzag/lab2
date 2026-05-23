package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
@Tag(name = "Persona", description = "CRUD de Persona")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Listar todas las personas")
	public List<PersonaResponse> personas(@PathVariable String database) {
		log.info("Into personas REST API");
			return personaInputAdapterRest.historial(database.toUpperCase());
	}
	
	@ResponseBody
	@GetMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar persona por cédula")
	public PersonaResponse persona(@PathVariable String database, @PathVariable Integer cc) {
		log.info("Into persona by id REST API");
		return personaInputAdapterRest.obtenerPersona(database.toUpperCase(), cc);
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Crear nueva persona")
	public PersonaResponse crearPersona(@RequestBody PersonaRequest request) {
		log.info("Into crearPersona REST API");
		return personaInputAdapterRest.crearPersona(request);
	}
	
	@ResponseBody
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Actualizar persona")
	public PersonaResponse editarPersona(@RequestBody PersonaRequest request) {
		log.info("Into editarPersona REST API");
		return personaInputAdapterRest.editarPersona(request);
	}
	
	@ResponseBody
	@DeleteMapping(path = "/{database}/{cc}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Eliminar persona")
	public PersonaResponse eliminarPersona(@PathVariable String database, @PathVariable Integer cc) {
		log.info("Into eliminarPersona REST API");
		return personaInputAdapterRest.eliminarPersona(database.toUpperCase(), cc);
	}
}
