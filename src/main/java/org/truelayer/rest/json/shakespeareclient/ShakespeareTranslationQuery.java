package org.truelayer.rest.json.shakespeareclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* This class represents the query to be sent to Shakespeare Translator service
* in the scope of a POST request.
* It is only a JSON wrapper around the Pokemon description. 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

//To skip properties which have not been mapped explicitly.
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShakespeareTranslationQuery {

	public String text;
	
	private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public ShakespeareTranslationQuery() {
		
	}
	
	public ShakespeareTranslationQuery(String text) {
		this.text = text;
	}
	
	public String toString() {
		try {
			return OBJECT_MAPPER.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}
}

