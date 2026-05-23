package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/estudio")
@Tag(name = "Estudio", description = "CRUD de Estudio")
public class EstudioControllerV1 {

	@Autowired
	private EstudioInputAdapterRest estudioInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Listar todos los estudios")
	public List<EstudioResponse> estudios(@PathVariable String database) {
		log.info("Into estudios REST API");
		return estudioInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar estudio por persona y profesion")
	public EstudioResponse estudio(@PathVariable String database, @PathVariable Integer personId,
			@PathVariable Integer professionId) {
		log.info("Into estudio by id REST API");
		return estudioInputAdapterRest.obtenerEstudio(database.toUpperCase(), personId, professionId);
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Crear nuevo estudio")
	public EstudioResponse crearEstudio(@RequestBody EstudioRequest request) {
		log.info("Into crearEstudio REST API");
		return estudioInputAdapterRest.crearEstudio(request);
	}

	@ResponseBody
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Actualizar estudio")
	public EstudioResponse editarEstudio(@RequestBody EstudioRequest request) {
		log.info("Into editarEstudio REST API");
		return estudioInputAdapterRest.editarEstudio(request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{personId}/{professionId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Eliminar estudio")
	public EstudioResponse eliminarEstudio(@PathVariable String database, @PathVariable Integer personId,
			@PathVariable Integer professionId) {
		log.info("Into eliminarEstudio REST API");
		return estudioInputAdapterRest.eliminarEstudio(database.toUpperCase(), personId, professionId);
	}
}
