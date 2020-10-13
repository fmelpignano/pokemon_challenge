package org.truelayer.rest.json.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.jboss.logging.Logger;

/**
 * The purpose of this class is to intercept a Response provided by the client (status code >400),
 * avoid its automatic conversion and transform it into an exception instead.
 * 
 * The exception is used as container in this case, to carry in its message information about the
 * error code and error message to be sent back to the client in a custom format (see 
 * ShakespeareResource class). 
 * 
 * This exception will then be caught at server level which will send back an appropriate JSON reply.
 * 
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
 */

@Provider
public class CustomResponseExceptionMapper implements ResponseExceptionMapper<RuntimeException> {
	
	private static final Logger LOGGER = Logger.getLogger(CustomResponseExceptionMapper.class);
	
    @Override
    public RuntimeException toThrowable(Response response) {
    	String aStatusMessage = response.getStatus() + "|" + response.readEntity(String.class);
    	LOGGER.warn("CustomResponseExceptionMapper being called, message to be sent: " + aStatusMessage);
        return new RuntimeException(aStatusMessage);
    }
}
