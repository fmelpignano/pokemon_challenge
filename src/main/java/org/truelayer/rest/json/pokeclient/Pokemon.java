package org.truelayer.rest.json.pokeclient;

import java.util.Arrays;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
* This class helps deserialize the JSON content received from Poke Api external service
* (see getPokemonByName method)
* Keep in mind that only the needful information have been mapped, others will be ignored.
* The purpose is in fact to be able to gather the information about this Pokemon species, 
* containing a url pointing to a more accurate species description. 
* 
* Example: https://pokeapi.co/api/v2/pokemon/charizard
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/


//To skip properties which have not been mapped explicitly.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {

	public Integer id;
	public String name;
	public Species species;
	
	public Pokemon() {
		
	}
	
	public static class Species {
		
		public String name;
		public String url;
		
		public Species() {
			
		}
		
		/* For Testing Purpose */
		public Species(String name, String speciesUrl) {
			this.name = name;
			this.url = speciesUrl;
		}
	}
	
	/**
	 * Gather the information about Species Id corresponding to Pokemon Species from the url field.
	 * If not found, return empry string. 
	 * @return Species Id taken from Species url if existing, empty string otherwise
	 */
	public String getSpeciesId() {
		String aId = "";
		
		if (this.species != null && this.species.url != null) {
			List<String> aTokens = Arrays.asList(this.species.url.split("/"));
	    	Integer aIndex = null;
	    	
	    	if (aTokens != null && !aTokens.isEmpty()) {
	    		aIndex = aTokens.size() - 1;
	    	}
	    	aId = aTokens.get(aIndex);
		}
		
		return aId;
	}
	
	/* For Testing Purpose */
	
	public Pokemon(String name, String speciesUrl) {
		this.id = 1;
		this.name = name;
		this.species = new Species(name, speciesUrl);
	}
}
