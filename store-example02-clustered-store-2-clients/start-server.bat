@echo off
setlocal

set WD=%~d0%~p0
if not defined TC_HOME (
  echo Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit
  pause
  exit /b 1
)
set TC_HOME=%TC_HOME:"=%
set TC_SERVER_HOME=%TC_HOME%\server

if not exist "%TC_SERVER_HOME%\bin\start-tc-server.bat" (
  echo "Modify the script to set TC_SERVER_HOME"
  pause
  exit /b 1
)
call "%TC_SERVER_HOME%\bin\start-tc-server.bat" "-f" "%WD%\tc-config.xml"

endlocal
