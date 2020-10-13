package org.truelayer.rest.json.exception;

/**
 * This class represents an exception thrown whenever there is an issue while parsing the species url
 * in a species object within a Pokemon instance.
 * It means i.e. that Pokemon.species.url is empty or that there has been an issue during its
 * processing. 
 * This kind of exception could be thrown only in case of faulty replies from Poke Api getPokemonByName
 * service.
 * 
 * @author  Fabio Melpignano
 * @version 1.0
 * @since   11-OCT-2020
 */

public class UnableToParseSpeciesException extends Exception {

	private final static String message = "Unable to parse Species URL";
	
	public UnableToParseSpeciesException() {
		super(message);
	}
}
