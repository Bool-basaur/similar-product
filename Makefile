# Makefile para similar-product
APP_NAME = similar-product
IMAGE_NAME = $(shell echo $(shell basename $(shell git rev-parse --show-toplevel 2>/dev/null || echo .)) | tr '[:upper:]' '[:lower:]')/$(APP_NAME)
JAR = target/*.jar
PORT = 5000

.PHONY: help build test clean run docker-build docker-run compose-up compose-down compose-test fmt lint package

help:
	@echo "Targets:"
	@echo "  make build         -> mvn clean package (skip tests by default)"
	@echo "  make test          -> run unit/integration tests"
	@echo "  make run           -> run the app locally (requires java)"
	@echo "  make docker-build  -> build docker image"
	@echo "  make docker-run    -> run docker image (port 5000)"
	@echo "  make compose-up    -> docker-compose up -d (mocks/integration)"
	@echo "  make compose-down  -> docker-compose down"
	@echo "  make compose-test  -> start test compose (docker-compose.test.yml)"
	@echo "  make clean         -> mvn clean"
	@echo "  make fmt           -> format (none configured, placeholder)"
	@echo "  make lint          -> run static checks (checkstyle/spotbugs)"

build:
	@mvn -B -DskipTests clean package

test:
	@mvn -B clean verify

clean:
	@mvn -B clean
	@rm -rf .mvn/wrapper m2

run: build
	@java -jar target/*.jar

docker-build:
	docker build -t $(APP_NAME):$(shell git rev-parse --short HEAD) .

docker-run: docker-build
	docker run --rm -p $(PORT):5000 --name $(APP_NAME) $(APP_NAME):$(shell git rev-parse --short HEAD)

docker-run-bg: docker-build
	docker run -d -p $(PORT):5000 --name $(APP_NAME) $(APP_NAME):$(shell git rev-parse --short HEAD)

docker-push:
	# requires login
	docker tag $(APP_NAME):$(shell git rev-parse --short HEAD) $(IMAGE_NAME):$(shell git rev-parse --short HEAD)
	docker push $(IMAGE_NAME):$(shell git rev-parse --short HEAD)

compose-up:
	docker-compose up -d

compose-down:
	docker-compose down

compose-test:
	docker-compose -f docker-compose.test.yml up --build -d

compose-test-down:
	docker-compose -f docker-compose.test.yml down

lint:
	mvn -B -DskipTests=false verify checkstyle:check spotbugs:check

package:
	@mvn -B -DskipTests package
