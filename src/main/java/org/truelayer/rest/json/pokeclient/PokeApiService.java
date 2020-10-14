package org.truelayer.rest.json.pokeclient;

import org.eclipse.microprofile.faulttolerance.Timeout;
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
* - pokemon/{name}, to retrieve the specieds Id of a Pokemon, its name being provided.
* - pokemon-species/{speciesId}, to retrieve species information by Species Id.
* 
* The response is represented by two classes in the model package, Pokemon and
* Species used to carry the information received in JSON format. 
* 
* A timeout has been put in place at client level, to avoid server being blocked 
* indefinitely on this client call.
* Beware that the TimeoutException will be arisen without the reply, meaning that the Response
* to Exception mapper will not be called. 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

@Path("/api/v2/")
@RegisterRestClient
@RegisterProvider(CustomResponseExceptionMapper.class)
@Timeout(2000)
@Produces(MediaType.APPLICATION_JSON)
public interface PokeApiService {

    @GET
    @Path("pokemon/{name}")
    
    Pokemon getPokemonByName(@PathParam String name);
    
    @GET
    @Path("pokemon-species/{speciesId}")
    Species getSpeciesById(@PathParam String speciesId);
}