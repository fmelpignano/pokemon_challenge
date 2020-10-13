package org.truelayer.rest.json.shakespeareclient;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.truelayer.rest.json.exception.CustomResponseExceptionMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/translate")
@RegisterRestClient
@RegisterProvider(CustomResponseExceptionMapper.class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ShakespeareApiService {

    @POST
    @Path("/shakespeare")
    public ShakespeareTranslation getShakespeareTranslation(String text);
}