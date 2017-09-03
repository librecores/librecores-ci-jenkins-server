package org.librecores

import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

/**
 * @author Oleg Nenashev
 * @since TODO
 */
class FuseSoCTools {

    /**
     * Creates a simple Job for FuseSoC.
     * The method presumes that the {@link PipelineLibrary#LCCI_PIPELINE_LIB} is installed by default.
     *
     * @param folder Folder
     * @param repo Repo
     * @param organization Organization of the project
     * @param nameSuffix Extra suffix
     * @param args Extra arguments to be passed to the library
     * @return Created job
     */
    static WorkflowJob createFuseSoCJob(Folder folder, String repo, String organization = "librecores", String nameSuffix = "", String args = null) {
        WorkflowJob job = folder.createProject(WorkflowJob.class, "${repo}${nameSuffix}")
        String extras = args == null ? "" : ", $args"
        job.definition = new CpsFlowDefinition(
            "buildPlugin(platforms: ['linux'], repo: 'https://github.com/${organization}/${repo}.git' ${extras})", true
        )
        return job;
    }

}
