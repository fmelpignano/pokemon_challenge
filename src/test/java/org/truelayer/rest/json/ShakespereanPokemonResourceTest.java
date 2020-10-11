package org.truelayer.rest.json;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.core.MediaType;

@QuarkusTest
public class ShakespereanPokemonResourceTest {
    
    @Test
    public void testPokemonSuccess() {
    	String aPokemonName = "charizard";
    	String aExpectedSpecies = "https://pokeapi.co/api/v2/pokemon-species/6/";
    	String aExpectedReply = "{ \"name\": \"" + aPokemonName +"\", \"description\": \"" + aExpectedSpecies + "\"  }";
        given()
          .pathParam("name", aPokemonName)
          .when().get("pokemon/{name}")
          .then()
             .statusCode(200)
             .body(is(aExpectedReply))
             .and()
             .contentType(MediaType.APPLICATION_JSON);
    }
    
    @Test
    public void testPokemonNotFound() {
    	String aPokemonName = "not_found";
    	String aExpectedSpecies = "Not Found";
    	String aExpectedReply = "{ \"name\": \"" + aPokemonName +"\", \"description\": \"" + aExpectedSpecies + "\"  }";
        given()
          .pathParam("name", aPokemonName)
          .when().get("pokemon/{name}")
          .then()
             .statusCode(200)
             .body(is(aExpectedReply))
             .and()
             .contentType(MediaType.APPLICATION_JSON);
    }
    
    @Test
    public void testPokemonFailure() {
    	String aPath = "http://127.0.0.1:8081/resteasy/wrong";
    	String aExpectedMessage = "RESTEASY003210: Could not find resource for full path: " + aPath;
        given()
          .when().get(aPath)
          .then()
             .statusCode(404)
             .body(containsString(aExpectedMessage));
    }

}