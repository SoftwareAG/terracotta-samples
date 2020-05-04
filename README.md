Terracotta 10.7 samples
=======================
This repository contains code samples for Terracotta version 10.7 clients (Ehcache 3 and TCStore).

**_NOTE:_**
For samples corresponding to Terracotta releases 10.5 and earlier, please check `release/10.5` branch.

What this document will help you accomplish:
--------------------------------------------

- Start a Terracotta cluster (one stripe or two stripes)

- Run sample Ehcache 3 and Terracotta Store (TCStore) based applications, that connect to a Terracotta cluster.

Prerequisites:
--------------

- Download the `Standalone installation package for Terracotta 10.x` from http://www.terracotta.org/downloads and unzip/untar it.

- Have a Terracotta license file ready (you can get a trial license file from : http://www.terracotta.org/downloads)

- Run the following commands in `bash shell` on Un*x, or in the Windows command prompt (look for .bat files)

Set the environment
-------------------

- For Un*x, run in a bash shell:

   ```export JAVA_HOME="/location/to/valid/JDK8OrLater"```
   ```export TC_HOME="/path/to/extracted/TerracottaKit"```


- On Windows, run in a command prompt:

   ```set JAVA_HOME="C:\....\java\jdk1.x.y_zz"```
   ```set TC_HOME="C:\path\to\extracted\TerracottaKit"```

Make the license available to the server and config-tool
-------------------------------------------------------

The config tool is responsible for activating your cluster (how many stripes in your cluster, propagating the license, etc.)
Placing the license in the `TC_HOME` directory helps the sample server and config-tool scripts pick it up.

For example:

   ```cp Terracotta107.xml TC_HOME/license.xml```

__Make sure you name the license file `license.xml`, and put it under ```TC_HOME```__

Run the sample applications
---------------------------

Clone this repo, and then you just need to cd into each directory to discover the sample and how start it.

___________________
For more information you can Ask a Question in the [TECHcommunity Forums](http://tech.forums.softwareag.com).

You can find additional information in the [Software AG TECHcommunity](http://techcommunity.softwareag.com).
