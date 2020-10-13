package org.truelayer.rest.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* This class will be used to sent back to the client the reply
* containing Pokemon name and description (translated in Shakespeare 
* language). 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

public class ShakespeareanPokemon {
	
	public String name;
	public String description;
	public Status status;
	
	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	/* Needed by Jackson */
	public ShakespeareanPokemon() {
		
	}
	
	public ShakespeareanPokemon(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = new Status();
		this.status = status;
	}
	
	public String toString() {
		try {
			return OBJECT_MAPPER.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
