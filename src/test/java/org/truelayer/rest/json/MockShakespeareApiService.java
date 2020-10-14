package org.truelayer.rest.json;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.truelayer.rest.json.exception.CustomResponseExceptionMapper;
import org.truelayer.rest.json.shakespeareclient.ShakespeareApiService;
import org.truelayer.rest.json.shakespeareclient.ShakespeareTranslationQuery;
import org.truelayer.rest.json.shakespeareclient.ShakespeareTranslationReply;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
* Mock implemenation of Shakespeare Translator client for unit testing. 
* The translation map helps to create diffeent scenarii depending on the input text. 
* To provide an index in the map an hash is computed starting from text to be translated in input,
* to associated it with the right translation.  
* 
* @author  Fabio Melpignano
* @version 1.0
* @since   11-OCT-2020
*/


@Alternative()
@Priority(1)
@ApplicationScoped
@RegisterProvider(CustomResponseExceptionMapper.class)
@RestClient
@Timeout(1000)
public class MockShakespeareApiService implements ShakespeareApiService {
	
	private static final Logger LOGGER = Logger.getLogger(MockShakespeareApiService.class);
	
	/*
	 * Map used to emulate service replies.
	 */
	private final static String TRANSLATION_SUCCESS = "file/charizard_shakespearetranslator_reply.json";
	private final static String NOT_FOUND= "file/not_found.json";
	
	/*
	 * Map to store link between pokemon description and its translation in json file.
	 * The map is indexed with a hash of the original text provided in input to the
	 * translation service. 
	 */
	private final Map<Long, String> translations = Stream.of(new Object[][] {
		  { 2399199406L, TRANSLATION_SUCCESS }, 
		  { 1L, NOT_FOUND }, 
		}).collect(Collectors.toMap(data -> (Long) data[0], data -> (String) data[1]));
	

	@Override
	public ShakespeareTranslationReply getShakespeareTranslation(ShakespeareTranslationQuery text) {
		// Compute checksum of incoming text and use it as index for the
		// map of expected replies.
		CRC32 aChecksum = new CRC32();
		aChecksum.update(text.toString().getBytes());
		Long aHash = aChecksum.getValue();
		
		LOGGER.infof("Mock getShakespeareTranslation called with %s parameter", text);
		LOGGER.infof("Mock getShakespeareTranslation called with %s hash", aHash);
		
		// Get the file corresponding to Translation json in resources.
		ClassLoader classLoader = getClass().getClassLoader();
		String fileName = translations.get(aHash);
		String aPath = classLoader.getResource(fileName).getFile();
		LOGGER.info(aPath);
		File file = new File(aPath);
		
		try {
			// Convert the json file to its Java class representation
			ObjectMapper objectMapper = new ObjectMapper();
			ShakespeareTranslationReply aTranslation = objectMapper.readValue(file, ShakespeareTranslationReply.class);
			LOGGER.info(objectMapper.writeValueAsString(aTranslation));
			return aTranslation;
		} catch (IOException e) {
			return new ShakespeareTranslationReply();
		}
	}
	
}
