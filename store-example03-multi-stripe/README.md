Demonstrates basic usage of the Terracotta Store API with a distributed storage using multiple stripes.

See src/Store3MultiStripe.java

To Run:
1. Start up two stripes (with one server each) by running 

        ./start-stripe1.(bat|sh) <terracotta-kit-path>
        ./start-stripe2.(bat|sh) <terracotta-kit-path>
        
2. Configure the cluster by running 

        ./configure-cluster.(bat|sh) <terracotta-kit-path>
        
3. Connect a TC DB client to the cluster by running 
        
        ./start-client.(bat|sh) <terracotta-kit-path>
