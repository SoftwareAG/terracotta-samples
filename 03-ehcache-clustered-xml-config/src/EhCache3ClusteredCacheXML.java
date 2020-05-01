/*
 * Copyright (c) 2020 Software AG, Darmstadt, Germany and/or its licensors
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import java.util.Random;
import java.util.UUID;

public class EhCache3ClusteredCacheXML {

  private static final String CACHE_ALIAS = "clustered-cache-03";

  public static void main(String[] args) throws Exception {
    try (CacheManager cacheManager = createCacheManager()) {
      startCacheOperations(cacheManager);
    }
  }

  private static CacheManager createCacheManager() {
    XmlConfiguration xmlConfiguration = new XmlConfiguration(EhCache3ClusteredCacheXML.class.getResource("/clustered.xml"));
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
    }
  }
}
