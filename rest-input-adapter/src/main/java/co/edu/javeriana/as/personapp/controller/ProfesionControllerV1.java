package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
@Tag(name = "Profesion", description = "CRUD de Profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Listar todas las profesiones")
	public List<ProfesionResponse> profesiones(@PathVariable String database) {
		log.info("Into profesiones REST API");
		return profesionInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar profesion por ID")
	public ProfesionResponse profesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into profesion by id REST API");
		return profesionInputAdapterRest.obtenerProfesion(database.toUpperCase(), id);
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Crear nueva profesion")
	public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("Into crearProfesion REST API");
		return profesionInputAdapterRest.crearProfesion(request);
	}

	@ResponseBody
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Actualizar profesion")
	public ProfesionResponse editarProfesion(@RequestBody ProfesionRequest request) {
		log.info("Into editarProfesion REST API");
		return profesionInputAdapterRest.editarProfesion(request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Eliminar profesion")
	public ProfesionResponse eliminarProfesion(@PathVariable String database, @PathVariable Integer id) {
		log.info("Into eliminarProfesion REST API");
		return profesionInputAdapterRest.eliminarProfesion(database.toUpperCase(), id);
	}
}
