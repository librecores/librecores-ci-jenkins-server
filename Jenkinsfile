node('docker') {
  checkout scm
  sh "docker build -t librecores/librecores-ci-dev:test ."
  // TODO: add some test automation
}
