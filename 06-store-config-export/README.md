About
-----
This sample illustrates the usage of the server script without any configuration files or explicit activation steps,
and the demonstration configuration export from a TC Store client.

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
        
2. Connect the client to the server by running 

        ./start-client.(bat|sh)

Notes
-----

* No explicit activation step is required in this example as the server activates itself using the license
* The startup script uses several default values, which may not work for you. For example, the default server ports may
be busy on your system. In that case, you can change the defaults to use explicit values instead.
