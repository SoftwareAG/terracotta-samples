/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

Demonstrates very basic operations with the Terracotta Store API with a distributed dataset.

See src/ClusteredStoreExample_Client1.java and src/ClusteredStoreExample_Client2.java

NOTE: to successfully run this sample, JAVA_HOME must point to a Java 1.8 JDK installation

To Run:
1. Start a server by running start-server.(bat|sh)
2. Configure the cluster by running configure-cluster.(bat|sh)
3. Connect the first TC DB client to it by running start-client1.(bat|sh). This client will create
a dataset, add a record, modify the record and prompt you to run the second client
4. Start the second client by running start-client2.(bat|sh). This client will apply further
modifications to the existing record in the dataset
5. Once the second client terminates, resume the first client by pressing Enter or Return key.
You will now see the changes done in Step 4 above at the first client
