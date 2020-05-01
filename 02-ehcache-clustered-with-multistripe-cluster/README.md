About
-----
This sample illustrates the usage of the server script with a configuration properties file, an explicit cluster activation step,
and the connectivity of an ehcache clustered client with the cluster.

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
        
2. Activate the cluster by running 

        ./activate-cluster.(bat|sh)
        
3. Connect an ehcache3 client to the cluster by running 

        ./start-client.(bat|sh)

Notes
-----

* The configuration properties file uses several values, which may not work for you. For example, the server ports may
be busy on your system. In that case, you can change the values to suit your needs.
