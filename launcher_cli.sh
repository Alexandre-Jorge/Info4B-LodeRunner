#!/bin/bash
cd objects
javac *.java
cd ..
javac *.java
if [$? -eq 0]
then
    java LodeRunner_cli $1 $2
else
    echo $?
    exit 1
fi