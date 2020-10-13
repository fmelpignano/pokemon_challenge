package org.truelayer.rest.json;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.truelayer.rest.json.pokeclient.Species;
import org.truelayer.rest.json.shakespeareclient.ShakespeareApiService;
import org.truelayer.rest.json.shakespeareclient.ShakespeareTranslation;
import org.truelayer.rest.json.pokeclient.PokeApiService;
import org.truelayer.rest.json.pokeclient.Pokemon;

/**
* Get a Shakesperean Pokemon description by name.
* This class represent two resources:
* pokemon (for debug purposes)
* pokemon/{name} to get a Pokemon description (by name) translated into Shakespeare language.
* 
* The processing when received a getDescriptionByName request is as following:
* - Call PokeApi client to retrieve information about Pokemon Species Id
* - Call PokeApi client to retrieve information about Pokemon Species containing its description
* - Call Shakespeare translator client, pass the description to it and get the translation
* - Send back the reply to che client
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/


@Path("/pokemon")
@Produces(MediaType.APPLICATION_JSON)
public class ShakespeareanPokemonResource {

	private static final Logger LOGGER = Logger.getLogger(ShakespeareanPokemonResource.class);
	
    @Inject
    @RestClient
    PokeApiService pokeApiServiceClient;
    
    @Inject
    @RestClient
    ShakespeareApiService shakespeareApiServiceClient;
    
    @GET
    @Path("/{name}")
    public Response getDescriptionByName(@PathParam String name) {
    	
    	try {
	    	// Call Poke Api to get Pokemon by Pokemon Name    	
	    	LOGGER.debug("Looking for Pokemon with name " +  name);
	    	Pokemon aPokemon = pokeApiServiceClient.getPokemonByName(name);
	    	LOGGER.debug("Received Pokemon species with name: " + aPokemon.species.name + ", with url: " + aPokemon.species.url );
	    	
	    	// Parsing the species url to get the species id
	    	String aSpeciesId = aPokemon.getSpeciesId();
	    	
	    	// Call Poke Api to get Species by Species Id
	    	LOGGER.debug("Looking for Species with Id: " +  aSpeciesId);
	    	Species aSpecies = pokeApiServiceClient.getSpeciesById(aSpeciesId);
	    	LOGGER.debug("Received pokemon species with name: " + aSpecies.name);
	    	
	    	// Parse the flavor_text_entries in Species to find longest English description.
	    	final String aDescriptionLanguge = "en";
	    	String aDescription = aSpecies.getLongestFlavorTextEntryByLanguage(aDescriptionLanguge);
	    	LOGGER.debug("Computed longest flavor_text in English is: " + aDescription);
	    	
	    	// Call Shakespeare service to get the translated description
	    	String aJsonDescription = "{ \"text\": \" " + aDescription  + "\" }";
	    	ShakespeareTranslation aTranslation = shakespeareApiServiceClient.getShakespeareTranslation(aJsonDescription);
	    	LOGGER.debug("Received flavor_text translation: " + aTranslation.contents.translated);
	    	
	    	// Send back a reply
	    	ShakespeareanPokemon aShakespeareanPokemon = new ShakespeareanPokemon(name, 
	    			aTranslation.contents.translated, 
	    			200,"Success");

	    	return Response.ok(aShakespeareanPokemon).build();
	    	
    	} catch(RuntimeException e) {
    		// Runtime exception thrown by ResponseExceptionMapper for the external client calls and caught here. 
    		LOGGER.debug("Received exception with message: " + e.getMessage());
	    	// Parse the exception message to get information about status code and message. 
    		String[] aTokens = e.getMessage().split("\\|");
    		 
    		if (aTokens != null && aTokens.length == 2) {
    			ShakespeareanPokemon aShakespeareanPokemon = new ShakespeareanPokemon("", "", Integer.valueOf(aTokens[0]), aTokens[1]);
    			// If parsing is successful it is most probably an error on client side. 
    			return Response.ok(aShakespeareanPokemon).build(); 
    		} else {
    			// Otherwise transform it into a server error by throwing an exception. 
    			// It will be caught by the ExceptionResponseMapper and translated into a server error reply.
    			throw new RuntimeException("Unable to parse: " + e.getMessage());
    		}
    	}
    }
}
