package org.truelayer.rest.json;

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

@Alternative()
@Priority(1)
@ApplicationScoped
@RestClient
public class MockPokeApiService implements PokeApiService {
	
	private static final Logger LOGGER = Logger.getLogger(ShakespeareanPokemonResource.class);
	
	/*
	 * Map used to emulate service replies.
	 */
	private final static String SPECIES_URL_SUCCESS = "https://pokeapi.co/api/v2/pokemon-species/6/";
	private final static String SPECIES_NOT_FOUND= "Not Found";
	private final static Map<String, String> pokemons = Stream.of(new String[][] {
		  { "charizard", SPECIES_URL_SUCCESS }, 
		  { "not_found", SPECIES_NOT_FOUND }, 
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	@Override
	public Pokemon getPokemonByName(String name) {
		LOGGER.infof("Mock getPokemonByName called with %s parameter", name);
		return new Pokemon(name, pokemons.get(name));
	}

	@Override
	public Species getSpeciesById(String speciesId) {
		LOGGER.infof("Mock getSpeciesById called with %s parameter", speciesId);
		return new Species();
	}

}
