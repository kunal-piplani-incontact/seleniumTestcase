call mvn clean install eclipse:eclipse -Dmaven.test.skip=true

IF %ERRORLEVEL%==0 (
	mkdir .\target\incontact-automation-jar\config
	mkdir .\target\incontact-automation-jar\logs
	mkdir .\target\incontact-automation-jar\response-times
	mkdir .\target\incontact-automation-jar\screenshots
	mkdir .\target\incontact-automation-jar\test-output
	mkdir .\target\incontact-automation-jar\test-suites
	mkdir .\target\incontact-automation-jar\lib
	mkdir .\target\incontact-automation-jar\src\main\resources\en

	xcopy .\src\main\resources\*.ini .\target\incontact-automation-jar\src\main\resources >nul
	xcopy .\src\test\resources\* .\target\incontact-automation-jar\test-suites /E >nul
	xcopy .\config\config.properties .\target\incontact-automation-jar\config >nul
	xcopy /s .\target\lib .\target\incontact-automation-jar\lib >nul
	xcopy /s .\lib .\target\incontact-automation-jar\lib >nul
	xcopy .\target\incontact-0.0.1-SNAPSHOT.jar .\target\incontact-automation-jar >nul
	xcopy .\run.bat .\target\incontact-automation-jar >nul
	xcopy .\src\main\resources\en\*.xml .\target\incontact-automation-jar\src\main\resources\en >nul
) ELSE (
	echo "Build Failed. Skipping the incontact-automation-jar directory generation."
)