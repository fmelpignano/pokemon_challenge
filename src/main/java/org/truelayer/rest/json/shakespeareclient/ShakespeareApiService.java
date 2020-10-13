package org.truelayer.rest.json.shakespeareclient;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.truelayer.rest.json.exception.CustomResponseExceptionMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
* This interface is used to perform REST call to external Shakespeare Translation service.
* The service /translate is exposed, reachable through POST method, expecting JSON as input
* and providing JSON as output.
* The input functionally represents the text to be translated (pokemon description in our case)
* and the output represents the corresponding translated text.  
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

@Path("/translate")
@RegisterRestClient
@RegisterProvider(CustomResponseExceptionMapper.class)
@Timeout(2000)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ShakespeareApiService {

    @POST
    @Path("/shakespeare")
    public ShakespeareTranslationReply getShakespeareTranslation(ShakespeareTranslationQuery text);
}