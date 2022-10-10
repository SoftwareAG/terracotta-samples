# Copyright (c) 2022 Software AG, Darmstadt, Germany and/or its licensors
#
# SPDX-License-Identifier: Apache-2.0
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

#!/bin/bash

WD=$(cd "$(dirname "$0")";pwd)

if [ -z "$TC_HOME" ]; then
  echo "Please initialize the environment variable TC_HOME to the location of your extracted Terracotta kit"
  exit 1
fi

if [ ! -d "${JAVA_HOME}" ]; then
  echo "$0: the JAVA_HOME environment variable is not defined correctly"
  exit 2
fi

JAVA="${JAVA_HOME}/bin/java"
JAVAC="${JAVA_HOME}/bin/javac"

uname | grep CYGWIN > /dev/null && TC_HOME=$(cygpath -w -p "${TC_HOME}")

# Add the client jars to the classpath
TC_CP="$WD/src"
while IFS= read -r line; do
  TC_CP="${TC_CP}:${line}"
done < <( find "${TC_HOME}/client" -type f -name '*.jar' )

# Add the logback configuration to the classpath
TC_CP=${TC_CP}:${TC_HOME}/client/logging/impl

echo "Compiling the sample class.."
"$JAVAC" -classpath "$TC_CP" "${WD}/src/ComplexDataTypeSample.java"

echo "Starting the Complex Data Type sample client.."
"$JAVA" -Xmx200m -classpath "$TC_CP" ComplexDataTypeSample
