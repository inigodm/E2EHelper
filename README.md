Simple E2E library to improve the semantics of E2E testing for the project that Amaia will do to  create our Thermostat

#To build

Run a docker with example test REST API to launch the tests

In linux you could need

```
sudo mkdir /sys/fs/cgroup/systemd
sudo mount -t cgroup -o none,name=systemd cgroup /sys/fs/cgroup/systemd
```

After of this run de docker:

```
docker rm -f $(docker ps -qa)
docker run -p 80:80 kennethreitz/httpbin
```

After this: ../gradlew tests
