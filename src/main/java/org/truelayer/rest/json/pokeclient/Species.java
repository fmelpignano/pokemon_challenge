package org.truelayer.rest.json.pokeclient;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.truelayer.rest.json.exception.UnableToGetPokemonDescription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
* This class helps deserialize the JSON content received from Poke Api external service
* (see getSpeciesById method)
* Keep in mind that only the needful information have been mapped, others will be ignored.
* The purpose is in fact to be able to gather the information about the flavor text entries, 
* containing a description for Pokemon species in several languages. 
* 
* Example: https://pokeapi.co/api/v2/pokemon-species/6/
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

//To skip properties which have not been mapped explicitly.
@JsonIgnoreProperties(ignoreUnknown = true)
public class Species {
	
	public String name;
	public Integer id;
	public List<FlavorTextEntries> flavor_text_entries; 
	
	public Species() {
		
	}
	
	public static class FlavorTextEntries {
		
		public String flavor_text;
		public Language language;
		public Version version;
		
		public FlavorTextEntries() {
			
		}
	}

	public static class Language {
		public String name; 
		public String url;
		
		public Language() {
			
		}
	}
	
	public static class Version  {
		public String name; 
		public String url;
		
		public Version() {
			
		}
	}
	
	/**
	 * 1. Get a stream of flavor_text_entries
	 * 2. Remove the ones not matching the language in input (or English - "en") by default
	 * 3. Get the flavor text value
	 * 4. Get the longest String among the flavor texts.
	 * @param language The language to filter on, English ("en") by default.
	 * @return The longest string among the flavor text entries matching language parameter.
	 * @throws UnableToGetPokemonDescription if no description can be found 
	 */
	public String getLongestFlavorTextEntryByLanguage(String language) throws Exception {
		Optional<String> aOptionalResult = this.flavor_text_entries
				.stream()
				.filter(x -> { return x.language.name.equals(language); })
				.map( x -> { return x.flavor_text; })
				.max(Comparator.comparingInt(String::length));
		
		if (aOptionalResult.isPresent()) {
			// It seems as Shakespeare Translation service do not like these characters.
		    String aResult = aOptionalResult.get().replaceAll("\\n|\\f|\\t|\\r", " ");
			return aResult;
		} else {
			throw new UnableToGetPokemonDescription();
		}
	}
}
