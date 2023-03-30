# Braintree Spring Example

[![Build Status](https://github.com/braintree/braintree_spring_example/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/braintree/braintree_spring_example/actions/workflows/ci.yml)

An example Braintree integration for Spring (Java).

## Setup Instructions

1. [Install gradle](https://docs.gradle.org/current/userguide/installation.html) and project dependencies:

  ```
  ./gradlew build -x test
  ```

2. Copy the contents of `example.config.properties` into a new file named `config.properties` and fill in your Braintree API credentials. Credentials can be found by navigating to  Account > My User > View Authorizations in the Braintree Control Panel. Full instructions can be [found on our support site](https://articles.braintreepayments.com/control-panel/important-gateway-credentials#api-credentials).

3. Start server:

  ```
  java -jar build/libs/braintree_spring_example.jar
  ```

  This starts the server on port `8080` listening to all interfaces.

## Running tests

All tests are integration tests. Integration tests make API calls to Braintree and require that you set up your Braintree credentials. You can run this project's integration tests by adding your sandbox API credentials to `config.properties` and running `./gradlew test`.

## Testing Transactions

Sandbox transactions must be made with [sample credit card numbers](https://developers.braintreepayments.com/reference/general/testing/java#credit-card-numbers), and the response of a `Transaction.sale()` call is dependent on the [amount of the transaction](https://developers.braintreepayments.com/reference/general/testing/java#test-amounts).

## Pro Tips

* Run `java -Dserver.port=4000 -jar build/libs/braintree_spring_example.jar` to start the server on port 4000. Replace `4000` with any number to start it on a different port.

## Help

 * Found a bug? Have a suggestion for improvement? Want to tell us we're awesome? [Submit an issue](https://github.com/braintree/braintree_spring_example/issues)
 * Trouble with your integration? Contact [Braintree Support](https://support.braintreepayments.com/) / support@braintreepayments.com
 * Want to contribute? [Submit a pull request](https://help.github.com/articles/creating-a-pull-request)

## Disclaimer

This code is provided as is and is only intended to be used for illustration purposes. This code is not production-ready and is not meant to be used in a production environment. This repository is to be used as a tool to help merchants learn how to integrate with Braintree. Any use of this repository or any of its code in a production environment is highly discouraged.
