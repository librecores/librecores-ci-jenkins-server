import com.github.kostyasha.yad.DockerSlaveTemplate
import com.github.kostyasha.yad.launcher.DockerComputerJNLPLauncher
import hudson.tasks.Maven.MavenInstallation
import hudson.tools.ToolLocationNodeProperty
import jenkins.model.Jenkins
import org.jenkinsci.plugins.yad.DockerCloudHelper

println("=== Installing Docker Cloud for Linux nodes")

// Default agent image
final DockerSlaveTemplate defaultJnlpAgentTemplate = DockerCloudHelper.fromTemplate("jenkins/jnlp-slave")
defaultJnlpAgentTemplate.with {
    // Default label to "docker", no caching
    // User - jenkins (default)
}

// Custom image for Maven builds
MavenInstallation.DescriptorImpl mavenDescriptor = Jenkins.instance.getDescriptorByType(MavenInstallation.DescriptorImpl.class);
final DockerSlaveTemplate mavenBuilderTemplate = DockerCloudHelper.fromTemplate("onenashev/demo-jenkins-maven-builder")
mavenBuilderTemplate.with {
    labelString = "docker linux mvnBuilder"
    remoteFs = "/root"
    ((DockerComputerJNLPLauncher)launcher).user = "root"
    //TODO: Make volume names configurable
    dockerContainerLifecycle.createContainer.volumes = ["maven-repo:/root/.m2", "jar-cache:/root/.jenkins"]
    nodeProperties = [
        new ToolLocationNodeProperty(
            // Maven from the parent Maven image, we do not want to run the installer each time
            new ToolLocationNodeProperty.ToolLocation(mavenDescriptor,"mvn", "/usr/share/maven")
        )
    ]
}

DockerCloudHelper.setup([defaultJnlpAgentTemplate, mavenBuilderTemplate])
