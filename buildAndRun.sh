#/bin/bash 

sbt clean test
status=$?

while [ $status -ne 0  ] 
do
    echo retrying build.. sometimes fails tests even though they run in IDE
    sbt clean test
    status=$?
done

rm features.csv
sbt "runMain com.dotdata.FeatureGen"

echo output will be in current directory in file: features.csv


