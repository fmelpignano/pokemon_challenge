package org.truelayer.rest.json.shakespeareclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
* This class represents the Shakespeare Translator service API reply.
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

//To skip properties which have not been mapped explicitly.
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShakespeareTranslationReply {

	public Success success;
	public Contents contents;
	
	public ShakespeareTranslationReply() {
		
	}
	
	public static class Success {
		
		public Integer total;
		
		public Success() {
			
		}
	}
	
	public static class Contents {
		
		public String translated;
		public String text;
		public String translation;
		
		public Contents() {
			
		}
	}
}

