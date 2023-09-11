setup:
	./gradlew clean && \
./gradlew build && \
docker-compose build

up-instantly:
	docker-compose up -d


up-sequentially:
	docker-compose up -d redis master-app && \
sleep 30 && \
docker-compose up -d slave-app

clean:
	docker-compose down --remove-orphans && \
docker volume rm $$(docker volume ls -qf dangling=true) && \
docker rmi $$(docker images | grep -e ''token-bucket'')