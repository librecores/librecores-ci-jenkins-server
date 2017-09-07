package org.librecores

import com.cloudbees.hudson.plugins.folder.Folder
import jenkins.branch.BranchSource
import jenkins.branch.MultiBranchProject
import org.jenkinsci.plugins.github_branch_source.GitHubSCMSource
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject

import java.lang.reflect.Field

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

    static WorkflowMultiBranchProject createMultiBranchJob(Folder folder, String repo,
            String organization = "librecores", String name = null, String credentialsId = Credentials.defaultGitHubID) {
        def jobName = name ?: repo
        WorkflowMultiBranchProject job = folder.createProject(WorkflowMultiBranchProject.class, "${jobName}")

        // We do not grant checkout just in case repo owners grants wrong access
        BranchSource bs = new BranchSource(new GitHubSCMSource("github", null, null, credentialsId, organization, repo))
        def fields = MultiBranchProject.class.declaredFields
        Field myField
        for (def field : fields) {
            if (field.name.equals("sources")) {
                myField = field
            }
        }
        myField.accessible = true
        myField.get(job).replaceBy([bs])

        return job
    }

}
