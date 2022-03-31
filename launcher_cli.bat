@echo off
cls
c:
cd C:\Users\utilisateur\Documents\universite\L2\S4\Info4B\Projet\objects
javac *.java
cd ..
javac *.java
if %ERRORLEVEL% neq 0 goto error
java lodeRunner_cli %1 %2
:error