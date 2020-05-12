PKGNAME=props
GOOGLE_JAVA_FORMAT_VERSION=1.7
FORMATTER=lib/google-java-format-$(GOOGLE_JAVA_FORMAT_VERSION)-all-deps.jar

default: build

gitsha := $(shell git log -n1 --pretty='%h')
version=$(shell git describe --exact-match --tags "$(gitsha)" 2>/dev/null)
ifeq ($(version),)
	version := $(gitsha)
endif

.PHONY: jabba
jabba:
	@echo "==> This project uses jabba for selecting a JAVA version"
	@echo "==> Before running this command, run:"
	@echo "==> jabba use"

.PHONY: clean
clean: jabba
	@echo "==> Cleaning project artifacts and metadata"
	bazelisk clean

.PHONY: build
build: jabba
	@echo "==> Building $(PKGNAME)"
	bazelisk build //...

test: jabba
	bazelisk test //...

.PHONY: fmt
fmt:
	@echo "==> Formatting all the code..."
	$(shell find . -name \"*.java\" | xargs java -jar $(FORMATTER) --set-exit-if-changed)

.PHONY: fmtcheck
fmtcheck:
	@echo "==> Ensuring all the code was properly formatted..."
	find . -name "*.java" | xargs java -jar $(FORMATTER) --set-exit-if-changed -n

.PHONY: vet
vet:
	@echo "==> Not implemented yet."

.PHONY: git-hooks
git-hooks:
	@echo ""
	@echo "==> Ensuring all git hooks are linked..."
	find .git/hooks -type l -exec rm {} \;
	find .githooks -type f -exec ln -sf ../../{} .git/hooks/ \;

.PHONY: setup
setup: git-hooks
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

ifeq (,$(wildcard ~/.jabba/jabba.sh))
	@echo ""
	@echo "==> Installing jabba..."
	curl -sL https://github.com/shyiko/jabba/raw/master/install.sh | bash && . ~/.jabba/jabba.sh
	@echo ""
	@echo "Don't forget to: source $$JABBA_HOME/jabba.sh"
	@echo ""
endif
