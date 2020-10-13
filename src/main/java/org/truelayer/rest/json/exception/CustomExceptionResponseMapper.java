package org.truelayer.rest.json.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.truelayer.rest.json.ShakespeareanPokemon;
import org.truelayer.rest.json.Status;

import javax.ws.rs.core.MediaType;

/**
 * The purpose of this class is to intercept a RuntimeException and translate it into a Response to be sent
 * back to the client in JSON format.
 * At this stage we assume that the exception are thrown by the server, as such error code is 500. 
 * 
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
 */

@Provider
public class CustomExceptionResponseMapper implements ExceptionMapper<RuntimeException> {

	private static final Logger LOGGER = Logger.getLogger(CustomExceptionResponseMapper.class);
	
	@Override
	public Response toResponse(RuntimeException e) {
		LOGGER.error("CustomExceptionResponseMapper being called, message being sent " + e.getMessage());
		return Response.ok(new ShakespeareanPokemon("", "", new Status(500, e.getMessage())))
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
