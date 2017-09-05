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

final DockerSlaveTemplate ciModules = DockerCloudHelper.fromTemplate("librecores/ci-modules")
ciModules.with{
    labelString = "docker-fusesoc-icarus";
    remoteFs = "/fusesoc"
    ((DockerComputerJNLPLauncher)launcher).user = "root"
}

DockerCloudHelper.setup([fusesocIcarus, ciModules])
