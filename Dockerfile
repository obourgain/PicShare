FROM java:8-jre

ADD target/share-pictures-*.jar /var/share-pictures.jar
EXPOSE 8080
VOLUME /var/pictures
WORKDIR /var

CMD ["java", "-DPROD_MODE=true", "-Dhttp.disable.gzip=true", "-jar", "/var/share-pictures.jar"]
