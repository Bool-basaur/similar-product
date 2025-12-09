APP_NAME=similar-product
JAR_PATH=target/$(APP_NAME).jar
IMAGE_NAME=$(APP_NAME)
PORT=5000

.PHONY: clean build test run docker-build docker-run lint mock stop-mock integration

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
	docker run -p $(PORT):5000 --env EXTERNAL_BASE_URL=http://host.docker.internal:3001 $(IMAGE_NAME):local

mock:
	docker run -d --rm \
		--name mock \
		-p 3001:8080 \
		-v $(CURDIR)/wiremock:/home/wiremock \
		wiremock/wiremock:latest

stop-mock:
	-docker stop mock

integration: stop-mock mock docker-build docker-run
