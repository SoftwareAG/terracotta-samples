Demonstrates execution of textual queries based on the Textual Query Language extension.
===========================================================================================

Have a look at the code
-----------------------
The Java source code is located under [src](src/)

Important note
--------------
Make sure you read and applied the instructions provided [at the top level](../../../)

Prerequisites:
--------------

- Download the `Textual Query Language Extension APIs for Terracotta DB` from http://www.terracotta.org/downloads/terracotta/ and unzip it / untar it.

- Run the following commands in `bash shell` on Un*x, or in the Windows command prompt (look for .bat files)

Set the environment
-------------------

- For Un*x, run in a bash shell :

   ```export TC_TCTQL_HOME="/path/to/extracted/Terrracotta_TCTQL_kit"```

- On Windows, run in a command prompt :

   ```set TC_TCTQL_HOME="C:\path\to\extracted\Terrracotta_TCTQL_kit"```

How to Run
----------

0. Make sure you have set TC_HOME and copied the license to ```TC_HOME/tools/cluster-tool/conf/``` , if not, please read the instructions provided [at the top level](../../../)

1. Start a server by running

        ./start-server.(bat|sh)

2. Configure the cluster by running

        ./configure-cluster.(bat|sh)

3. Connect a store client equipped with the Textual Query Language extension to the server by running

        ./start-client.(bat|sh)
