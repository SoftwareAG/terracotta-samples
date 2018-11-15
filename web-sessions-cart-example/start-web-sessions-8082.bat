@REM Copyright © 2013 - 2018 Software AG, Darmstadt, Germany and/or its licensors
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
@REM : EnableExtensions for usage of ~ ; EnableDelayedExpansion for usage of !!
setlocal EnableExtensions EnableDelayedExpansion

pushd "%~dp0"
set WD=%CD%
popd

if not defined TC_VERSION (
  echo Please initialize the environment variable TC_VERSION to the version of your extracted TerracottaDB kit
  pause
  exit /b 1
)

if not defined TC_HOME (
  echo Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit
  pause
  exit /b 1
)
set TC_HOME=%TC_HOME:"=%

if exist "%TC_HOME%\server\bin\setenv.bat" (
  call "%TC_HOME%\server\bin\setenv.bat"
)

if not defined JAVA_HOME (
  echo Environment variable JAVA_HOME needs to be set
  pause
  exit /b 1
)

set JAVA="%JAVA_HOME%\bin\java.exe"
set JAVAC="%JAVA_HOME%\bin\javac.exe"

echo Starting build and servlet container on port 8082
./mvnw.cmd verify cargo:run -Pcargo,8082

endlocal
