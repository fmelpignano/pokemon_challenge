package org.truelayer.rest.json.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.jboss.logging.Logger;

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
