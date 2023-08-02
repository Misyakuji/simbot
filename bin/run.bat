@echo off
echo.
echo [Info] Run the Web project using the Jar command.
echo.

cd %~dp0
cd ../simbot-starter/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -jar %JAVA_OPTS% simbot-starter-1.0.jar

cd bin
pause