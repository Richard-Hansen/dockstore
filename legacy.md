# Legacy Documentation

The following material may be useful to understand the current build procedure and contains 
legacy documentation on several procedures and Dockstore's architecture. 

## For Dockstore Developers

### Swagger Java Client for Dockstore

This will no longer be necessary to do manually and is now done as part of the Maven build process.
Just remember to commit a new `dockstore-webservice/src/main/resources/swagger.yaml` when the dockstore API changes. 
Content is left here for reference purposes. 

Background:

 * Client library generated by the [swagger code generator](https://github.com/swagger-api/swagger-codegen)
 * Is generated based on the webservice's swagger UI spec
 * Used by the Dockstore CLI to make http requests to the dockstore
 * If you changed/added some endpoints that the CLI uses, you will need to regenerate the swagger client.
 
To regenerate the swagger client:

1. Have the dockstore webservice running
2. Pull the code from their repo and cd to the directory. We are using v2.1.4. Build using `mvn clean install`
3. Run `java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate -i http://localhost:8080/swagger.json -l java -o <output directory> --library jersey2`. The output directory is where you have dockstore/swagger-java-client/.
4. NOTE: Re-generating the swagger client will probably generate an incorrect pom file. Use git checkout on the pom file to undo the changes to it.

### Swagger Java Client for quay.io

This will no longer be necessary to do manually and is now done as part of the Maven build process.
Content is left here for reference purposes. 

Background:

 * Client library generated by the [swagger code generator](https://github.com/swagger-api/swagger-codegen)
 * Is generated based on the quay.io's swagger UI spec
 * Used by the Dockstore CLI to make http requests to quay.io
 * If CoreOS changes their API, you will need to regenerate the swagger client.
 
 To regenerate the swagger client:
 
1. Run `echo "{\"library\":\"jersey2\",\"invokerPackage\":\"io.swagger.quay.client\",\"modelPackage\":\"io.swagger.quay.client.model\",\"apiPackage\":\"io.swagger.quay.client.api\"}" > config.json`
2. Run `java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate -i https://quay.io/api/v1/discovery -l java -o <output directory> --library jersey2 --config config.json`. The output directory is where you have dockstore/swagger-java-client/.
3. NOTE: Rengenerating the swagger client will probably generate an incorrect pom file. Use git checkout on the pom file to undo the changes to it.

### CWL Avro documents

Background:
* The CWL specification is defined in something similar to but not entirely like Avro
* Use the schema salad project to convert to an avro-ish schema document
* Generate the Java classes for the schema
* We cannot use these classes directly since CWL documents are not json or avro binaries, use cwl-tool to convert to json and 
then gson to convert from json due to some incompatibilities between CWL avro and normal avro.  

To regenerate:

1. Get schema salad from the common-workflow-language organization and run `python -mschema_salad --print-avro ~/common-workflow-language/draft-3/cwl-avro.yml`
2. Get the avro tools jar and CWL avsc and call `java -jar avro-tools-1.7.7.jar compile schema cwl.avsc cwl`
3. Copy them to the appropriate directory in dockstore-client (you will need to refactor and insert package names)

Eventually, this will be moved out as a proper Maven dependency on https://github.com/common-workflow-language/cwlavro

### Demo Integration with Github.com

Setup your copy of Dockstore as a third-party application able to communicate with GitHub on behalf of a GitHub user. 

1. Setup a new OAuth application at [Register a new OAuth application](https://github.com/settings/applications/new)
2. Browse to [http://localhost:8080/integration.github.com](http://localhost:8080/integration.github.com)
3. Authorize via github.com using the provided link
4. Browse to [http://localhost:8080/github.repo](http://localhost:8080/github.repo) to list repos along with their collab.json (if they exist)

### Demo Integration with Quay.io

Setup your copy of Dockstore as a third-party application able to communicate with quay.io on behalf of a quay.io user. 

1. Setup an application as described in [Creating a new Application](http://docs.quay.io/api/)
2. Browse to [http://localhost:8080/integration.quay.io](http://localhost:8080/integration.quay.io)
3. Authorize via quay.io using the provided link
4. Browse to [http://localhost:8080/container](http://localhost:8080/container) to list repos that we have tokens for at quay.io

### Demo Integration with Bitbucket
 
1. Setup a new application as described in [Integrate another application through OAuth](https://confluence.atlassian.com/bitbucket/integrate-another-application-through-oauth-372605388.html). 
2. Use the dockstore-ui to authorize Bitbucket access for your current logged in user. Use the UI refresh controls to refresh your tools. 

### Webservice Demo

Demo the webservice and test communication with GitHub and quay.io

1. Build the project and run the webservice. NOTE: The webservice will grab and use the IP of the server running the API. For example, if running on a docker container with IP 172.17.0.24, the API will use this for the curl commands and request URLs.
2. Add your Github token. Follow the the steps above to get your Github token. This will create a user with the same username.
3. Add your Quay token. It will automatically be assigned to the user created with Github if the username is the same. If not, you need to user /token/assignEndUser to associate it with the user.
4. To load all your containers from Quay, use /container/refresh to load them in the database for viewing. This needs to be done automatically once the Quay token is set.
5. Now you can see and list your containers. Note that listing Github repos do not return anything because it does not return a valid json.

### Dockstore Java Client

Some background on the client:

* https://sdngeeks.wordpress.com/2014/08/01/swagger-example-with-java-spring-apache-cxf-jackson/
* http://developers-blog.helloreverb.com/enabling-oauth-with-swagger/