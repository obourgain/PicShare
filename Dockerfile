FROM java:8-jre

ADD target/share-pictures-*.jar /var/share-pictures.jar
EXPOSE 8080

CMD ["java", "-DPROD_MODE=true", "-jar", "/var/share-pictures.jar"]
