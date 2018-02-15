:: Copyright (c) 2011-2018 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
:: Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
@echo off
setlocal

set WD=%~d0%~p0
set TC_SERVER_HOME=%WD%..\..\server

if not exist "%TC_SERVER_HOME%\bin\start-tc-server.bat" (
  echo "Modify the script to set TC_SERVER_HOME"
  pause
  exit /b 1
)
call "%TC_SERVER_HOME%\bin\start-tc-server.bat" "-f" "%WD%\tc-config-stripe2.xml"

endlocal
