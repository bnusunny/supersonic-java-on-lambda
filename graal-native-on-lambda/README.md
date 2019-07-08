# Graal Native Image on AWS Lambda Custom Runtime

At first, there are 3 steps to go through the packaging process.

* Build Java application with gradle build

* Recompile by GraalVM native-image, this would make you a native execution binary file as you named

* Pack the native execution binary file with an AWS Lambda Custom runtime file - bootstrap

more information refer to:

https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/runtimes-custom.html
https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/runtimes-walkthrough.html

## Known issue regarding dump heap signal

GraalVM Native Image does not implement JVMTI agent, hence triggering heap dump creation from Applications area is impossible. Apply -H:+AllowVMInspection flag with the native-image tool for Native Image processes. This way your application will handle signals and get a heap dump when it receives SIGUSR1 signal. Guest language REPL process must be started also with the --jvm flag to monitor it using GraalVM VisualVM. This functionality is available with GraalVM Enterprise Edition. It is not available in GraalVM open source version available on GitHub. See the Generating Native Heap Dumps page for details on creating heap dumps from a native image process.

Until 2019-07-08, running graalvm native-image binary on aws lambda would still occuer the exception:
> Util_sun_misc_Signal.ensureInitialized: CSunMiscSignal.open() failed
> defatil discussion refer to: https://github.com/oracle/graal/issues/841

So, we need to disable this feature.

## How To Build

check with build-image.sh.
Using docker and make aws-graal.zip or run it as your local graalvm installed.

```bash
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
```

## Deploy to AWS Lambda Custom Runtime

The most important configuration is:

* runtime
* handler

Since this application is a self-build runtime, so the runtime should be only **"Provided"**, and the handler should be the **native-executive-binary name**, which is **aws-graal** in the sample.

```bash

aws lambda create-function \
--function-name "aws-graal" \
--runtime "provided" \
--role "arn:aws:iam::584518143473:role/lambda_basic_execution" \
--handler "aws-graal" \
--timeout 5 \
--memory-size 128 \
--zip-file "fileb://aws-graal.zip"

```
