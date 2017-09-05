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
import org.librecores.FuseSoCTools
import org.librecores.PipelineLibrary

println("=== Initialize the Development folder")
if (Jenkins.instance.getItem("Development") != null) {
    println("Development folder has been already initialized, skipping the step")
    return
}


// Admin owns the root Development folder
def folder = Jenkins.instance.createProject(Folder.class, "Development")
FolderOwnershipHelper.setOwnership(folder, new OwnershipDescription(true, "admin"))

// Users get their own sandboxes
def folder2 = folder.createProject(Folder.class, "User")
FolderOwnershipHelper.setOwnership(folder2, new OwnershipDescription(true, "user"))

// Create a library for the local LibreCores Pipeline Library Development
// if the Env Var is set and the directory is mapped
println("==== Initializing local Pipeline development dir")
File file = new File("/var/jenkins_home/pipeline-dev")
if (file.exists() && file.listFiles().length > 0) {
    println("/var/jenkins_home/pipeline-dev is mapped, initializing the directory")
} else {
    println("/var/jenkins_home/pipeline-dev is not mapped, skipping")
    return
}

def pipelineLib = folder.createProject(Folder.class, "PipelineLibrary")
FolderOwnershipHelper.setOwnership(pipelineLib, new OwnershipDescription(true, "user"))

// Add extra Pipeline libs and jobs
ArrayList<LibraryConfiguration> customPipelineLibs = []
def pipelineLibsDir = new File("/var/jenkins_home/pipeline-dev")
if (pipelineLibsDir.exists()) {
    println("==== Scanning the Pipeline Dev library")
    pipelineLibsDir.eachFile (FileType.DIRECTORIES) { directoryPath ->
        if (new File(directoryPath, ".Jenkinslib").exists()) {
            println("===== Adding Pipeline Library ${directoryPath}")
            customPipelineLibs.add(PipelineLibrary.fromFilesystem(directoryPath))
        } else if (new File(directoryPath, "Jenkinsfile").exists()) {
            println("===== Adding Pipeline Job ${directoryPath}")
            WorkflowJob httpClientProject = pipelineLib.createProject(WorkflowJob.class, directoryPath.name)
            httpClientProject.definition = new CpsScmFlowDefinition(
                new FSSCM(directoryPath.absolutePath, false, false, null), "Jenkinsfile")
        }
    }
}

// Add all discovered libs
pipelineLib.addProperty(new FolderLibraries(customPipelineLibs))
