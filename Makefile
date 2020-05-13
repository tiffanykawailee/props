PKGNAME=props
GOOGLE_JAVA_FORMAT_VERSION=1.7
FORMATTER=lib/google-java-format-$(GOOGLE_JAVA_FORMAT_VERSION)-all-deps.jar

default: build

# Determine the current commit's git hash and identify any version tags
GITHASH := $(shell git log -n1 --pretty='%H')
VERSION_TAG=$(shell git describe --exact-match --tags "$(GITHASH)" 2>/dev/null)

.PHONY: clean
clean: jabba
	@echo "==> Cleaning project artifacts and metadata"
	bazelisk clean

.PHONY: build
build: jabba
	@echo "==> Building $(PKGNAME)"
	bazelisk build //java/core/...

test: jabba
	bazelisk test //java/core/...

.PHONY: fmt
fmt:
	@echo "==> Formatting all the code..."
	$(shell find . -name \"*.java\" | grep -v '.ijwb' | xargs java -jar $(FORMATTER) --set-exit-if-changed)

.PHONY: fmtcheck
fmtcheck:
	@echo "==> Ensuring all the code was properly formatted..."
	find . -name "*.java" | grep -v '.ijwb' | xargs java -jar $(FORMATTER) --set-exit-if-changed -n

.PHONY: vet
vet:
	@echo "==> Not implemented yet."

.PHONY: generate-pom-version
generate-pom-version:
ifeq (, $(VERSION_TAG))
	$(error "Could not find a tag for commit hash: $(GITHASH)")
endif
	@echo "$(VERSION_TAG:v%=%)" > java/central-sync/VERSION

.PHONY: assemble-maven
assemble-maven: jabba
ifeq (, $(shell cat java/central-sync/VERSION))
	$(error "Before running this target, make sure to generate a VERSION file with the _generate-pom-version_ target")
endif
	@echo ""
	@echo "==> Assembling JAR artifacts for publishing to Maven Central..."
	bazelisk build //java/central-sync:assemble-maven

.PHONY: deploy-maven
deploy-maven: assemble-maven
ifeq (, ${DEPLOY_MAVEN_USERNAME})
	$(error "Cannot assemble a JAR without a value for DEPLOY_MAVEN_USERNAME")
endif
ifeq (, ${DEPLOY_MAVEN_PASSWORD})
	$(error "Cannot assemble a JAR without a value for DEPLOY_MAVEN_PASSWORD")
endif
	@echo ""
	@echo "==> Deploying JAR artifacts to Maven Central"
	bazelisk run //java/central-sync:deploy-maven -- release --gpg

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

.PHONY: jabba
jabba:
	@echo "==> This project uses jabba for selecting a JAVA version"
	@echo "==> Before running this command, run:"
	@echo "==> jabba use"

