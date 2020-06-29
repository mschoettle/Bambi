# compile and test bambi
FROM amazoncorretto:8 as java-builder

ARG VERSION=development

COPY [".git", "/work/.git"]
COPY ["src", "/work/src"]
COPY ["gradle", "/work/gradle"]
COPY ["build.gradle", "settings.gradle", "gradlew", "/work/"]

WORKDIR /work

RUN ./gradlew -i build -PprojectVersion=${VERSION} --no-daemon

# create final image
FROM alpine:latest

RUN wget -c -O amazon-corretto-8-jre-8.252.09.1-r0.apk https://d3pxv6yz143wms.cloudfront.net/ea/8.252.09.1/amazon-corretto-8-jre-8.252.09.1-r0.apk && \
    wget -c -O /etc/apk/keys/amazoncorretto.rsa.pub https://d3pxv6yz143wms.cloudfront.net/ea/8.252.09.1/amazoncorretto.rsa.pub && \
    apk add amazon-corretto-8-jre-8.252.09.1-r0.apk && \
    rm -rf amazon-corretto-8-jre-8.252.09.1-r0.apk

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/default-jvm/jre

RUN addgroup -S cip4 && adduser -S cip4 -G cip4 && \
    mkdir /bambidata && chown cip4:cip4 /bambidata && \
    mkdir /BambiHF && chown cip4:cip4 /BambiHF

COPY --chown=cip4:cip4 --from=java-builder ["/work/build/libs/*.jar", "/app/bambi.jar"]

USER cip4

EXPOSE 8080

ENTRYPOINT ["java", "-cp","/app/bambi.jar", "org.cip4.bambi.server.BambiService"]


