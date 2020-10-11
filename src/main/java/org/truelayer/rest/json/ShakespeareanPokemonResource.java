package org.truelayer.rest.json;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;


@Path("/pokemon")
@Produces(MediaType.APPLICATION_JSON)
public class ShakespeareanPokemonResource {

	private static final Logger LOGGER = Logger.getLogger(ShakespeareanPokemonResource.class);
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
    	LOGGER.debug("Receieved Request");
        return "hello";
    }
    
    @GET
    @Path("/{name}")
    public Response getDescriptionByName(@PathParam String name) {
    	
    	// Call Poke Api to get Pokemon by Name
    	
    	// Call Shakespeare Translator
    	
    	// Send back a reply
    	String aReply = "{ \"name\": \"" + name +"\", \"description\": \"" + name + "\"  }";
    	return Response.ok(aReply).build();
    }
}
