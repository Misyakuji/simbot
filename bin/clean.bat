@echo off
echo.
echo [Info] Clear the project target generation path.
echo.

%~d0
cd %~dp0

cd ..
call mvn clean

pause