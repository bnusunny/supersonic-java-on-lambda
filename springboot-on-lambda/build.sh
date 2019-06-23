#!/usr/bin/env bash
mvn clean test package

docker build \
   --build-arg GRAAL_ARGUMENTS="--no-server --verbose -cp target/*:target/lib/* \
      solid.humank.springbootonlambda.Main \
      -H:IncludeResources=.*.properties|.*META-INF/persistence.xml|.*.xsd \
      -H:+ReportUnsupportedElementsAtRuntime \
      -H:IncludeResourceBundles=com.sun.org.apache.xerces.internal.impl.xpath.regex.message \
      -H:+AllowVMInspection \
      -H:Name=app" \
   -t humank/springboot-on-lambda:latest .