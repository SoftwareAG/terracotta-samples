#!/bin/bash
# Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
# Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.

WD=$(cd "$(dirname "$0")";pwd)
TC_HOME="$1"
if [ -z "$TC_HOME" ]; then
  echo "USAGE: $0 <TERRACOTTA_KIT_PATH>"
  exit 1
fi

if [ ! -d "${JAVA_HOME}" ]; then
  echo "$0: the JAVA_HOME environment variable is not defined correctly"
  exit 2
fi

JAVA="${JAVA_HOME}/bin/java"
JAVAC="${JAVA_HOME}/bin/javac"

uname | grep CYGWIN > /dev/null && TC_HOME=$(cygpath -w -p "${TC_HOME}")

TC_CP=.
# Add the client jars in the classpath
find "${TC_HOME}/client/" -type f -name "*.jar" > file
while IFS= read -r line; do
	TC_CP=${TC_CP}:${line}
done < file
rm file

# Add the logback configuration to the classpath
TC_CP=${TC_CP}:${TC_HOME}/client/logging/impl

echo "Compiling the sample class.."
"$JAVAC" -classpath "$TC_CP" "${WD}/src/ClusteredStoreExample_Client2.java"

echo "Starting the TC DB sample client, it's going to try to connect to your local server.."
"$JAVA" -Xmx200m -classpath "$TC_CP:$WD/src/" ClusteredStoreExample_Client2
