#!/usr/bin/env bash

APPDIR=target/
BUNDLEDIR=target/bundle

function bundle() {
    rm -f response.txt

    mvn clean package -DskipTests=true -Dnative=true -Dnative-image.docker-build=true

    mkdir -p ${BUNDLEDIR}
    cp -r target/wiring-classes/bootstrap ${APPDIR}/*-runner  ${BUNDLEDIR}
    chmod 755 ${BUNDLEDIR}/bootstrap
    cd ${BUNDLEDIR} && zip -q function.zip bootstrap function.sh *-runner ; cd -

}

[ -z "$NOBUNDLE" ] && bundle

sls deploy
