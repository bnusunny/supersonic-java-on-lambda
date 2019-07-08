#!/bin/sh

gradle build

# -H:+AllowVMInspection , this option is not work on aws lambda function, should disable it first.
# reference case discussion : 
# https://github.com/oracle/graal/issues/841


native-image --no-server \
        --class-path build/libs/aws.graal-1.0-SNAPSHOT.jar \
        -H:Name=aws-graal \
        -H:Class=sample.Main \
        -H:+ReportUnsupportedElementsAtRuntime \
        -H:-AllowVMInspection \
        -R:-InstallSegfaultHandler


#docker run --rm --name aws-graal -v $(pwd):/work  oracle/graalvm-ce:1.0.0-rc14 \
#    /bin/bash -c "native-image --no-server \
#        --class-path work/build/libs/aws.graal-1.0-SNAPSHOT.jar \
#        -H:Name=aws-graal \
#        -H:Class=sample.Main \
#        -H:+ReportUnsupportedElementsAtRuntime \
#        -H:+AllowVMInspection \
#        -R:-InstallSegfaultHandler \
#        cp aws-grall /work/build/aws-graal"


chmod +x bootstrap

zip aws-graal.zip bootstrap aws-graal

echo deploy aws-graal.zip to AWS Lambda