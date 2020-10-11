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
* - Call PokeApi clien to retrieve information about Pokemon Species containing its description
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
    
    @GET
    @Path("/{name}")
    public Response getDescriptionByName(@PathParam String name) {
    	// Call Poke Api to get Pokemon by Name
    	Pokemon aPokemon = pokeApiServiceClient.getPokemonByName(name);
    	
    	// Call Shakespeare Translator
    	
    	// Send back a reply
    	String aReply = "{ \"name\": \"" + aPokemon.name +"\", \"description\": \"" + aPokemon.species.url + "\"  }";
    	return Response.ok(aReply).build();
    }
}
