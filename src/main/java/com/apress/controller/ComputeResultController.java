package com.apress.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apress.domain.Vote;
import com.apress.dto.OptionCount;
import com.apress.dto.VoteResult;
import com.apress.repository.VoteRepository;

@RestController
public class ComputeResultController {

	@Autowired
	private VoteRepository voteRepository;

	@RequestMapping(value = "/computeresult", method = RequestMethod.GET)
	public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
		
		// va tener la cantidad de votos totales y la cantidad de votos por opcion
		VoteResult voteResult = new VoteResult();
		
		// obtengo todos los votos
		Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);

		// algoritmo para contar los votos por opcion
		int totalVotes = 0;
		
		// creo map temporal que va a ir guardando el id como key 
		// y como valor va ir acumulando los votos que tuvo esa opcion
		Map<Long, OptionCount> tempMap = new HashMap<Long, OptionCount>();
		
		// itero todos los votos
		for (Vote v : allVotes) {
			
			// contador de votos
			totalVotes++;
			
			// obtengo el OptionCount para el id de opcion dado por el voto
			OptionCount optionCount = tempMap.get(v.getOption().getId());
			
			// si ya esta agregado va se != null, si no fue agregado va ser null
			if (optionCount == null) {
				optionCount = new OptionCount();
				optionCount.setOptionId(v.getOption().getId());		// obtengo el id de la opcion y lo paso al obj OptionCount
				tempMap.put(v.getOption().getId(), optionCount);	// guardo el id de la opcion y su contador
			}
			// si no es null, no se crea intancia pero se suma uno al contador
			optionCount.setCount(optionCount.getCount() + 1);
		}		
		// paso el contador de votos totales
		voteResult.setTotalVotes(totalVotes);
		
		// seteo los resultados pasando del HashMap a un tipo Collection con .values()
		voteResult.setResults(tempMap.values());
		
		// devuelvo el objeto VoteResult al body del response
		return new ResponseEntity<>(voteResult, HttpStatus.OK);
	}

}