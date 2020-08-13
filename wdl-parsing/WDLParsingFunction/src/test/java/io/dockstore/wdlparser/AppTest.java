package io.dockstore.wdlparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AppTest {
  @Test
  public void successfulResponse() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    WDLParserRequest request = new WDLParserRequest();
    request.setBranch("1.0.4");
    request.setUri("https://github.com/briandoconnor/dockstore-tool-md5sum.git");
    request.setDescriptorRelativePathInGit("Dockstore.wdl");
    App app = new App();
    APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
    requestEvent.setBody(objectMapper.writeValueAsString(request));
    APIGatewayProxyResponseEvent result = app.handleRequest(requestEvent, null);
    assertEquals(HttpURLConnection.HTTP_OK, result.getStatusCode().intValue());
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    String content = result.getBody();
    assertNotNull(content);
    WDLParserResponse response = objectMapper.readValue(content, WDLParserResponse.class);
    assertTrue(response.isValid());
    assertTrue(response.getClonedRepositoryAbsolutePath().contains("/tmp"));
    assertEquals(0, response.getSecondaryFilePaths().size());
    System.out.println(response.getClonedRepositoryAbsolutePath());
  }

  @Test
  public void successfulResponseOfComplexWorkflow() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    WDLParserRequest request = new WDLParserRequest();
    request.setBranch("dockstore-test");
    request.setUri("https://github.com/dockstore-testing/gatk-sv-clinical.git");
    request.setDescriptorRelativePathInGit("GATKSVPipelineClinical.wdl");
    App app = new App();
    APIGatewayProxyRequestEvent requestEvent = new APIGatewayProxyRequestEvent();
    requestEvent.setBody(objectMapper.writeValueAsString(request));
    APIGatewayProxyResponseEvent result = app.handleRequest(requestEvent, null);
    assertEquals(HttpURLConnection.HTTP_OK, result.getStatusCode().intValue());
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    String content = result.getBody();
    assertNotNull(content);
    WDLParserResponse response = objectMapper.readValue(content, WDLParserResponse.class);
    assertTrue(response.isValid());
    assertTrue(response.getClonedRepositoryAbsolutePath().contains("/tmp"));
    assertFalse(
        "Main descriptor isn't a secondary file path",
        response.getSecondaryFilePaths().contains("GATKSVPipelineClinical.wdl"));
    assertEquals(76, response.getSecondaryFilePaths().size());
    System.out.println(response.getClonedRepositoryAbsolutePath());
  }

  @Ignore("Too dangerous test to run, also flakey")
  public void testRecursiveWDL() throws IOException {
    File file = new File("src/test/resources/recursive.wdl");
    String path = file.getAbsolutePath();
    WDLParserResponse response = App.getResponse(path);
    Assert.assertFalse("A workflow that has recursive HTTP imports is invalid", response.isValid());
  }
}