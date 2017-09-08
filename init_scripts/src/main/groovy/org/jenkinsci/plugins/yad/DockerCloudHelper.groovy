package org.jenkinsci.plugins.yad

import com.github.kostyasha.yad.DockerCloud
import com.github.kostyasha.yad.DockerConnector
import com.github.kostyasha.yad.DockerContainerLifecycle
import com.github.kostyasha.yad.DockerSlaveTemplate
import com.github.kostyasha.yad.commons.DockerCreateContainer
import com.github.kostyasha.yad.commons.DockerImagePullStrategy
import com.github.kostyasha.yad.commons.DockerPullImage
import com.github.kostyasha.yad.commons.DockerRemoveContainer
import com.github.kostyasha.yad.commons.DockerStopContainer
import com.github.kostyasha.yad.launcher.DockerComputerJNLPLauncher
import com.github.kostyasha.yad.strategy.DockerOnceRetentionStrategy
import hudson.model.Node
import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration
import org.jenkinsci.core.JenkinsHelper

/**
 * Helper for Yet Another Docker Plugin
 * @author Oleg Nenashev.
 * @since TODO
 */
class DockerCloudHelper {

    /**
     * Initializes a Docker Cloud from the specified templates.
     * @param templates List of templates
     * @return Created Cloud instance, which is added to Jenkins
     */
    static DockerCloud setup(List<DockerSlaveTemplate> templates) {
        def cloud = new DockerCloud(
            "docker-cloud",
            templates,
            10,
            //TODO: YAD Plugin does not work well with this image and Unix sockets. Would be useful to migrate
            new DockerConnector("tcp://${JenkinsHelper.hostname}:2376"))

        Jenkins.instance.clouds.add(cloud)
        return cloud
    }

    static DockerSlaveTemplate fromTemplate(String image) {
        return new DockerSlaveTemplate(
            maxCapacity: 10,
            mode: Node.Mode.EXCLUSIVE,
            numExecutors: 1,
            launcher: new DockerComputerJNLPLauncher(
                jenkinsUrl: JenkinsLocationConfiguration.get().url,
                launchTimeout: 100,
                noCertificateCheck: true
            ),
            dockerContainerLifecycle: new DockerContainerLifecycle(
                image: image,
                pullImage: new DockerPullImage(
                    pullStrategy: DockerImagePullStrategy.PULL_ONCE
                ),
                createContainer: new DockerCreateContainer(
                    privileged: false,
                    tty: false
                ),
                stopContainer: new DockerStopContainer(
                    timeout: 100
                ),
                removeContainer: new DockerRemoveContainer(
                    force: true,
                    removeVolumes: true
                )
            ),
            retentionStrategy: new DockerOnceRetentionStrategy(30)
        )
    }

}
