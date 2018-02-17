@echo off
setlocal enableDelayedExpansion

set str=%0
SET LIB_PATH=.

IF not "!str!"=="run.bat" (
	set lead=!str:\run.bat=!
	cd !lead!
	SET LIB_PATH=!lead!
)

SET CLASSPATH=%LIB_PATH%;%LIB_PATH%\lib\*;incontact-0.0.1-SNAPSHOT.jar

IF "%1"=="" (
	echo Class Path is : %CLASSPATH%
	echo Running default tests...
	java org.testng.TestNG test-suites\testng.xml
	call:outputBackup testng.xml
	GOTO:eof
)

:LOOP
echo Running tests from suite: %1
java org.testng.TestNG test-suites\%1
set filename=%1
set filename=%filename:/=__%
set filename=%filename:\=__%
call:outputBackup %filename%
SHIFT

IF not "%1"=="" (
	GOTO LOOP
)
GOTO:eof

:outputBackup
IF not exist .\test-output-backup mkdir test-output-backup
xcopy /i "test-output" "test-output-backup\!date:~-10,2!_!date:~-7,2!_!date:~-4,4!__!time:~0,2!_!time:~3,2!_!time:~6,2!-test-output-%1"
mkdir test-output

echo Test Execution completed.
