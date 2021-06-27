package com.apress.dto;

import java.util.Collection;

public class VoteResult {

	private int totalVotes;						// cantidad total de votos
	private Collection<OptionCount> results;	// cantidad de votos para cada opcion

	public int getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(int totalVotes) {
		this.totalVotes = totalVotes;
	}

	public Collection<OptionCount> getResults() {
		return results;
	}

	public void setResults(Collection<OptionCount> results) {
		this.results = results;
	}
}
