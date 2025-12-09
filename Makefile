APP_NAME=similar-product
JAR_PATH=target/$(APP_NAME).jar
IMAGE_NAME=$(APP_NAME)
PORT=5000

.PHONY: clean build test run docker-build docker-run lint

clean:
	mvn clean

build:
	mvn -DskipTests package

test:
	mvn clean verify

lint:
	mvn checkstyle:check spotbugs:check

run:
	java -jar $(JAR_PATH)

docker-build:
	docker build -t $(IMAGE_NAME):local .

docker-run:
	docker run -p $(PORT):5000 $(IMAGE_NAME):local