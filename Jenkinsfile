// Effectively runs on Master until we have a separate Docker agent
node('docker') {
  checkout scm
  sh "docker build -t librecores/librecores-ci-dev:test ."
  // TODO(oleg_nenashev): add some test automation
}
