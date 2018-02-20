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
