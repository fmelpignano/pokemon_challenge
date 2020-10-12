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

import com.fasterxml.jackson.databind.ObjectMapper;

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
	    	LOGGER.info("Looking for Pokemon with name " +  name);
	    	Pokemon aPokemon = pokeApiServiceClient.getPokemonByName(name);
	    	LOGGER.info("Received Pokemon species with name: " + aPokemon.species.name + ", with url: " + aPokemon.species.url );
	    	
	    	// Parsing the species url to get the species id
	    	String aSpeciesId = aPokemon.getSpeciesId();
	    	
	    	// Call Poke Api to get Species by Species Id
	    	LOGGER.debug("Looking for Species with Id: " +  aSpeciesId);
	    	Species aSpecies = pokeApiServiceClient.getSpeciesById(aSpeciesId);
	    	LOGGER.info("Received pokemon species with name: " + aSpecies.name);
	    	
	    	// Parse the flavor_text_entries in Species to find longest English description.
	    	final String aDescriptionLanguge = "en";
	    	String aDescription = aSpecies.getLongestFlavorTextEntryByLanguage(aDescriptionLanguge);
	    	LOGGER.info("Computed longest flavor_text in English is: " + aDescription);
	    	
	    	// Call Shakespeare service to get the translated description
	    	String aJsonDescription = "{ \"text\": \" " + aDescription  + "\" }";
	    	ShakespeareTranslation aTranslation = shakespeareApiServiceClient.getShakespeareTranslation(aJsonDescription);
	    	ObjectMapper aObjectMapper = new ObjectMapper();
	    	String aStr = aObjectMapper.writeValueAsString(aTranslation);
	    	LOGGER.info("Received flavor_text translation: " + aStr);
	    	
	    	// Send back a reply
	    	ShakespeareanPokemon aShakespeareanPokemon = new ShakespeareanPokemon(name, aTranslation.contents.translated);
	    	return Response.ok(aShakespeareanPokemon).build();
	    	
    	} catch(Exception e) {
    		// If there is any exception send out a corresponding response with empty ShakespeareanPokemon.
    		LOGGER.info(e.getMessage());
    		return Response.status(500, e.getMessage()).entity(new ShakespeareanPokemon()).build();
    	}
    }
}
