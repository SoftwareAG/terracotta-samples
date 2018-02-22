@echo off
setlocal

set WD=%~d0%~p0
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

set TC_CP=.
rem Add the client jars in the classpath
for /F "usebackq delims=" %%I in (`dir /b /s "%TC_HOME%\client\*.jar"`) do call set "TC_CP=%%TC_CP%%;%%I"
rem Add the logback configuration to the classpath
set "TC_CP=%TC_CP%;%TC_HOME%\client\logging\impl"

echo Compiling the sample class..
%JAVAC% -classpath "%TC_CP%" "%WD%\src\Store3MultiStripe.java"

echo Starting the TC DB sample client, it's going to try to connect to your local servers..
%JAVA% -cp "%TC_CP%;%WD%\src" -Xmx200m Store3MultiStripe
pause

endlocal
