#!/bin/bash

WD=$(cd "$(dirname "$0")";pwd)

if [ -z "$TC_HOME" ]; then
  echo "Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit"
  exit 1
fi

CLUSTER_TOOL_CONF="${TC_HOME}/tools/cluster-tool/conf"

if [ ! -f "${CLUSTER_TOOL_CONF}/license.xml" ]; then
  echo "License file not found. Please name it 'license.xml' and put it under '${CLUSTER_TOOL_CONF}'"
  exit 2
fi

"${TC_HOME}/tools/cluster-tool/bin/cluster-tool.sh" configure -n myCluster "${WD}/tc-config-stripe1.xml" "${WD}/tc-config-stripe2.xml"
