@echo off
echo Starting VAgent Selenium Tests...
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and add it to your PATH
    pause
    exit /b 1
)

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11+ and add it to your PATH
    pause
    exit /b 1
)

echo Java and Maven are available
echo.

REM Install dependencies
echo Installing dependencies...
call mvn clean install -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to install dependencies
    pause
    exit /b 1
)

echo Dependencies installed successfully
echo.

REM Run tests
echo Running all tests...
call mvn test
if %errorlevel% neq 0 (
    echo ERROR: Tests failed
    pause
    exit /b 1
)

echo.
echo All tests completed successfully!
echo Check test-output directory for reports and screenshots
pause
