:: Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
:: Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
@echo off
setlocal

set WD=%~d0%~p0
set TC_HOME=%1
if [%TC_HOME%] == [] (
  echo "USAGE: %0 <TERRACOTTA_KIT_PATH>"
  exit /b 1
)
set TC_HOME=%TC_HOME:"=%

call :NORMALIZEPATH "%TC_HOME%\tools\cluster-tool\conf"
set CLUSTER_TOOL_CONF=%RETVAL%

if not exist "%CLUSTER_TOOL_CONF%\license.xml" (
  echo License file not found. Please name it 'license.xml' and put it under '%CLUSTER_TOOL_CONF%'
  pause
  exit /b 1
)

call "%TC_HOME%\tools\cluster-tool\bin\cluster-tool.bat" configure -n myCluster "%WD%\tc-config-stripe1.xml" "%WD%\tc-config-stripe2.xml"
pause

:NORMALIZEPATH
  set RETVAL=%~dpfn1
  exit /b
endlocal
