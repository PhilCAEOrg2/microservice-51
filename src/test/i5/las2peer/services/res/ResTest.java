package i5.las2peer.services.res;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import i5.las2peer.p2p.LocalNode;
import i5.las2peer.p2p.LocalNodeManager;
import i5.las2peer.api.p2p.ServiceNameVersion;
import i5.las2peer.security.ServiceAgentImpl;
import i5.las2peer.security.UserAgentImpl;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.connectors.webConnector.WebConnector;
import i5.las2peer.connectors.webConnector.client.ClientResponse;
import i5.las2peer.connectors.webConnector.client.MiniClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * Service3 - Test Class
 *
 * This class provides a basic testing framework for the microservice Service3. It was
 * generated by the CAE (Community Application Framework).
 */
public class ResTest {

  private static final String HTTP_ADDRESS = "http://127.0.0.1";
  private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

  private static LocalNode node;
  private static WebConnector connector;
  private static ByteArrayOutputStream logStream;

  private static UserAgentImpl testAgent;
  private static final String testPass = "adamspass";

  // version does not matter in tests
  private static final ServiceNameVersion testTemplateService = new ServiceNameVersion(Res.class.getCanonicalName(),"0.1");

  private static final String mainPath = "res";


  /**
   * Called before the tests start.
   * Sets up the node and initializes connector and users that can be used throughout the tests.
   * @throws Exception
   */
  @BeforeClass
  public static void startServer() throws Exception {

    // start node
    node = new LocalNodeManager().newNode();
    testAgent = MockAgentFactory.getAdam();
    testAgent.unlock(testPass); // agent must be unlocked in order to be stored
    node.storeAgent(testAgent);
    node.launch();

    ServiceAgentImpl testService = ServiceAgentImpl.createServiceAgent(testTemplateService, "a pass");
    testService.unlock("a pass");

    node.registerReceiver(testService);

    // start connector
    logStream = new ByteArrayOutputStream();

    connector = new WebConnector(true, HTTP_PORT, false, 1000);
    connector.setLogStream(new PrintStream(logStream));
    connector.start(node);
    Thread.sleep(1000); // wait a second for the connector to become ready
    testAgent = MockAgentFactory.getAdam();
    
     
  }


  /**
   * 
   * Second Test for the FirstCase method.
   * 
   */
  @Test
  public void testFirstCase2() {
    MiniClient c = new MiniClient();
    c.setAddressPort(HTTP_ADDRESS, HTTP_PORT);
    
        try {
      ClientResponse result = c.sendRequest("GET", mainPath + "/one", "");
    Assert.assertTrue(result.getHttpCode() != 404);
Assert.assertTrue(result.getHttpCode() == 200);

      System.out.println("Result of 'test$HTTP_Method_Name$': " + result.getResponse().trim());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception: " + e);
    }
    

    try {
      ClientResponse result = c.sendRequest("GET", mainPath + "/two", "");
    
      System.out.println("Result of 'test$HTTP_Method_Name$': " + result.getResponse().trim());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Exception: " + e);
    }
    


    
  }
  




  /**
   * Called after the tests have finished. Shuts down the server and prints out the connector log
   * file for reference.
   * @throws Exception
   */
  @AfterClass
  public static void shutDownServer() throws Exception {
	
	 

    connector.stop();
    node.shutDown();

    connector = null;
    node = null;

    System.out.println("Connector-Log:");
    System.out.println("--------------");

    System.out.println(logStream.toString());
  }

}
