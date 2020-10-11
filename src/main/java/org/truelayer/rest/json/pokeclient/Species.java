package org.truelayer.rest.json.pokeclient;

import java.util.List;

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
}
