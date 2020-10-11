package org.truelayer.rest.json;

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
	
	private String name;
	private String description;
	
	/* Needed by Jackson */
	public ShakespeareanPokemon() {
		
	}
	
	public ShakespeareanPokemon(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}

}
