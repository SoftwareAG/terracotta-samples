#!/bin/bash
# Copyright © 2018 Software AG, Darmstadt, Germany and/or its licensors
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
#

WD=$(cd "$(dirname "$0")";pwd)

if [ -z "$TC_HOME" ]; then
  echo "Please initialize the environment variable TC_HOME to the location of your extracted Terrracotta kit"
  exit 1
fi

if [ ! -f "${TC_HOME}/license.xml" ]; then
  echo "License file not found. Please name it 'license.xml' and put it under '${TC_HOME}'"
  exit 2
fi

"${TC_HOME}/tools/config-tool/bin/config-tool.sh" activate -f "${WD}/cluster.properties" -n tc-cluster -l "${TC_HOME}/license.xml"