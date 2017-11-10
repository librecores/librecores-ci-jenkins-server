#! /bin/bash -e
# Additional wrapper, which adds custom environment options for the run

extra_java_opts=( \
  '-Djenkins.install.runSetupWizard=false -Djenkins.model.Jenkins.slaveAgentPort=50000' \
  '-Djenkins.model.Jenkins.slaveAgentPortEnforce=true' \
  "-Dio.jenkins.dev.security.createAdmin=${CONF_CREATE_ADMIN}" \
  "-Dio.jenkins.dev.security.allowRunsOnMaster=${CONF_ALLOW_RUNS_ON_MASTER}" \
  '-Dhudson.model.LoadStatistics.clock=1000' \
)

if [ -z "$DEV_HOST" ] ; then
  echo "WARNING: DEV_HOST is undefined, localhost will be used. Some logic like Docker Cloud may work incorrectly."
else
  extra_java_opts+=( "-Dio.jenkins.dev.host=${DEV_HOST}" )
fi

if [[ "$DEBUG" ]] ; then
  extra_java_opts+=( \
    '-Xdebug' \
    '-Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=y' \
  )
fi

export JAVA_OPTS="$JAVA_OPTS ${extra_java_opts[@]}"

echo "********YOUR SERVER ADDRESS WILL BE AS FOLLOWS*******"
echo "localhost: 127.0.0.1:8080 (Usable on some setups)"
container_ip=$(ip route get 8.8.8.8 | awk '/8.8.8.8/ {print $NF}')
echo "via docker container IP: ${container_ip}:8080 (Usable on some setups)"
server_ip=$(curl -s http://checkip.amazonaws.com |cut -d " " -f 5)
echo "Server external address: ${server_ip}:8080 (OS-agnostic as long as server is externally GUI-launchable (No by default))"
echo "*****************************************************"

exec /usr/local/bin/jenkins.sh "$@"
