package org.truelayer.rest.json;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

/**
* Class handling client calls to whatever path different than /pokemon 
* which is handled by ShakespereanPokemonResource class. 
* Purpose is to avoid providing default error message page showing more 
* information than needed (i.e. static resources, other endpoints, etc.)
* 
* Example: 
* Query:
* http://127.0.0.1:8180/
* Reply:
* {"statusCode":404,"message":"Not Found, available endpoints: /pokemon/{name}"}
* 
* Query:
* http://127.0.0.1:8180/test
* Reply:
* {"statusCode":404,"message":"Not Found, available endpoints: /pokemon/{name}"}
* 
* 
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class OtherResource {

	// Sure, not a dynamic reply, but it avoid exposing more than needed. 
	private static final Status DEFAULT_REPLY = new Status(404, "Not Found, available endpoints: /pokemon/{name}"); 

    @GET
    public Response rootPath() {
    	// Basic handler replying to queries against "/".
    	// Sending back generic error structure (no pokemon service targeted) with usage example.
    	return Response.status(404).entity(DEFAULT_REPLY).build();
    }
    
    @GET
    @Path("{var:.+}")
    public Response whateverPath(@PathParam String whatever) {
    	// Basic handler replying to queries against all patterns (apart /pokemon which has dedicated route).
    	// Sending back generic error structure (no pokemon service targeted) with usage example.
    	return Response.status(404).entity(DEFAULT_REPLY).build();
    }
}
