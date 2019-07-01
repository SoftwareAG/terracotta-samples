Demonstrates very basic operations with the Terracotta Store API with a distributed dataset.
===========================================================================================

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
        
3. Connect the first TC client to it by running 

        ./start-client1.(bat|sh)
    
    This client will create a dataset, add a record, modify the record and prompt you to run the second client.
    
4. Start the second client by running 

        ./start-client2.(bat|sh)
    
    This client will apply further modifications to the existing record in the dataset
    
5. Once the second client terminates, resume the first client by pressing Enter or Return key.
You will now see the changes done in Step 4 above at the first client
