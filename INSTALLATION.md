# Installation
> PostgreSQL is required since 2015-08-02, before that, no DB was used.

## Prerequisites

You need to have installed `docker`. The whole application has be built using docker 1.7.1. Make sure to have a recent enough version of docker.

## Fetch docker images

```
$ docker pull postgres:9.4
$ docker pull alecharp/picshare:latest
```

## Start `postgres` database

```
$ mkdir -p /data/picshare/db
$ docker run \
    -e POSTGRES_PASSWORD=test \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v /data/picshare/db:/var/lib/postgresql/data/pgdata \
    --name postgres-picshare \
    -d postgres:9.4
```

Make sure to change the __POSTGRES_PASSWORD__ value and use it in the next `psql` command.

## Configure `postgres` database

```
$ docker run --rm -it \
    --link postgres-picshare:postgres \
    postgres:9.4 \
    psql -h postgres -p 5432 -U postgres --password
(use password from previous section)

postgres=# CREATE USER picshareuser WITH ENCRYPTED PASSWORD 'picsharepassword';
postgres=# CREATE DATABASE picshare WITH OWNER picshareuser;
```

Make sure to change the __picsharepassword__ value and report this change to  `src/main/resources/picshare.properties`.

No other tasks will be required on the database as the project is using flyway to migrate it on application start.

## Start `picshare` docker container

```
$ mkdir -p /data/picshare/storage
$ docker run \
    -p 8080:8080 \
    -v /data/picshare/storage:/var/pictures \
    --link postgres-picshare:postgres \
    -ti alecharp/picshare
```

The pictures file will be stored in `/data/picshare/storage`

The alias of the postgres-picshare container will be used in `picshare.properties` for the `jdbc.url`. If you change it, report the modification to `src/main/resources/picshare.properties`.
