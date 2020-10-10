package org.truelayer.rest.json;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

@Path("/resteasy/hello")
public class ShakespeareanPokemonResource {

	private static final Logger LOGGER = Logger.getLogger(ShakespeareanPokemonResource.class);
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String hello() {
    	LOGGER.debug("Receieved Request");
        return "hello";
    }
}
