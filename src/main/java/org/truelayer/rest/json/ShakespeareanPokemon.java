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
	public Result status;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/* Needed by Jackson */
	public ShakespeareanPokemon() {
		
	}
	
	// 
	public static class Result {
		public Integer statusCode;
		public String message;
		
		public Result() {
			
		}
	}
	
	public ShakespeareanPokemon(String name, String description, Integer statusCode, String message) {
		this.name = name;
		this.description = description;
		this.status = new Result();
		this.status.statusCode = statusCode;
		this.status.message = message;
	}
	
	public String toString() {
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
