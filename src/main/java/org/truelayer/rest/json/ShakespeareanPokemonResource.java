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
import org.truelayer.rest.json.shakespeareclient.ShakespeareTranslationQuery;
import org.truelayer.rest.json.shakespeareclient.ShakespeareTranslationReply;
import org.truelayer.rest.json.pokeclient.PokeApiService;
import org.truelayer.rest.json.pokeclient.Pokemon;

/**
* Get a Shakesperean Pokemon description by name.
* This class enable the access to pokemon/{name} route. 
* It enables to receive a Pokemon description (by name) translated into Shakespeare language
* and sent back in JSON format.
* 
* The processing when received a getDescriptionByName request is as following:
* - Call PokeApi client to retrieve information about Pokemon Species Id
* - Call PokeApi client to retrieve information about Pokemon Species containing its description
* - Call Shakespeare translator client, pass the description to it and get the translation
* - Send back the reply to che client
* 
* The reply not only contains information about the Pokemon (name, translated description), but also
* about the status of the request (status code and message).
* 
* Example of such usage as following. 
* Query:
* http://127.0.0.1:8180/pokemon/charmander
* Reply:
* {"name":"charmander","description":"The flame ... fiercely.","status":{"statusCode":200,"message":"Success"}}
* 
* This class handles as well the routing to other paths which are not exposed:
* Query:
* http://127.0.0.1:8180/pokemon/
* Reply:
* {"name":"","description":"","status":{"statusCode":404,"message":"Chek URL, please provide Pokemon Name to /pokemon service"}}
* 
* Query:
* http://127.0.0.1:8180/pokemon/test/double
* {"name":"","description":"","status":{"statusCode":404,"message":"Chek URL, please provide Pokemon Name to /pokemon service"}}
* 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/


@Path("pokemon")
@Produces(MediaType.APPLICATION_JSON)
public class ShakespeareanPokemonResource {

	private static final Logger LOGGER = Logger.getLogger(ShakespeareanPokemonResource.class);
	// Language to for Pokemon description.
	private static final String LANGUAGE = "en";
	// Used to split exception message in status + message if possible.
	private static final String EXCEPTION_SEPARATOR = "\\|";
	// Default reply status in case of success or wrong path being targeted. 
	private static final Status POKEMON_DEFAULT_REPLY = new Status(404, "Check URL, please provide Pokemon Name to /pokemon service");
	private static final Status POKEMON_SUCCESS_REPLY = new Status(200, "Success");
	
	// Injection for the client enabling the call to Poke API service.
    @Inject
    @RestClient
    PokeApiService pokeApiServiceClient;
    
    // Injection for the client enabling the call to Shakespeare Translator service.
    @Inject
    @RestClient
    ShakespeareApiService shakespeareApiServiceClient;
    
    @GET
    public Response noPokemonNamePath() {
    	// Basic handler replying to queries against all /pokemon pattern (apart /pokemon/{name} which has dedicated route).
    	// Sending back ShakespereanPokemon structure with usage example since /pokemon service has been targeted.
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("","", POKEMON_DEFAULT_REPLY);
    	return Response.ok(aPokemon).build();
    }
    
    @GET
    @Path("{name}")
    public Response getDescriptionByName(@PathParam String name) {
    	// Handler to process getDescriptionByName calls.
    	try {
	    	
    		// 1. Call Poke Api to get Pokemon by Pokemon Name    	
	    	LOGGER.debug("Looking for Pokemon with name " +  name);
	    	Pokemon aPokemon = pokeApiServiceClient.getPokemonByName(name);
	    	LOGGER.debug("Received Pokemon species with name: " + aPokemon.species.name + ", with url: " + aPokemon.species.url );
	    	
	    	// 2. Parsing the species url to get the species id
	    	String aSpeciesId = aPokemon.getSpeciesId();
	    	
	    	// 3. Call Poke Api to get Species by Species Id
	    	LOGGER.debug("Looking for Species with Id: " +  aSpeciesId);
	    	Species aSpecies = pokeApiServiceClient.getSpeciesById(aSpeciesId);
	    	LOGGER.debug("Received pokemon species with name: " + aSpecies.name);
	    	
	    	// 4. Parse the flavor_text_entries in Species to find longest English description.
	    	// Remove as well special character (\n, \f, etc.) since not supported by Shakespeare translator.
	    	String aDescription = aSpecies.getLongestFlavorTextEntryByLanguage(LANGUAGE);
	    	LOGGER.debug("Computed longest flavor_text in English is: " + aDescription);
	    	
	    	// 5. Call Shakespeare service to get the translated description
	    	ShakespeareTranslationQuery aTranslationQuery = new ShakespeareTranslationQuery(aDescription);
	    	ShakespeareTranslationReply aTranslation = shakespeareApiServiceClient.getShakespeareTranslation(aTranslationQuery);
	    	LOGGER.debug("Received flavor_text translation: " + aTranslation.contents.translated);
	    	
	    	// 6. Send back a reply
	    	ShakespeareanPokemon aShakespeareanPokemon = new ShakespeareanPokemon(name, 
	    			aTranslation.contents.translated,
	    			POKEMON_SUCCESS_REPLY);

	    	return Response.ok(aShakespeareanPokemon).build();
	    	
    	} catch(Exception e) {
    		// Runtime exception thrown by ResponseExceptionMapper for the external client calls and caught here. 
    		LOGGER.info("Received exception with message: " + e.getMessage());
	    	// Parse the exception message to get information about status code and message. 
    		String[] aTokens = e.getMessage().split(EXCEPTION_SEPARATOR);
    		 
    		if (aTokens != null && aTokens.length == 2) {
    			Status aStatus = new Status(Integer.valueOf(aTokens[0]), aTokens[1]);
    			ShakespeareanPokemon aShakespeareanPokemon = new ShakespeareanPokemon("", "", aStatus);
    			// If parsing is successful it is most probably an error on client side. 
    			return Response.ok(aShakespeareanPokemon).build(); 
    		} else {
    			// Otherwise transform it into a server error by throwing an exception. 
    			// It will be caught by the ExceptionResponseMapper and translated into a server error reply.
    			throw new RuntimeException(e.getMessage());
    		}
    	}
    }
    
    @GET
    @Path("{name}/{var:.+}")
    public Response noResourceAfterPokemonPath() {
    	// Basic handler replying to queries against all /pokemon pattern (apart /pokemon/{name} which has dedicated route).
    	// Sending back ShakespereanPokemon structure with usage example since /pokemon service has been targeted.
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("","", POKEMON_DEFAULT_REPLY);
    	return Response.ok(aPokemon).build();
    }
}
