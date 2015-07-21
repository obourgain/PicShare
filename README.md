# PicShare

PicShare is a project to easily share the pictures with your friend and family.

## License

Under Apache Software Version 2 license. Please read `LICENSE.txt`.

## Build

 - java 8
 - maven 3.3.3

```
mvn clean verify
```

## Run

```
mvn clean verify
docker build -t $USER/picshare .
docker run -ti -p 8080:8080 -v "$(pwd)/pictures":/var/pictures $USER/picshare
```
