LCCI_IMAGE="librecores/librecores-ci:dev"
LCCI_CONTAINER_NAME="lcci-master"

# For local builds
CWP_VERSION=1.5
CWP_MAVEN_REPO=https://repo.jenkins-ci.org/releases
CWP_MAVEN_REPO_PATH=io/jenkins/tools/custom-war-packager/custom-war-packager-cli

build:
	docker build -t ${LCCI_IMAGE} .

run:
	docker run --rm --name ${LCCI_CONTAINER_NAME} -v maven-repo:/root/.m2 \
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}

dev:
	docker run --rm --name ${LCCI_CONTAINER_NAME} -v maven-repo:/root/.m2 \
		-v ${MY_PIPELINE_LIBRARY_DIR}:/var/jenkins_home/pipeline-library \
		-v ${MY_OTHER_PIPELINE_LIBS_DIRS}:/var/jenkins_home/pipeline-libs \ 
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}	

debug:
	docker run --rm --name ${LCCI_CONTAINER_NAME} -e DEBUG=true -p 5005:5005 -v maven-repo:/root/.m2 \
		-v ${MY_PIPELINE_LIBRARY_DIR}:/var/jenkins_home/pipeline-library \
		-v ${MY_OTHER_PIPELINE_LIBS_DIRS}:/var/jenkins_home/pipeline-libs \
		-e DEV_HOST=${CURRENT_HOST} \
		-p 8080:8080 -p 50000:50000 ${LCCI_IMAGE}

.build/cwp-cli-${CWP_VERSION}.jar:
	rm -rf .build
	mkdir -p .build
	wget -O .build/cwp-cli-${CWP_VERSION}.jar $(CWP_MAVEN_REPO)/${CWP_MAVEN_REPO_PATH}/${CWP_VERSION}/custom-war-packager-cli-${CWP_VERSION}-jar-with-dependencies.jar
	touch .build/cwp-cli-${CWP_VERSION}.jar

build-local: .build/cwp-cli-${CWP_VERSION}.jar
	java -jar .build/cwp-cli-${CWP_VERSION}.jar \
	     -configPath packager-config.yml -version ${VERSION} ${CWP_OPTS} \
		 -mvnSettingsFile ${MVN_SETTINGS_FILE}

