/*
 * Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

Demonstrates basic usage of the Terracotta Store API with a distributed storage using multiple stripes.

See src/Store3MultiStripe.java

To Run:
1. Start up two stripes (with one server each) by running start-stripe1.(bat|sh) and start-stripe2.(bat|sh)
2. Configure the cluster by running configure-cluster.(bat|sh)
3. Connect a TC DB client to the cluster by running start-client.(bat|sh)
