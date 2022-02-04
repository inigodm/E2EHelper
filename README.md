Simple E2E test library to improve the semantics of E2E testing for the project that Amaia will do to create our Thermostat

It, basically, manages DB and HTTP connections in order to let the test more semantics

# Installing 

Is hosted in the maven repository of [jitpack](https://jitpack.io)

But you can use master-SNAPSHOT to get the latest code.

So, to use it, in gradle, you have to add the repository:

```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

And add the dependency:

Currently major version, when this document was done, was 0.0.4, nowadays is:

[![](https://jitpack.io/v/inigodm/E2EHelper.svg)](https://jitpack.io/#inigodm/E2EHelper)

```
dependencies {
    testImplementation 'com.github.inigodm:E2EHelper:0.0.4'
}
```

or use the master branch to get the latest code

```
dependencies {
    testImplementation 'com.github.inigodm:E2EHelper:master-SNAPSHOT'
}
```

# Using it

You can use it simply as part of your unit tests or make another specific sourceset to  manage e2e tests which, being recomended, is more tricky and require more gradle/maven configuration.

To get examples of use a look to existing [tests](https://github.com/inigodm/E2EHelper/tree/master/src/test/kotlin), you could find there some examples of use

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
