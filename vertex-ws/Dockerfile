FROM alpine as builder

ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/6.2.0/newrelic-agent-6.2.0.jar /nr/newrelic.jar
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/6.2.0/newrelic.yml /nr/newrelic.yml

FROM openjdk:8u232-jre-slim

LABEL maintainer="MovilePay"
LABEL version="1.0"

ENV NR_APP_NAME="movilepay-vertex-ws"
ENV NR_LICENSE_KEY="d0598448142ea730672b4c6781042902FFFFNRAL"

WORKDIR /usr/src/app

COPY target/vertex-ws-1.0-SNAPSHOT-fat.jar .
ADD entrypoint.sh /

RUN mkdir /newrelic
COPY --from=builder /nr/* /newrelic/

EXPOSE 9009

ENTRYPOINT ["/entrypoint.sh"]

