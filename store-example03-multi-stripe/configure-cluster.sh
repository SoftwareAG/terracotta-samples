#!/bin/bash
# Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
# Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.

WD=$(cd "$(dirname "$0")";pwd)
TC_HOME="$1"
if [ -z "$TC_HOME" ]; then
  echo "USAGE: $0 <TERRACOTTA_KIT_PATH>"
  exit 1
fi
CLUSTER_TOOL_CONF="${TC_HOME}/tools/cluster-tool/conf"

if [ ! -f "${CLUSTER_TOOL_CONF}/license.xml" ]; then
  echo "License file not found. Please name it 'license.xml' and put it under '${CLUSTER_TOOL_CONF}'"
  exit 2
fi

"${TC_HOME}/tools/cluster-tool/bin/cluster-tool.sh" configure -n myCluster "${WD}/tc-config-stripe1.xml" "${WD}/tc-config-stripe2.xml"
