#!/bin/bash

WD=$(cd "$(dirname "$0")" && pwd)
TC_HOME="$1"
if [ -z "$TC_HOME" ]; then
  echo "USAGE: $0 <TERRACOTTA_KIT_PATH>"
  exit 1
fi

TC_SERVER_HOME="$TC_HOME"/server

if [ ! -f "$TC_SERVER_HOME/bin/start-tc-server.sh" ]; then
  echo "Modify the script to set TC_SERVER_HOME"
  exit 2
fi

"${TC_SERVER_HOME}/bin/start-tc-server.sh" -f "${WD}/tc-config-stripe1.xml"
