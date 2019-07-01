Demonstrates basic usage of the Terracotta Store API with a distributed storage using multiple stripes.
===========================================================================================

Have a look at the code
-----------------------
The Java source code is located under [src](src/)

Important note
--------------
Make sure you read and applied the instructions provided [at the top level](../../../)

How to Run
----------
1. Start up two stripes (with one server each) by running 

        ./start-stripe1.(bat|sh)
        ./start-stripe2.(bat|sh)
        
2. Configure the cluster by running 

        ./configure-cluster.(bat|sh)
        
3. Connect a TC client to the cluster by running 
        
        ./start-client.(bat|sh)
