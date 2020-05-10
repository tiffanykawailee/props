PKGNAME=props
GOOGLE_JAVA_FORMAT_VERSION=1.7

default: build

gitsha := $(shell git log -n1 --pretty='%h')
version=$(shell git describe --exact-match --tags "$(gitsha)" 2>/dev/null)
ifeq ($(version),)
	version := $(gitsha)
endif

.PHONY: build
build:
	@echo "==> Building $(PKGNAME)"
	bazelisk build //...

.PHONY: clean
clean:
	@echo "==> Cleaning project artifacts and metadata"
	bazelisk clean

.PHONY: fmt
fmt:
	@echo "==> Formatting all the code..."
	$(shell find . -name \"*.java\" | xargs java -jar lib/google-java-format-1.8-all-deps.jar --set-exit-if-changed)

.PHONY: fmtcheck
fmtcheck:
	@echo "==> Ensuring all the code was properly formatted..."
	find . -name "*.java" | xargs java -jar lib/google-java-format-1.8-all-deps.jar --set-exit-if-changed -n

.PHONY: vet
vet:
	@echo "==> Not implemented yet."

FORMATTER=lib/google-java-format-$(GOOGLE_JAVA_FORMAT_VERSION)-all-deps.jar
.PHONY: setup
setup:
ifeq (,$(wildcard $(FORMATTER)))
	@echo ""
	@echo "==> Installing google-java-format..."
	@mkdir -p lib
	curl -sSfL -o $(FORMATTER) https://github.com/google/google-java-format/releases/download/google-java-format-$(GOOGLE_JAVA_FORMAT_VERSION)/google-java-format-$(GOOGLE_JAVA_FORMAT_VERSION)-all-deps.jar
endif

ifeq (, $(shell which bazelisk))
ifeq (, $(shell which go))
	$(error "Bazelisk is not installed and golang is not available")
endif
	@echo ""
	@echo "==> Installing bazelisk..."
	go get github.com/bazelbuild/bazelisk
ifeq (, $(shell which bazelisk))
	$(error "Please add '$(shell go env GOPATH)/bin' to your current PATH")
endif
endif

	@echo ""
	@echo "==> Ensuring all git hooks are linked..."
	find .git/hooks -type l -exec rm {} \;
	find .githooks -type f -exec ln -sf ../../{} .git/hooks/ \;

test:
	bazelisk test //...
