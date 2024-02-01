## Overview

This sample integrates together:

* Spring Boot 3
* Ehcache 3
* AOP
* Metrics
* Prometheus
* Actuator
* Vaadin

It accesses an online service for currency conversion and simulate some additional latencies.

It also creates 2 caches (heap only), with listeners and TTL of 60 seconds.

## How to run this sample:

1. Requires: **Java 17**
2. You must first get an API key at https://freecurrencyapi.com/
2. Build: `./mvnw package`
3. Run: `./target/spring-boot-ehcache-0.0.1-SNAPSHOT.jar --app.freecurrencyapi.key=<your-api-key>`
4. Open your browser at http://localhost:8080/
5. Execute some currency conversion
6. Look at the logs below
7. Look at Spring Boot actuator endpoints at http://localhost:8080/actuator to see the metrics and caches and verify
   that the caching works.
