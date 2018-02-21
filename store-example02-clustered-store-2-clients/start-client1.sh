#!/bin/bash

WD=$(cd "$(dirname "$0")";pwd)

if [ -z "$TC_HOME" ]; then
  echo "Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit"
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
"$JAVAC" -classpath "$TC_CP" "${WD}/src/ClusteredStoreExample_Client1.java"

echo "Starting the TC DB sample client, it's going to try to connect to your local server.."
"$JAVA" -Xmx200m -classpath "$TC_CP:$WD/src/" ClusteredStoreExample_Client1
