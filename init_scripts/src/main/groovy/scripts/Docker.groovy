import com.github.kostyasha.yad.DockerSlaveTemplate
import com.github.kostyasha.yad.launcher.DockerComputerJNLPLauncher
import hudson.tasks.Maven.MavenInstallation
import hudson.tools.ToolLocationNodeProperty
import jenkins.model.Jenkins
import org.jenkinsci.plugins.yad.DockerCloudHelper

println("=== Installing Docker Cloud for Linux nodes")

final DockerSlaveTemplate fusesocIcarus = DockerCloudHelper.fromTemplate("onenashev/fusesoc-icarus")
fusesocIcarus.with {
        labelString = "docker-fusesoc-icarus";
        remoteFs = "/fusesoc"
        ((DockerComputerJNLPLauncher)launcher).user = "root"
}

final DockerSlaveTemplate lcciModules = DockerCloudHelper.fromTemplate("librecores/ci-modules")
lcciModules.with{
    labelString = "librecores-ci-modules"
    remoteFs = "/fusesoc"
    ((DockerComputerJNLPLauncher)launcher).user = "root"
    dockerContainerLifecycle.createContainer.volumes = ["lcci-tools:/tools"]
}

final DockerSlaveTemplate lcciBase = DockerCloudHelper.fromTemplate("librecores/librecores-ci")
lcciBase.with{
    labelString = "librecores-ci";
    remoteFs = "/"
    ((DockerComputerJNLPLauncher)launcher).user = "root"
}

DockerCloudHelper.setup([fusesocIcarus, lcciBase, lcciModules])
