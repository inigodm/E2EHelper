#!/bin/bash

# Run a docker with example test REST API to launch the tests

sudo mkdir /sys/fs/cgroup/systemd
sudo mount -t cgroup -o none,name=systemd cgroup /sys/fs/cgroup/systemd

docker rm -f $(docker ps -qa)
docker run -p 80:80 kennethreitz/httpbin

#After this: ../gradlew tests
