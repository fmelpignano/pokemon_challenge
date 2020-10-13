package org.truelayer.rest.json;

import io.quarkus.test.junit.QuarkusTest;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

@QuarkusTest
public class ShakespereanPokemonResourceTest {
    
	private static final Logger LOGGER = Logger.getLogger(ShakespereanPokemonResourceTest.class);
	
	private final static String REPLY_SUCCESS = "file/charizard_truelayerservice_reply.json";
	private final static String REPLY_NOT_FOUND= "file/not_found_truelayerservice_reply.json";
	
    @Test
    public void testPokemonSuccess() {
    	// Purpose of the test is to call the service using an existing pokemon name.
    	// External calls have been mocked for testing purpose.
    	String aPokemonName = "charizard";
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = REPLY_SUCCESS;
		String aPath = classLoader.getResource(fileName).getFile();
		LOGGER.info(aPath);
		testPokemon(aPokemonName, aPath, 200);
    }
    
    @Test
    public void testPokemonNotFound() {
    	// Purpose of the test is to call the service using an existing pokemon name.
    	// External calls have been mocked for testing purpose.
    	String aPokemonName = "not_found";
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = REPLY_NOT_FOUND;
		String aPath = classLoader.getResource(fileName).getFile();
		LOGGER.info(aPath);
		testPokemon(aPokemonName, aPath, 200);
    }
    
    @Test
    public void testPokemonFailure() {
    	String aPath = "http://127.0.0.1:8081/resteasy/wrong";
    	String aExpectedMessage = "RESTEASY003210: Could not find resource for full path: " + aPath;
        
    	given()
          .when().get(aPath)
          .then()
             .statusCode(200)
             .body(containsString(aExpectedMessage));
        
    	// Updating path to an existing server
    	aPath = "http://127.0.0.1:8180/resteasy/wrong";
        given()
          .when().get(aPath)
          .then()
             .statusCode(404);
    }
    
    
    /**
     * Util method to assert call result (reply content, status code) with expected parameters.
     * 
     * @param pokemonName i.e. charizard or other
     * @param expectedReplyPath
     * @param expectedStatusCode
     */
    public void testPokemon(String pokemonName, String expectedReplyPath, Integer expectedStatusCode) {

		File file = new File(expectedReplyPath);
		
		// Get the expected reply from file.
		ShakespeareanPokemon aShakespereanPokemon = new ShakespeareanPokemon("", "", 200, "");
		try {
			// Convert the json file to its Java class representation; it could be overkill but it 
			// helps testing that serialize/deserialize is working.
			ObjectMapper objectMapper = new ObjectMapper();
			aShakespereanPokemon = objectMapper.readValue(file, ShakespeareanPokemon.class);
			LOGGER.info("EXPECTED: " + aShakespereanPokemon.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		// Compare received and expected reply.
    	given()
          .pathParam("name", pokemonName)
          .when().get("pokemon/{name}")
          .then()
             .statusCode(expectedStatusCode)
             .body(is(aShakespereanPokemon.toString()))
             .and()
             .contentType(MediaType.APPLICATION_JSON);
    }

}