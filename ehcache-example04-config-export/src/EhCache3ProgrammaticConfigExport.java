import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.management.registry.DefaultManagementRegistryConfiguration;
import org.ehcache.xml.XmlConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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

public class EhCache3ProgrammaticConfigExport {

  private static final String DEFAULT_TSA_PORT = "9410";
  private static final String TERRACOTTA_URI_ENV = "TERRACOTTA_SERVER_URL";
  private static final String CACHE_MANAGER_ALIAS = "clustered-cache-manager-04";
  private static final String CACHE_ALIAS = "clustered-cache-04";
  private static final String SERVER_RESOURCE = "primary-server-resource";
  private static final String SHARED_RESOURCE_POOL = "resource-pool-a";
  private static final String DEFAULT_SERVER_URI_STR = "terracotta://localhost:" + DEFAULT_TSA_PORT;
  private static final String SERVER_URI_STR = System.getenv(TERRACOTTA_URI_ENV) == null ? DEFAULT_SERVER_URI_STR : System.getenv(TERRACOTTA_URI_ENV);

  public static void main(String[] args) throws Exception {
    try (CacheManager cacheManager = createCacheManager()) {
      exportConfiguration(cacheManager);
    }
  }

  private static CacheManager createCacheManager() {
    final URI uri = URI.create(SERVER_URI_STR + "/" + CACHE_MANAGER_ALIAS);
    final CacheManagerBuilder<PersistentCacheManager> clusteredCacheManagerBuilder = CacheManagerBuilder
        .newCacheManagerBuilder()
        .with(ClusteringServiceConfigurationBuilder
            .cluster(uri)
            .autoCreate()
            .defaultServerResource(SERVER_RESOURCE)
            .resourcePool(SHARED_RESOURCE_POOL, 10, MemoryUnit.MB))
        .using(new DefaultManagementRegistryConfiguration()
            .addTags("my-client-tag", "another-client-tag")
            .setCacheManagerAlias(CACHE_MANAGER_ALIAS))
        .withCache(CACHE_ALIAS, CacheConfigurationBuilder.newCacheConfigurationBuilder(
            Long.class,
            String.class,
            ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(1000, EntryUnit.ENTRIES)
                .offheap(1, MemoryUnit.MB)
                .with(ClusteredResourcePoolBuilder.clusteredShared(SHARED_RESOURCE_POOL))));

    System.out.println("Building the clustered CacheManager, connecting to : " + uri + ", using " + SERVER_RESOURCE);

    return clusteredCacheManagerBuilder.build(true);
  }

  private static void exportConfiguration(CacheManager cacheManager) throws Exception {
    Configuration runtimeConfiguration = cacheManager.getRuntimeConfiguration();
    XmlConfiguration xmlConfiguration = new XmlConfiguration(runtimeConfiguration);
    prettyPrintXML(xmlConfiguration.toString());
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
