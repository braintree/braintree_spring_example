# Braintree Spring Example

An example Braintree integration for Spring (Java)

1. Install gradle and project dependencies:

  ```
  brew install gradle
  ./gradlew build
  ```

2. Copy the `example.config.properties` to `config.properties` and fill in your Braintree API credentials. Credentials can be found by navigating to  Account > My user > View API Keys in the Braintree control panel. Full instructions can be [found on our support site](https://articles.braintreepayments.com/control-panel/important-gateway-credentials#api-credentials).

3. Start server

  ```
  java -jar build/libs/bt-example-0.1.0.jar
  ```

  This starts the server on port `8080` listening to all interfaces.

## Running tests

All tests are integration tests. Integration tests make API calls to Braintree and require that you set up your Braintree credentials. You can run this project's integration tests by adding your sandbox API credentials to `config.properties` and running `./gradlew test`.

## Pro Tips

* Run `java -Dserver.port=4000 -jar build/libs/bt-example-0.1.0.jar` to start the server on port 4000. Replace `4000` with any number to start it on a different port.

## Disclaimer

This code is provided as is and is only intended to be used for illustration purposes. This code is not production-ready and is not meant to be used  in a production environment. This repository is to be used as a tool to help merchants learn how to integrate with Braintree. Any use of this repository or any of its code in a production environment is highly discouraged.
