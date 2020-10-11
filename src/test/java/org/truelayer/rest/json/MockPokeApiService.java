package org.truelayer.rest.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.truelayer.rest.json.pokeclient.PokeApiService;
import org.truelayer.rest.json.pokeclient.Pokemon;
import org.truelayer.rest.json.pokeclient.Species;

import com.fasterxml.jackson.databind.ObjectMapper;

@Alternative()
@Priority(1)
@ApplicationScoped
@RestClient
public class MockPokeApiService implements PokeApiService {
	
	private static final Logger LOGGER = Logger.getLogger(MockPokeApiService.class);
	
	/*
	 * Map used to emulate service replies.
	 */
	private final static String POKEMON_SUCCESS = "file/charizard_pokeapi_pokemon_reply.json";
	private final static String POKEMON_SPECIES_SUCCESS = "file/charizard_pokeapi_species_reply.json";
	private final static String NOT_FOUND= "file/not_found.json";
	
	/*
	 * Map to store link between pokemon name and json file (to ease addition of new tests).
	 */
	private final Map<String, String> pokemons = Stream.of(new String[][] {
		  { "charizard", POKEMON_SUCCESS }, 
		  { "not_found", NOT_FOUND }, 
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	/*
	 * Map to store link between species id and json file (to ease addition of new tests).
	 */
	private final Map<String, String> species = Stream.of(new String[][] {
		  { "6", POKEMON_SPECIES_SUCCESS }, 
		  { "-1", NOT_FOUND }, 
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	
	@Override
	public Pokemon getPokemonByName(String name) {
		LOGGER.infof("Mock getPokemonByName called with %s parameter", name);
		
		// Get the file corresponding to Pokemon json in resources.
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = pokemons.get(name);
		String aPath = classLoader.getResource(fileName).getFile();
		LOGGER.info(aPath);
		File file = new File(aPath);
		
		try {
			// Convert the json file to its Java class representation
			ObjectMapper objectMapper = new ObjectMapper();
			Pokemon aPokemon = objectMapper.readValue(file, Pokemon.class);
			LOGGER.info(objectMapper.writeValueAsString(aPokemon));
			return aPokemon;
		} catch (IOException e) {
			e.printStackTrace();
			return new Pokemon();
		}
	}

	@Override
	public Species getSpeciesById(String speciesId) {
		LOGGER.infof("Mock getSpeciesById called with %s parameter", speciesId);
		
		// Get the file corresponding to Pokemon json in resources.
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = species.get(speciesId);
		String aPath = classLoader.getResource(fileName).getFile();
		LOGGER.info(aPath);
		File file = new File(aPath);
		
		try {
			// Convert the json file to its Java class representation
			ObjectMapper objectMapper = new ObjectMapper();
			Species aSpecies = objectMapper.readValue(file, Species.class);
			LOGGER.info(objectMapper.writeValueAsString(aSpecies));
			return aSpecies;
		} catch (IOException e) {
			e.printStackTrace();
			return new Species();
		}
	}
	
}
