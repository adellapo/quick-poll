package com.apress.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Poll {

	@Id
	@GeneratedValue
	@Column(name = "POLL_ID")
	private Long id;

	@Column(name = "POLL_NAME")
	private String name;

	@Column(name = "QUESTION")
	@NotEmpty								// valida que el bean no tenga una question vacia
	private String question;

	@OneToMany(cascade = CascadeType.ALL)	// una instancia Poll va tener cero o muchas instancias Option
	@JoinColumn(name = "POLL_ID")			// la columna con la que se hace el join
	@OrderBy(value = "value")				// para que se ordene por value ==> Option.value
	@Size(min = 2, max = 6)					// validacion del bean con un minimo de 2 y maximo 6 opciones
	private Set<Option> options;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<Option> getOptions() {
		return options;
	}

	public void setOptions(Set<Option> options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getId() + ", " + getQuestion() + ", " + getOptions();
	}

}
