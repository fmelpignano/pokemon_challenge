package org.truelayer.rest.json;

import io.quarkus.test.junit.QuarkusTest;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;

import javax.ws.rs.core.MediaType;

/**
* This class is the one containing unit tests for the ShakespereanPokemonResource class.
* Each test contains a description of the scenario it should cover. 
*
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/

@QuarkusTest
public class ShakespereanPokemonResourceTest {
    
	private static final Logger LOGGER = Logger.getLogger(ShakespereanPokemonResourceTest.class);
	
	private final static String REPLY_SUCCESS = "file/charizard_truelayerservice_reply.json";
	private final static Status SERVICE_NOT_FOUND = new Status(404, "Not Found, available endpoints: /pokemon/{name}");
	
    @Test
    public void testPokemonSuccess() {
    	// Purpose of the test is to call the service using an existing pokemon name.
    	// External calls have been mocked for testing purpose.
		testPokemon("charizard", REPLY_SUCCESS);
    }
    
    @Test
    public void testPokemonNotFound() {
    	// Purpose of the test is to call the service using a not existing pokemon name.
    	// A reply should be provided with an error. 
    	// External calls have been mocked for testing purpose.
    	Status aStatus = new Status(404, "Not Found");
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("", "", aStatus);
		testPokemon("pokemon/{name}", "not_found", aPokemon);
    }
    
    @Test
    public void testNoSpeciesFoundForPokemon() {
    	// Purpose of the test is to check that an error within a reply is reported
    	// if there is no species information provided by PokeApi getPokemonByName.
    	Status aStatus = new Status(500, "Unable to parse Species URL");
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("", "", aStatus);
		testPokemon("/pokemon/{name}", "no_species", aPokemon);
    }
    
    @Test
    public void testWrongSpeciesForPokemon() {
    	// Purpose of the test is to check that an error within a reply is 
    	// reported if there is no description being provided by PokeApi getSpeciesById.
    	Status aStatus = new Status(500, "Unable to get Pokemon Description");
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("", "", aStatus);
		testPokemon("/pokemon/{name}", "wrong_species", aPokemon);
    }
    
    @Test
    public void testTimeout() {
    	// Purpose of the test is to check that a timeout is actually triggered on client calls.
    	Status aStatus = new Status(500, "Timeout[org.truelayer.rest.json.MockPokeApiService#getPokemonByName] timed out");
    	ShakespeareanPokemon aPokemon = new ShakespeareanPokemon("", "", aStatus);
		testPokemon("/pokemon/{name}", "timeout", aPokemon);
    }
    
    @Test
    public void testRootResourceNotFound() {
    	// Purpose of the test is to check that an error is reported if 
    	// a root path is being targeted.
    	// Reply is different than a successful call to /pokemon/{name}
    	final String aPath = "/";
        given()
          .when().get(aPath)
          .then()
             .statusCode(404)
             .and()
             .body(is(SERVICE_NOT_FOUND.toString()));
    }
    
    @Test
    public void testOtherResourceNotFound() {
    	// Purpose of the test is to check that an error is reported if 
    	// a path different than /pokemon/{resource} is being targeted.
    	// Reply is different than a successful call to /pokemon/{name}
    	final String aPath = "/resteasy/wrong";
        given()
          .when().get(aPath)
          .then()
             .statusCode(404)
             .and()
             .body(is(SERVICE_NOT_FOUND.toString()));
    }
    
    @Test
    public void testOtherResourceNotFoundAfterPokemon() {
    	// Purpose of the test is to check that an error is reported if 
    	// a path like /pokemon/{resource}/* is being targeted.
    	// In this case a proper reply is provided since assuming that the client
    	// targeted the right service.
    	Status aStatus = new Status(404, "Check URL, please provide Pokemon Name to /pokemon service");
    	ShakespeareanPokemon aExpectedReply = new ShakespeareanPokemon("", "", aStatus);
    	final String aPath = "/pokemon/wrong/test";
        given()
          .when().get(aPath)
          .then()
             .statusCode(200)
             .and()
             .body(is(aExpectedReply.toString()));
    }

    /**
     * Util method to assert call result (reply content, status code) with expected parameters.
     * It perform such operation by reading result from a file. 
     * @param pokemonName i.e. charizard or other
     * @param expectedReplyPath
     */
    public void testPokemon(String pokemonName, String expectedReplyPath) {
		
    	ClassLoader classLoader = getClass().getClassLoader();
		final String aFullPath = classLoader.getResource(expectedReplyPath).getFile();
		LOGGER.info(aFullPath);
		File file = new File(aFullPath);
		
		// Get the expected reply from file.
		Status aStatus = new Status(200, "");
		ShakespeareanPokemon aShakespereanPokemon = new ShakespeareanPokemon("", "", aStatus);
		try {
			// Convert the json file to its Java class representation; it could be overkill but it 
			// helps testing that serialize/deserialize is working.
			ObjectMapper objectMapper = new ObjectMapper();
			aShakespereanPokemon = objectMapper.readValue(file, ShakespeareanPokemon.class);
		} catch (Exception e) {
			LOGGER.info("Expected Reply was: " + aShakespereanPokemon.toString());
			LOGGER.error(e.getStackTrace());
		}
    	
		testPokemon("pokemon/{name}", pokemonName, aShakespereanPokemon);
    }
    
    /**
     * Util method to assert call result (reply content, status code) with expected parameters.
     * 
     * @param pokemonName i.e. charizard or other
     * @param ShakespeareanPokemon instance to be sent back to the client
     */
    public void testPokemon(String url, String pokemonName, ShakespeareanPokemon aPokemon) {
		    	
		// Compare received and expected reply.
    	given()
          .pathParam("name", pokemonName)
          .when().get(url)
          .then()
             .statusCode(200)
             .body(is(aPokemon.toString()))
             .and()
             .contentType(MediaType.APPLICATION_JSON);
    }

}