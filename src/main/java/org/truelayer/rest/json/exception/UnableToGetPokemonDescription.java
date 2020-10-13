package org.truelayer.rest.json.exception;

/**
 * This class represents an exception thrown whenever there is no pokemon description available.
 * It means i.e. that flavor_text_entries is empty or that there has been an issue during its
 * processing. 
 * This kind of exception could be thrown only in case of faulty replies from Poke Api getSpeciesById
 * service.
 * 
 * @author  Fabio Melpignano
 * @version 1.0
 * @since   11-OCT-2020
 */

public class UnableToGetPokemonDescription extends Exception {

	private final static String message = "Unable to get Pokemon Description";
	
	public UnableToGetPokemonDescription() {
		super(message);
	}
}
