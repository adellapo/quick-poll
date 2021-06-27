package com.apress.controller;

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

import com.apress.domain.Vote;
import com.apress.repository.VoteRepository;

@RestController
public class VoteController {

	@Autowired
	private VoteRepository voteRepository;
	
	// creo un voto ==> se le pasa id de la encuesta (Poll) desde la
	// URI como un @PathVariable y un objeto Vote como body del request
	@RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
	public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote vote) {

		// persisto el voto (Vote) que se paso en el body del request
		vote = voteRepository.save(vote);
		
		// creo los headers que van a ser enviados en la respuesta
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(ServletUriComponentsBuilder
									.fromCurrentRequest()			// prepara al builder copiando string del scheme, host, port, path y query de un HttpServletRequest
									.path("/{id}")					// agrega la ruta dada a la ruta existente de este constructor
									.buildAndExpand(vote.getId())	// crea una instancia de UriComponent  y reemplaza las variables de URI template con los valores de un array
									.toUri());						// devuelve un URI a partir de la instacia del UriComponent

		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	// obtengo los votos para un Poll cuyo id se pasa por pathvariable
	@RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.GET)
	public Iterable<Vote> getAllVotes(@PathVariable Long pollId) {
		return voteRepository.findByPoll(pollId);
	}
	
}

//============================================================================================
/*
 * Este caso funcionaria para un POST ==> http://localhost:8080/polls/1/votes
 * 
 * elijo la opcion con id 3
 * 
 * body del request:
 * -----------------
 * 3
 * 
 * */
//	@RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
//	public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Long optionId) {
//
//		Option option = optionRepository.findOne(optionId);
//
//		// persisto el voto (Vote) que se paso en el body del request
//		Vote vote = voteRepository.save(new Vote(option));
//
//		// creo los headers que van a ser enviados en la respuesta
//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vote.getId()).toUri());
//		
//		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
//	}
//============================================================================================
/*
 * Este caso funcionaria para un POST ==> http://localhost:8080/polls/1/votes
 * 
 * elijo la opcion con id 3
 * 
 * body del request:
 * -----------------
 * 	{
 * 		"searchByClassAttribute" : "value",
 * 		"valueClassAttribute" : "Gartenhaus - Birra - Sepi+Nico"
 * 	}		 
 * 
 * */
//@RequestMapping(value = "/polls/{pollId}/votes", method = RequestMethod.POST)
//public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Map<String, ?> selOpt) {
//	
//	Option option = null;
//	HttpHeaders responseHeaders = null;
//	String searchBy = (String) selOpt.get("searchByClassAttribute");
//	
//	if (searchBy.equals("id")) {
//		option = optionRepository.findOne(((Number) selOpt.get("valueClassAttribute")).longValue());
//	} else {
//		option = optionRepository.findByValue((String) selOpt.get("valueClassAttribute"));
//	}
//	
//	if (option != null) {
//		// persisto el voto (Vote) que se paso en el body del request
//		Vote vote = voteRepository.save(new Vote(option));
//
//		// creo los headers que van a ser enviados en la respuesta
//		responseHeaders = new HttpHeaders();
//		responseHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vote.getId()).toUri());
//	}
//	
//	return new ResponseEntity<>("ASASASASA", responseHeaders, HttpStatus.CREATED);
//}
