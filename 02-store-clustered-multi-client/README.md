About
-----
This sample illustrates the usage of the server script without any configuration files or explicit activation steps,
and the connectivity of multiple TC Store clients with the cluster.

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
        
2. Connect the first TC client to it by running 

        ./start-client1.(bat|sh)
    
    This client will create a dataset, add a record, modify the record and prompt you to run the second client.
    
3. Start the second client by running 

        ./start-client2.(bat|sh)
    
    This client will apply further modifications to the existing record in the dataset
    
4. Once the second client terminates, resume the first client by pressing Enter or Return key.
You will now see the changes done in the step above at the first client.

Notes
-----

* No explicit activation step is required in this example as the server activates itself using the license
* The startup script uses several default values, which may not work for you. For example, the default server ports may
be busy on your system. In that case, you can change the defaults to use explicit values instead.
