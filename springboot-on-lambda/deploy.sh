#!/usr/bin/env bash

source dump-aws-logs.sh

APPDIR=target/
BUNDLEDIR=target/bundle

function bundle() {
    rm -f response.txt
    rm hello.zip hello
    mvn clean test package

    native-image --no-fallback \
        --report-unsupported-elements-at-runtime \
        -jar target/springboot-on-lambda-0.0.1.jar \
        -H:Name=hello

    chmod 755 bootstrap hello
    zip hello.zip bootstrap hello

    echo deploy hello.zip to AWS Lambda

}

[ -z "$NOBUNDLE" ] && bundle

clearStreams

echo Deleting old function
aws lambda delete-function \
    --function-name nativesb

echo Creating function
aws lambda create-function \
    --function-name nativesb \
    --timeout 10 \
    --zip-file fileb://hello.zip \
    --handler hello \
    --runtime provided \
    --role ${LAMBDA_ROLE_ARN}

echo
time aws lambda invoke --function-name nativesb --payload '{"firstName":"James", "lastName": "Lipton"}' response.txt
cat response.txt
echo

echo
time aws lambda invoke --function-name nativesb --payload '{"firstName":"James", "lastName": "Halpert"}' response.txt
cat response.txt
echo

echo
time aws lambda invoke --function-name nativesb --payload '{"firstName":"James", "lastName": "Jamm"}' response.txt
cat response.txt
echo

dump
