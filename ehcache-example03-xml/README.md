Demonstrates basic usage of the Ehcache API with a distributed cache using XML configuration.
=============================================================================================

Have a look at the code
-----------------------
The Java source code is located under [src](src/)

Important note
--------------
Make sure you read and applied the instructions provided [at the top level](../../../)

How to Run
----------

1. Start a server by running 
        
        ./start-server.(bat|sh)
    
2. Configure the cluster by running 
        
        ./configure-cluster.(bat|sh)
        
3. Connect an ehcache3 client to the server by running 

        ./start-client.(bat|sh)
