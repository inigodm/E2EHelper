Simple E2E library to improve the semantics of E2E testing for the project that Amaia will do to  create our Thermostat

#Installing

Is hosted in the maven repository of [jitpack](https://jitpack.io)

So, to use it, in gradle, you have to add the repository:

```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

And add the dependencie:

```
dependencies {
    testImplementation 'com.github.inigodm:E2EHelper:0.0.4'
}
```

# Using it

You can use it in your tests sourceset simpli using it on your tests source directory or make another specific sourceset to  manage e2e tests which, being recomended, is more tricky, so:

To start using it, simply add to your dependencies and take a look to existing tests, you could find there some exameples of use

#To build from source

## Testing it

It uses a dummie web app in a docker and a sqlite db to tests:

To run the docker with an example REST API to launch the tests

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

There is a .sh in /scripts to do it

After this: 

```./gradlew tests```

And to build

```./gradlew build```

or

```./gradlew build -Penabletest```

To launch build after tests
