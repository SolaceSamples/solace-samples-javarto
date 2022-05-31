# Solace Samples JavaRTO

## Solace Messaging API for JavaRTO

The Java Real-Time Optimized (RTO) messaging API (also referred to as solclientj) is a low-latency Java Native Interface (JNI) wrapper for the C API. For "pure Java" Solace APIs, please check out JCSMP or PubSub+ Java API instead.

The "Getting Started" tutorials will get you up to speed and sending messages with Solace technology as quickly as possible. There are three ways you can get started:

- Follow [these instructions](https://cloud.solace.com/learn/group_getting_started/ggs_signup.html) to quickly spin up a cloud-based Solace messaging service for your applications.
- Follow [these instructions](https://docs.solace.com/Solace-SW-Broker-Set-Up/Setting-Up-SW-Brokers.htm) to start the Solace VMR in leading Clouds, Container Platforms or Hypervisors. The tutorials outline where to download and how to install the Solace VMR.
- If your company has Solace message routers deployed, contact your middleware team to obtain the host name or IP address of a Solace message router to test against, a username and password to access it, and a VPN in which you can produce and consume messages.

## Contents

This repository contains code and matching tutorial walk-through for five different basic Solace messaging patterns. For a nice introduction to the Solace API and associated tutorials, check out the [tutorials home page](https://tutorials.solace.dev/javarto/).

See the individual tutorials for details:

- [Publish/Subscribe](https://tutorials.solace.dev/javarto/publish-subscribe/): Learn how to set up pub/sub messaging on a Solace VMR.
- TODO - Persistence: Learn how to set up persistence for guaranteed delivery.
- TODO - Request/Reply: Learn how to set up request/reply messaging.
- TODO - Confirmed Delivery: Learn how to confirm that your messages are received by a Solace message router.
- TODO - Topic to Queue Mapping: Learn how to map existing topics to Solace queues.

## Checking out and Building

To check out the project and build it, do the following:

  1. clone this GitHub repository
  1. `cd solace-samples-javarto`

## Build the Samples

To build the samples:

  1. `./gradlew assemble`
  
## Running the Samples

To try individual samples, build the project from source and then run samples like the following:

On **Linux**:

```
./build/staged/bin/TopicSubscriber <<HOST_ADDRESS>> <<VPN> <<client-username>>

```

On **MacOS & Darwin**:

```
./build/staged/bin/TopicSubscriber <<HOST_ADDRESS>> <<VPN> <<client-username>>

```

On **Windows** Command Prompt:

```
build\staged\bin\TopicSubscriber.bat <<HOST_ADDRESS>> <<VPN> <<client-username>>

```

## Exploring the Samples

### Setting up your preferred IDE

Using a modern Java IDE provides cool productivity features like auto-completion, on-the-fly compilation, assisted re-factoring and debugging which can be useful when you're exploring the samples and even modifying the samples. Follow the steps below for your preferred IDE.

#### Using Eclipse

To generate Eclipse metadata (.classpath and .project files), do the following:

    ./gradlew eclipse

Once complete, you may then import the projects into Eclipse as usual:

 *File -> Import -> Existing projects into workspace*

Browse to the *'solace-samples-javarto'* root directory. All projects should import
free of errors.

> If your Eclipse has the Gradle plugin already installed, the instead of generating the Eclipse metadata files as above, simply import this tutorial as a Gradle project: *File -> Import -> Gradle -> Gradle Project*

#### Using IntelliJ IDEA

To generate IDEA metadata (.iml and .ipr files), do the following:

    ./gradlew idea

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License

This project is licensed under the Apache License, Version 2.0. - See the [LICENSE](LICENSE) file for details.

## Resources

For more information try these resources:

- [Tutorials](https://tutorials.solace.dev/)
- The Solace Developer Portal website at: http://solace.dev
- Get a better understanding of [Solace technology](https://solace.com/products/tech/).
- Check out the [Solace blog](https://solace.com/blog/) for other interesting discussions around Solace technology
- Ask the [Solace community.](https://solace.community)
