/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.management.registry.DefaultManagementRegistryConfiguration;
import org.terracotta.connection.ConnectionException;

import java.net.URI;
import java.util.Random;
import java.util.UUID;

public class EhCache3ClusteredCache {

  private static final String CACHE_MANAGER_ALIAS = "clustered-cache-manager";
  private static final String CACHE_ALIAS = "clustered-cache";
  private static final String DEFAULT_TSA_PORT = "9410";
  private static final String SERVER_RESOURCE = "primary-server-resource";
  private static final String SHARED_RESOURCE_POOL = "resource-pool-a";

  public static void main(String[] args) throws Exception {
    CacheManager cacheManager = createCacheManager();
    startCacheOperations(cacheManager);
  }

  private static CacheManager createCacheManager() throws ConnectionException {
    String serverPort = System.getProperty("tsa-port", DEFAULT_TSA_PORT);
    final URI uri = URI.create("terracotta://localhost:" + serverPort + "/" + CACHE_MANAGER_ALIAS);
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

    cacheManager.close();
  }
}
