@echo off
cd objects
javac *.java
cd ..
javac *.java
if %ERRORLEVEL% neq 0 goto error
java lodeRunner_serv
:error