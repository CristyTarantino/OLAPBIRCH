@echo off
REM avvio il servizio di mysql
net start mysql

REM avvio il nostro server
java -jar OlapBirch.jar

pause 