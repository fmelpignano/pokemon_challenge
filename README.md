# pokemon_challenge project

For information about how to run the service, please refer to "How to run it ?" section.

The following sections will also focus on high level flow and technical choices performed. 

## Why Quarkus ? 

Because it is a framework on which I would like to gather some knowledge for different reasons: 
- optimized for the cloud environment
- native mode (decrease start time and memory footprint)
- plugin mechanism to "easily" enable features (fault tolerance, observability, etc.)
Last but not least it has been an opportunity to practice with a CDI framework. 

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## What about pokemon_challenge design ? 
The service has been implemented using Quarkus RestEasy plugin to implement JAX-RS services while the clients follow the Microprofile REST client API.

The project is divided into different packages: 
* org.truelayer.rest.json

It contains everything related to the resource, reachable through the path /pokemon/{name}: the resource and the classes representing the service replies.
Other paths could be matched and they are managed by a dedicated handler in charge of sending back a pertinent reply, in JSON format, with the reply status and a related message.

* org.truelayer.rest.json.exception

It contains the ExceptionMappers (from Response to Exception and viceversa) and the Exception classes.

* org.truelayer.rest.json.pokeclient

It contains everything related to the Poke API client: the REST client interface and classes to map JSON replies.

* org.truelayer.rest.json.shakespeareclient

It contains everything related to the Shakespeare Translator client: the REST client interface and classes to map JSON quey/reply.

To follow the service flow, I suggest to start from ShakespereanPokemonResource class; this class:
* Call GET method on Poke API service to gather Pokemon information (specifically the species Id)
* Call GET method on Poke API service to gather information about the species using its Id (retrieve during previous step)
* Call POST method on Shakespeare Translator API to translate the species description to Shakespeare language
* Send back the successful reply to the client with a status message

The classes within the project have been documented and they will not be described in detail in this document.

## Highlights
Among the different features offered by Quarkus and used in this project

* REST client interface

Easy to build client interface but difficult to customize them especially when it comes to exception handling.
* Logging

Easy to plug logging mechanism but difficult to configure effectively. 
* Fault tolerance

Only Timeout has been used, easy to put in place for Poke API and Shakespeare Translator API. 
* Testing

Easy to put in place testing with rest-assured plugin and JUnit 5, enabling to check for response status and content as well as standard unit testing for classes; mockup solution are as well available to emulate external calls.

However, especially for the latter, it could be useful to use a circuit breaker, since the service is throttling incoming requests (5 requests/hour).
This way when the threshold has been reached subsequent calls can be avoided, sparing resources and avoiding pushing the deadline in the future.

## To be improved
Several axes to improve available: 
* Observability 

Plugin exists for monitoring (Micrometer, no Prometheus out of the box) and traceability; not put in place at the moment but definitely a MUST. 

* Thread model

With all the abstraction put in place, plus the different flavor offered, is quite difficult to understand quickly what is going on under the hood. For instance, for the REST clients, a singleton client instance is proposed which ensures thread safety, prevent socket exahustion but affects the performaces; an approach with a thread pool could have helped here.

* Security

No authentication or authorization put in place during this experience; plugins exist for such purpose but they have not been tested in this scope. 

## How to run it ?

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `truelayer_pokemon-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it is not an Uber-jar_ as the dependencies are copied into the `target/lib` directory.
If you want to build an Uber-jar_, just add the `--uber-jar` option to the command line:
```shell script
./mvnw package -PuberJar
```

The application is now runnable using `java -jar target/truelayer_pokemon-1.0.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/truelayer_pokemon-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# Further Readings

## RESTEasy JAX-RS
Guide: https://quarkus.io/guides/rest-json

## Quarkus Cookbook
https://learning.oreilly.com/library/view/quarkus-cookbook/
