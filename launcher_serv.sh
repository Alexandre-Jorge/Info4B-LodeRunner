#!/bin/bash
cd objects
javac *.java
cd ..
javac *.java
java LodeRunner_serv
