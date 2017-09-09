// Initializes the Development folder, which is fully configurable by the user

import groovy.io.FileType
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import jenkins.model.Jenkins
import com.cloudbees.hudson.plugins.folder.Folder
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper
import org.librecores.Credentials
import org.librecores.FuseSoCTools
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
folder.description = "Directory for prototyping CI flows"

// Create sample projects for users
def userFolders = new HashMap<String, Folder>()
for (String user : Users.listUsers()) {
    def userFolder = folder.createProject(Folder.class, user)
    FolderOwnershipHelper.setOwnership(userFolder, new OwnershipDescription(true, user))
    userFolder.description = "Folder of user ${user}"
    userFolders.put(user, userFolder)
}

// Sample projects
FuseSoCTools.createMultiBranchJob(userFolders["oleg-nenashev"], "cocotb-example-endian_swapper", "oleg-nenashev")
FuseSoCTools.createMultiBranchJob(userFolders["oleg-nenashev"], "picorv32", "oleg-nenashev", "PicoRV32")
FuseSoCTools.createMultiBranchJob(userFolders["oleg-nenashev"], "fusesoc", "oleg-nenashev")
FuseSoCTools.createMultiBranchJob(userFolders["oleg-nenashev"], "wb_sdram_ctrl", "oleg-nenashev", "wb_sdram_ctrl", Credentials.defaultGitHubID, true)
FuseSoCTools.createMultiBranchJob(userFolders["wallento"], "lowrisc-chip", "wallento")
FuseSoCTools.createMultiBranchJob(userFolders["wallento"], "baremetal-apps", "optimsoc", "OpTiMSoC")
