@REM Copyright (c) 2020 Software AG, Darmstadt, Germany and/or its licensors
@REM
@REM SPDX-License-Identifier: Apache-2.0
@REM
@REM   Licensed under the Apache License, Version 2.0 (the "License");
@REM   you may not use this file except in compliance with the License.
@REM   You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM   Unless required by applicable law or agreed to in writing, software
@REM   distributed under the License is distributed on an "AS IS" BASIS,
@REM   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM   See the License for the specific language governing permissions and
@REM   limitations under the License.

@echo off
@REM : EnableExtensions for usage of ~
setlocal EnableExtensions

if not defined TC_HOME (
  echo Please initialize the environment variable TC_HOME to the location of your extracted Terracotta kit
  pause
  exit /b 1
)
set TC_HOME=%TC_HOME:"=%

if not exist "%TC_HOME%\license.xml" (
  echo License file not found. Please name it 'license.xml' and put it under '%TC_HOME%'
  pause
  exit /b 1
)

call "%TC_HOME%\server\bin\start-tc-server.bat" "-s" "localhost" "-l" "%TC_HOME%\license.xml" "-N" "tc-cluster" "-y" "availability"

endlocal
