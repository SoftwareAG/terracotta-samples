#!/bin/bash
# Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
# Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.

WD=$(cd "$(dirname "$0")" && pwd)
TC_SERVER_HOME=$(dirname "$(dirname "${WD}")")/server

if [ ! -f "$TC_SERVER_HOME/bin/start-tc-server.sh" ]; then
  echo "Modify the script to set TC_SERVER_HOME"
  exit 2
fi

"${TC_SERVER_HOME}/bin/start-tc-server.sh" -f "${WD}/tc-config-stripe1.xml"
