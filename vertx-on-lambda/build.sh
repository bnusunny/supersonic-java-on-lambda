#!/bin/bash

docker run --rm -it -v $(pwd):/work oracle/graalvm-ce:19.1.0 /bin/bash -c "gu install native-image && cd /work && ./mvnw clean package"

echo deploy ./target/myapp-0.0.1-SNAPSHOT-aws-function.zip to AWS Lambda
