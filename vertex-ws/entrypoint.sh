#!/bin/bash

java ${JAVA_OPTS} \
 -javaagent:/newrelic/newrelic.jar -Dnewrelic.config.file=/newrelic/newrelic.yml \
 -Dnewrelic.config.license_key=${NR_LICENSE_KEY} \
 -Dnewrelic.config.app_name=${NR_APP_NAME} \
 -Dnewrelic.config.distributed_tracing.enabled=true \
 -cp "vertex-ws-1.0-SNAPSHOT-fat.jar" \
 org.jan.MainKt
