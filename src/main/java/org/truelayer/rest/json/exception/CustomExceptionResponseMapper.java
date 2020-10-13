package org.truelayer.rest.json.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.truelayer.rest.json.ShakespeareanPokemon;

import javax.ws.rs.core.MediaType;

@Provider
public class CustomExceptionResponseMapper implements ExceptionMapper<RuntimeException> {

	private static final Logger LOGGER = Logger.getLogger(CustomExceptionResponseMapper.class);
	
	@Override
	public Response toResponse(RuntimeException e) {
		LOGGER.error("CustomExceptionResponseMapper being called, message being sent " + e.getMessage());
		return Response.ok(new ShakespeareanPokemon("", "", 500, e.getMessage()))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
