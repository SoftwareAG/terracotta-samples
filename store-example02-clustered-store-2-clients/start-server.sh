#!/bin/bash

WD=$(cd "$(dirname "$0")" && pwd)

if [ -z "$TC_HOME" ]; then
  echo "Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit"
  exit 1
fi

TC_SERVER_HOME="$TC_HOME"/server

if [ ! -f "$TC_SERVER_HOME/bin/start-tc-server.sh" ]; then
  echo "Modify the script to set TC_SERVER_HOME"
  exit 2
fi

"${TC_SERVER_HOME}/bin/start-tc-server.sh" -f "${WD}/tc-config.xml"
