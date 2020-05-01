import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.terracottatech.store.Type;
import com.terracottatech.store.configuration.DatasetConfiguration;
import com.terracottatech.store.manager.DatasetManager;
import com.terracottatech.store.manager.XmlConfiguration;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class StoreProgrammaticConfigExport {
  private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
  private static final String DEFAULT_TSA_PORT = "9410";
  private static final String STORE_NAME = "mySampleStore06";
  private static final String SERVER_RESOURCE = "main";
  private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
  private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);

  public static void main(String[] args) throws Exception {
    try (DatasetManager datasetManager = DatasetManager.clustered(URI.create(SERVER_URI_STR)).build()) {
      // clean-up (delete) sample dataset from any previous run of this sample program
      datasetManager.destroyDataset(STORE_NAME);

      // create a dataset
      DatasetConfiguration offheapResource = datasetManager.datasetConfiguration().offheap(SERVER_RESOURCE).build();
      datasetManager.newDataset(STORE_NAME, Type.LONG, offheapResource);
      String xmlConfiguration = XmlConfiguration.toXml(datasetManager.getDatasetManagerConfiguration());
      prettyPrintXML(xmlConfiguration);
    }
  }

  private static void prettyPrintXML(String xmlConfiguration) throws Exception {
    System.out.println("Exported programmatic configuration to XML:");

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
        .newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document document = docBuilder.parse(new InputSource(new StringReader(xmlConfiguration)));

    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    Writer prettyPrint = new StringWriter();
    transformer.transform(new DOMSource(document), new StreamResult(prettyPrint));
    System.out.print(prettyPrint.toString());
  }
}
