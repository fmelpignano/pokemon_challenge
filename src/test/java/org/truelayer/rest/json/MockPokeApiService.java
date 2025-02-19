package org.truelayer.rest.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.truelayer.rest.json.exception.CustomResponseExceptionMapper;
import org.truelayer.rest.json.pokeclient.PokeApiService;
import org.truelayer.rest.json.pokeclient.Pokemon;
import org.truelayer.rest.json.pokeclient.Species;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
* Mock implemenation of PokeApi client for unit testing. 
* The pokemon and species maps help to tailor the reply depending on the input parameters, 
* to test different scenarii. 
* 
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

@Alternative()
@Priority(1)
@ApplicationScoped
@RegisterProvider(CustomResponseExceptionMapper.class)
@RestClient
@Timeout(1000)
public class MockPokeApiService implements PokeApiService {
	
	private static final Logger LOGGER = Logger.getLogger(MockPokeApiService.class);
	
	/*
	 * Map used to emulate service replies.
	 */
	private final static String POKEMON_SUCCESS = "file/charizard_pokeapi_pokemon_reply.json";
	private final static String POKEMON_SPECIES_SUCCESS = "file/charizard_pokeapi_species_reply.json";
	private final static String POKEMON_NO_SPECIES = "file/no_species_pokeapi_pokemon_reply.json";
	private final static String POKEMON_WRONG_SPECIES = "file/wrong_species_pokeapi_pokemon_reply.json";
	private final static String SPECIES_WRONG_DESCRIPTION = "file/no_pokemon_description_pokeapi_species_reply.json";
	
	private final static String NOT_FOUND= "not_existing";
	
	/*
	 * Map to store link between pokemon name and json file (to ease addition of new tests).
	 */
	private final Map<String, String> pokemons = Stream.of(new String[][] {
		  { "charizard", POKEMON_SUCCESS }, 
		  { "not_found", NOT_FOUND },
		  { "no_species", POKEMON_NO_SPECIES},
		  { "wrong_species", POKEMON_WRONG_SPECIES},
		  { "timeout", NOT_FOUND}
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	/*
	 * Map to store link between species id and json file (to ease addition of new tests).
	 */
	private final Map<String, String> species = Stream.of(new String[][] {
		  { "6", POKEMON_SPECIES_SUCCESS }, 
		  { "-1", NOT_FOUND },
		  {"123", SPECIES_WRONG_DESCRIPTION}
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	
	@Override
	public Pokemon getPokemonByName(String name) {
		LOGGER.infof("Mock getPokemonByName called with %s parameter", name);
		
		if (name.equals("timeout")) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				LOGGER.info("Thread interrupted");
			}
		}
		
		try {
			// Get the file corresponding to Pokemon json in resources.
			ClassLoader classLoader = getClass().getClassLoader();
			String fileName = pokemons.get(name);
			String aPath = classLoader.getResource(fileName).getFile();
			File file = new File(aPath);
			// Convert the json file to its Java class representation
			ObjectMapper objectMapper = new ObjectMapper();
			Pokemon aPokemon = objectMapper.readValue(file, Pokemon.class);
			return aPokemon;
		} catch (Exception e) {
			// if there has been an issue parsing the file (i.e. in case of not_found case)
			// then catch and throw a runtime exception to emulate real client behavior. 
			LOGGER.info("Throwing runtime exception");
			throw new RuntimeException("404|Not Found");
		}
	}

	@Override
	public Species getSpeciesById(String speciesId) {
		LOGGER.infof("Mock getSpeciesById called with %s parameter", speciesId);
		
		// Get the file corresponding to Pokemon json in resources.
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = species.get(speciesId);
		String aPath = classLoader.getResource(fileName).getFile();
		File file = new File(aPath);
		
		try {
			// Convert the json file to its Java class representation
			ObjectMapper objectMapper = new ObjectMapper();
			Species aSpecies = objectMapper.readValue(file, Species.class);
			return aSpecies;
		} catch (IOException e) {
			// if there has been an issue parsing the file (i.e. in case of not_found case)
			// then catch and throw a runtime exception to emulate real client behavior. 
			LOGGER.info("Throwing runtime exception");
			throw new RuntimeException("404|Not Found");
		}
	}
	
}
