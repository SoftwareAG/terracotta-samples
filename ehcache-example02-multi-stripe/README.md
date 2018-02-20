Demonstrates basic usage of the Ehcache API with a distributed cache using multiple stripes.

See src/EhCache3MultiStripe.java

NOTE: to successfully run this sample, JAVA_HOME must point to a Java 1.8 JDK installation

To Run:
1. Start up two stripes (with one server each) by running 

        ./start-stripe1.(bat|sh) <terracotta-kit-path>
        ./start-stripe2.(bat|sh) <terracotta-kit-path>
        
2. Configure the cluster by running 

        ./configure-cluster.(bat|sh) <terracotta-kit-path>
        
3. Connect an ehcache3 client to the cluster by running 

        ./start-client.(bat|sh) <terracotta-kit-path>
