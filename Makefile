LCCI_IMAGE="librecores/librecores-ci:dev"
CURRENT_HOST?=127.0.0.1

build:
	docker build -t ${LCCI_IMAGE} .

run:
	docker run --rm -v maven-repo:/root/.m2 \
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}

dev:
	docker run --rm -v maven-repo:/root/.m2 \
		-v ${MY_PIPELINE_LIBRARY_DIR}:/var/jenkins_home/pipeline-library \
		-v ${MY_OTHER_PIPELINE_LIBS_DIRS}:/var/jenkins_home/pipeline-libs \ 
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}	

debug:
	docker run --rm -e DEBUG=true -p 5005:5005 -v maven-repo:/root/.m2 \
		-v ${MY_PIPELINE_LIBRARY_DIR}:/var/jenkins_home/pipeline-library \
		-v ${MY_OTHER_PIPELINE_LIBS_DIRS}:/var/jenkins_home/pipeline-libs \
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}

build-local-war:
	mvn clean package -Plocal-build

