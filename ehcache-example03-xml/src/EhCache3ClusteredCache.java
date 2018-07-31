import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.util.Random;
import java.util.UUID;

public class EhCache3ClusteredCache {

  private static final String CACHE_ALIAS = "clustered-cache-03";

  public static void main(String[] args) throws Exception {
    CacheManager cacheManager = createCacheManager();
    startCacheOperations(cacheManager);
    cacheManager.close();
  }

  private static CacheManager createCacheManager() {
    XmlConfiguration xmlConfiguration =
        new XmlConfiguration(EhCache3ClusteredCache.class.getResource("/clustered.xml"));
    CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfiguration);
    cacheManager.init();
    return cacheManager;
  }

  private static void startCacheOperations(CacheManager cacheManager) throws InterruptedException {
    Cache<Long, String> cache = cacheManager.getCache(CACHE_ALIAS, Long.class, String.class);
    Random random = new Random();

    for (int i = 0; i < 10; i++) {
      Long key = random.nextLong();
      String value = UUID.randomUUID().toString();

      System.out.println("" + (i + 1) + ". Putting key : " + key + " with value : " + value);
      cache.put(key, value);
      Thread.sleep(1000);
    }
  }
}
