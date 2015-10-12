#!/bin/sh
# Note: I've written this using sh so it works in the busybox container too

# USE the trap if you need to also do manual cleanup after the service is stopped,
#     or need to start multiple services in the one container
trap "echo TRAPed signal" HUP INT QUIT KILL TERM

# start service in background here
#/usr/sbin/apachectl start
# the entrypoint provided by the base Postgres container
/docker-entrypoint.sh &

# todo put the web service startup here
java -jar /gitroot/dockstore-webservice/target/dockstore-webservice-*.jar server /hello-world.yml

echo "[hit enter key to exit] or run 'docker stop <container>'"
read

# stop service and clean up here
echo "stopping postgres"
#/usr/sbin/apachectl stop
# not sure if this is right
gosu postgres pg_ctl -D "$PGDATA" -m fast -w stop

echo "exited $0"
