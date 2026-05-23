package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telefono")
@Tag(name = "Telefono", description = "CRUD de Telefono")
public class TelefonoControllerV1 {

	@Autowired
	private TelefonoInputAdapterRest telefonoInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Listar todos los telefonos")
	public List<TelefonoResponse> telefonos(@PathVariable String database) {
		log.info("Into telefonos REST API");
		return telefonoInputAdapterRest.historial(database.toUpperCase());
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Buscar telefono por numero")
	public TelefonoResponse telefono(@PathVariable String database, @PathVariable String number) {
		log.info("Into telefono by number REST API");
		return telefonoInputAdapterRest.obtenerTelefono(database.toUpperCase(), number);
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Crear nuevo telefono")
	public TelefonoResponse crearTelefono(@RequestBody TelefonoRequest request) {
		log.info("Into crearTelefono REST API");
		return telefonoInputAdapterRest.crearTelefono(request);
	}

	@ResponseBody
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Actualizar telefono")
	public TelefonoResponse editarTelefono(@RequestBody TelefonoRequest request) {
		log.info("Into editarTelefono REST API");
		return telefonoInputAdapterRest.editarTelefono(request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Eliminar telefono")
	public TelefonoResponse eliminarTelefono(@PathVariable String database, @PathVariable String number) {
		log.info("Into eliminarTelefono REST API");
		return telefonoInputAdapterRest.eliminarTelefono(database.toUpperCase(), number);
	}
}
