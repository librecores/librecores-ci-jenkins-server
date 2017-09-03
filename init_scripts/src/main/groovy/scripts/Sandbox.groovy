// Initializes the Development folder, which is fully configurable by the user

import groovy.io.FileType
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import hudson.plugins.filesystem_scm.FSSCM
import jenkins.model.Jenkins
import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMRetriever
import org.librecores.PipelineLibrary
import org.librecores.Users

println("=== Initialize the Development folder")
if (Jenkins.instance.getItem("Sandbox") != null) {
    println("Development folder has been already initialized, skipping the step")
    return
}


// Admin owns the root Development folder
def folder = Jenkins.instance.createProject(Folder.class, "Sandbox")
FolderOwnershipHelper.setOwnership(folder, new OwnershipDescription(true, "admin"))
PipelineLibrary.forFolder(folder, [PipelineLibrary.LCCI_PIPELINE_LIB])
folder.description = "Directory for prototyping CI for projects"

for (String user : Users.listUsers()) {
    def userFolder = folder.createProject(Folder.class, user)
    FolderOwnershipHelper.setOwnership(userFolder, new OwnershipDescription(true, user))
}