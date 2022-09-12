FROM eclipse-temurin:17

RUN apt-get update && apt-get install -y build-essential

ENV APP_HOME /braintree_spring_example
RUN mkdir $APP_HOME
WORKDIR $APP_HOME

ADD . $APP_HOME
RUN ./gradlew build -x test
