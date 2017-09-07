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
import org.librecores.FuseSoCTools
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

FuseSoCTools.createMultiBranchJob(folder, "lowrisc-chip", "wallento")
FuseSoCTools.createMultiBranchJob(folder, "cocotb-example-endian_swapper", "oleg-nenashev")
FuseSoCTools.createMultiBranchJob(folder, "baremetal-apps", "optimsoc", "OpTiMSoC")
FuseSoCTools.createMultiBranchJob(folder, "picorv32", "oleg-nenashev", "PicoRV32")
FuseSoCTools.createMultiBranchJob(folder, "fusesoc", "oleg-nenashev")
FuseSoCTools.createMultiBranchJob(folder, "wb_sdram_ctrl", "oleg-nenashev")
