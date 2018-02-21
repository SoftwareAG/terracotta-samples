@echo off
setlocal

set WD=%~d0%~p0
if not exist "%TC_HOME%" (
  echo Please initialize the environment variable TC_HOME to the location of your extracted TerracottaDB kit
  pause
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

call "%TC_HOME%\tools\cluster-tool\bin\cluster-tool.bat" configure -n myCluster "%WD%\tc-config.xml"
pause

:NORMALIZEPATH
  set RETVAL=%~dpfn1
  exit /b
endlocal
