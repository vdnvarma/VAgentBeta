@echo off
echo Starting VAgent Smoke Tests...
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

echo Running smoke tests...
call mvn test -DsuiteXmlFile=src/test/resources/smoke-tests.xml
if %errorlevel% neq 0 (
    echo ERROR: Smoke tests failed
    pause
    exit /b 1
)

echo.
echo Smoke tests completed successfully!
echo Check test-output directory for reports and screenshots
pause
