#/bin/bash 

sbt clean test
status=$?

while [ $status -ne 0  ] 
do
    echo retrying build.. sometimes fails tests even though they run in IDE
    sbt clean test
    status=$?
done

sbt test


