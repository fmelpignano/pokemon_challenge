package org.truelayer.rest.json.shakespeareclient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//To skip properties which have not been mapped explicitly.
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShakespeareTranslation {

	public Success success;
	public Contents contents;
	
	public ShakespeareTranslation() {
		
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

