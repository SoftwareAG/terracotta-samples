/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

Demonstrates basic usage of the Ehcache API with a distributed cache.

See src/EhCache3ClusteredCache.java

NOTE: to successfully run this sample, JAVA_HOME must point to a Java 1.8 JDK installation

To Run:
1. Start a server by running 
        
        ./start-server.(bat|sh) <terracotta-kit-path>
    
2. Configure the cluster by running 
        
        ./configure-cluster.(bat|sh) <terracotta-kit-path>
        
3. Connect an ehcache3 client to the server by running 

        ./start-client.(bat|sh) <terracotta-kit-path>
