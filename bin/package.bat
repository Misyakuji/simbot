@echo off
echo.
echo [Info] Package the Web project and generate the war/jar package file.
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

pause