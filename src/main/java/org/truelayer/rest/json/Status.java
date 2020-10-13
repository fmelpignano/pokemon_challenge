package org.truelayer.rest.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* This class will be used to represent a HTTP Status. 
* It will be embedded in ShakespereanPokemon class to represent success/failure
* of calls as well as used to send back an error reply (wrong path accessed). 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

public class Status {
	public Integer statusCode;
	public String message;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/* Needed by Jackson */
	public Status() {
		
	}
	
	/* Needed by Jackson */
	public Status(Integer statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public String toString() {
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
