About
-----
This sample illustrates the usage of the server script without any configuration files or explicit activation steps,
and the usage of multiple TC Store API with a multi-stripe cluster.

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

        ./activate-cluster.(bat|sh)
        
3. Connect a TC client to the cluster by running 
        
        ./start-client.(bat|sh)
