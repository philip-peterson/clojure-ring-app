IMAGE ?= clojure-ring-app
NAME  ?= clojure-ring-app
PORT  ?= 3399

.DEFAULT_GOAL := help

.PHONY: help build up down restart logs shell dev compile watch clean

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## ' $(MAKEFILE_LIST) \
		| awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-10s\033[0m %s\n", $$1, $$2}'

build: ## Build the image (installs deps + compiles the ClojureScript bundle)
	docker build -t $(IMAGE) .

up: ## Run the app in the background on http://localhost:$(PORT)
	docker run -d --name $(NAME) -p $(PORT):3399 $(IMAGE)

down: ## Stop and remove the container
	docker rm -f $(NAME) || true

restart: down up ## Restart the container

logs: ## Follow the container logs
	docker logs -f $(NAME)

shell: ## Open a bash shell in the running container
	docker exec -it $(NAME) bash

dev: ## Run server + ClojureScript watcher with live reload (source mounted)
	docker run --rm -it --init \
		--name $(NAME) \
		-p $(PORT):3399 \
		-v $(PWD)/src:/app/src \
		-v $(PWD)/resources:/app/resources \
		-v $(PWD)/shadow-cljs.edn:/app/shadow-cljs.edn \
		$(IMAGE) sh -c 'clojure -M:cljs watch app & exec clojure -M -m clojure-ring-app.core'

compile: ## Rebuild the ClojureScript bundle into resources/public/js (mounted source)
	docker run --rm \
		-v $(PWD)/src:/app/src \
		-v $(PWD)/resources:/app/resources \
		-v $(PWD)/shadow-cljs.edn:/app/shadow-cljs.edn \
		$(IMAGE) clojure -M:cljs compile app

watch: ## Watch-recompile ClojureScript against the mounted source
	docker run --rm -it \
		-v $(PWD)/src:/app/src \
		-v $(PWD)/resources:/app/resources \
		-v $(PWD)/shadow-cljs.edn:/app/shadow-cljs.edn \
		$(IMAGE) clojure -M:cljs watch app

clean: ## Remove the container and image
	docker rm -f $(NAME) || true
	docker rmi $(IMAGE) || true
