// Initializes the production jobs directory, which runs with deployed scripts and repos

import com.cloudbees.hudson.plugins.folder.Folder
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import jenkins.model.Jenkins
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.librecores.Organizations
import org.librecores.PipelineLibrary

println("=== Initialize the Production folder")
if (Jenkins.instance.getItem("Production") != null) {
    println("Production folder has been already initialized, skipping the step")
    return
}

def folder = Jenkins.instance.createProject(Folder.class, "Production")
PipelineLibrary.forFolder(folder, [PipelineLibrary.LCCI_PIPELINE_LIB])
FolderOwnershipHelper.setOwnership(folder, new OwnershipDescription(true, "admin"))
folder.description = "Production instance management"

// TODO: Add loading of organizations from external source, e.g. LibreCores Web
for(def org : Organizations.DEFAULT) {
    org.toOrganizationFolder(folder)
}

