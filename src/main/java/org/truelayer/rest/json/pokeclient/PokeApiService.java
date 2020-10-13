package org.truelayer.rest.json.pokeclient;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.truelayer.rest.json.exception.CustomResponseExceptionMapper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
* This interface is used to perform REST call to external PokeApi service.
* Among the different resources in Poke API, only two have been retained for 
* this challenge purpose: 
* - pokemon/{name}, to retrieve the information about a Pokemon from its name.
* - pokemon-species/{speciesId}, to retrieve species information by Species Id.
* 
* The response is represented by two classes in the model package, Pokemon and
* Species used to carry the information received in JSON format. 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

@Path("/api/v2/")
@RegisterRestClient
@RegisterProvider(CustomResponseExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
public interface PokeApiService {

    @GET
    @Path("pokemon/{name}")
    Pokemon getPokemonByName(@PathParam String name);
    
    @GET
    @Path("pokemon-species/{speciesId}")
    Species getSpeciesById(@PathParam String speciesId);
}