package com.apress.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apress.domain.Poll;
import com.apress.repository.PollRepository;

@RestController
public class PollController {

	@Autowired
	private PollRepository pollRepository;

	// creo una Encuesta. Con @RequestBody se va estar vinculando el body de la peticion
	@RequestMapping(value = "/polls", method = RequestMethod.POST)
	public ResponseEntity<?> createPoll(@RequestBody Poll poll) {

		// recibo el elemento Poll mediante la variable poll y lo guardo
		poll = pollRepository.save(poll);
	
		// creo los headers que van a ser enviados en la respuesta
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder
							.fromCurrentRequest() 			// ver NOTAS 1 - ServletUriComponentsBuilder
							.path("/{id}")					// ver NOTAS 2 - UriComponentsBuilder
							.buildAndExpand(poll.getId())	// ver NOTAS 3 - UriComponentsBuilder
							.toUri();						// genera la URI
		responseHeaders.setLocation(newPollUri);			// seteo la ubicacion del header para la nueva entidad creada
		
		// envio respuesta 201 (creado)
		// body: null
		// headers: responseHeaders
		// statusCode: 201
		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	// obtengo las Encuestas
	@RequestMapping(value = "/polls", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Poll>> getAllPolls() {

		// obtengo un Iterable que es una interfaz que permite
		// a un objeto ser el objetivo de un blucle for-each
		Iterable<Poll> allPolls = pollRepository.findAll();

		// envio la respuesta 200 (ok, salio todo bien) pasando el iterable que
		// representa todas las entidades encuestas
		// encontradas por el repositorio
		return new ResponseEntity<>(allPolls, HttpStatus.OK);
	}

	// obtengo una Encuesta a partir de su id
	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPoll(@PathVariable Long pollId) {

		// instancio una entidad Encuesta consultando al repositorio
		Poll p = pollRepository.findOne(pollId);
		
		// envio una respuesta 200 (OK) pasando pasandole la entidad Encuesta encontrada
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	// actualizo una Encuesta mandando todo el objeto Poll
	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {

		// guardo la Encuesta en el repositorio
		pollRepository.save(poll);

		// envio respuesta 200 (OK)
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// borro una Encuesta a partir de su id
	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {

		// borro la Encuesta del repositorio
		pollRepository.delete(pollId);

		// envio respuesta 200 (OK)
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// actualizo una Encuesta con PATCH ==> solo mandando el campo a actualizar con Patch
	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PATCH)
	public ResponseEntity<?> updateWithPatch(@PathVariable Long pollId, @RequestBody PatchRequest patch){
		
		// obtengo la encuesta por el id en el pathvariable
		Poll poll = pollRepository.findOne(pollId);
		
		// si es null, no se encontró. Si se encontró
		if(poll!=null) {
			if(patch.getReplace().equals("question")) {
				poll.setQuestion(patch.getValue());
			}
			else if(patch.getReplace().equals("name")) {
				poll.setName(patch.getValue());
			}
			else {
				return new ResponseEntity<>("No se encontró la encuesta", HttpStatus.NOT_FOUND);
			}
		}
		pollRepository.save(poll);
		
		return new ResponseEntity<>(poll, HttpStatus.OK);
	}
	
}

//------------------------------------------------------------------------------------
// NOTAS
//------------------------------------------------------------------------------------
/*
 * 1:
 * --
 * Same as fromRequest(HttpServletRequest) except the request is obtained 
 * through RequestContextHolder:
 * 
 * public static ServletUriComponentsBuilder fromCurrentRequest() {
 * 		return fromRequest(getCurrentRequest());
 * }
 * ------------------------------------------------------------------------------------
 * Prepare a builder from the host, port, scheme, and path (but not the query)
 * of the HttpServletRequest:
 * 
 * public static ServletUriComponentsBuilder fromRequest(HttpServletRequest request){
 * 	...
 * }
 * ------------------------------------------------------------------------------------
 * protected static HttpServletRequest getCurrentRequest() {
 * 		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
 * 		...
 * 		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
 * 		return servletRequest;
 * 	}
 * ------------------------------------------------------------------------------------
 * 2:
 * --
 * 	Adjunta la path dada a la path existente del UriComponentsBuilder. 
 * 	La path dada puede contener variables de template de UR
 * ------------------------------------------------------------------------------------
 * 3:
 * --
 * 	crea una instancia de UriComponents y reemplaza las variables plantilla de URI 
 * 	por los valores del arreglo
 * ------------------------------------------------------------------------------------
 * 4:
 * --
 * 	@RequestBody indica que un parametro de metodo debe estar vinculada al cuerpo de la solicitu web
 *  El cuerpo de la solicitud se pasa a través de un HttpMessageConverter para resolver el
  * argumento del método según el tipo de contenido de la solicitud. Opcionalmente, la validación 
  * automática se puede aplicar anotando el argumento con @Valid
  *
 */	