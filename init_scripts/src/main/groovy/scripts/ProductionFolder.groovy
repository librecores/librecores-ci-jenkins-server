// Initializes the production jobs directory, which runs with deployed scripts and repos

import com.cloudbees.hudson.plugins.folder.Folder
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import com.synopsys.arc.jenkins.plugins.ownership.jobs.JobOwnerHelper
import hudson.plugins.git.GitSCM
import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.librecores.PipelineLibrary

println("=== Initialize the Production folder")
if (Jenkins.instance.getItem("Production") != null) {
    println("Production folder has been already initialized, skipping the step")
    return
}

def folder = Jenkins.instance.createProject(Folder.class, "Production")
PipelineLibrary.forFolder(folder, [PipelineLibrary.LCCI_PIPELINE_LIB])
FolderOwnershipHelper.setOwnership(folder, new OwnershipDescription(true, "admin"))

// Add a sample project
WorkflowJob project1 = folder.createProject(WorkflowJob.class, "Ownership_Plugin_Agent")
project1.setDefinition(new CpsFlowDefinition("buildPlugin(platforms: ['linux'], repo: 'https://github.com/jenkinsci/ownership-plugin.git')", true))
JobOwnerHelper.setOwnership(project1, new OwnershipDescription(true, "admin", Arrays.asList("user")))

// TODO: Add Multi-Branch project, which does not build with Windows
