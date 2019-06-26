#!/usr/bin/env bash

source dump-aws-logs.sh

APPDIR=target/
BUNDLEDIR=target/bundle

function bundle() {
    rm -f response.txt

    CP=`java -jar target/micro-0.0.1-SNAPSHOT.jar --thin.classpath --thin.profile=graal`
    M2_REPO=$HOME/.m2/repository
    CP=$CP:$M2_REPO/org/springframework/spring-orm/5.1.7.RELEASE/spring-orm-5.1.7.RELEASE.jar:$M2_REPO/org/springframework/spring-jdbc/5.1.7.RELEASE/spring-jdbc-5.1.7.RELEASE.jar:$M2_REPO/javax/persistence/javax.persistence-api/2.2/javax.persistence-api-2.2.jar
    CP=$CP:$M2_REPO/org/springframework/spring-boot-graal-feature/0.5.0.BUILD-SNAPSHOT/spring-boot-graal-feature-0.5.0.BUILD-SNAPSHOT.jar

    native-image -Dio.netty.noUnsafe=true --no-server -H:Name=$APPDIR/micro-runner -H:+ReportExceptionStackTraces --no-fallback --allow-incomplete-classpath --report-unsupported-elements-at-runtime -cp $APPDIR/classes:$CP com.example.micro.MicroApplication

    mkdir -p ${BUNDLEDIR}
    cp -r ./bootstrap ${APPDIR}/*-runner  ${BUNDLEDIR}
    chmod 755 ${BUNDLEDIR}/bootstrap
    cd ${BUNDLEDIR} && zip -q function.zip bootstrap function.sh *-runner ; cd -

}

[ -z "$NOBUNDLE" ] && bundle

clearStreams

echo Deleting old function
aws lambda delete-function \
    --function-name springboot-on-lambda

echo Creating function
aws lambda create-function \
    --function-name springboot-on-lambda \
    --timeout 10 \
    --zip-file fileb://${BUNDLEDIR}/function.zip \
    --handler function.sh \
    --runtime provided \
    --role ${LAMBDA_ROLE_ARN}

echo
time aws lambda invoke --function-name spring-graalvm --payload '{"firstName":"James", "lastName": "Lipton"}' response.txt
cat response.txt
echo

echo
time aws lambda invoke --function-name spring-graalvm --payload '{"firstName":"James", "lastName": "Halpert"}' response.txt
cat response.txt
echo

echo
time aws lambda invoke --function-name spring-graalvm --payload '{"firstName":"James", "lastName": "Jamm"}' response.txt
cat response.txt
echo

dump
