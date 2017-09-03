package org.librecores

import com.cloudbees.hudson.plugins.folder.Folder
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

/**
 * @author Oleg Nenashev
 */
enum PipelineLibrary {
    LCCI_PIPELINE_LIB("librecores-pipeline-lib");

    final String id
    final String organization

    PipelineLibrary(String id, String organization = "librecores") {
        this.id = id
        this.organization = organization
    }

    public LibraryConfiguration toConfig() {
        def pipelineLibrarySource = new GitSCMSource(id, "https://github.com/${organization}/${id}.git", null, null, null, false)
        LibraryConfiguration lc = new LibraryConfiguration(id, new SCMSourceRetriever(pipelineLibrarySource))
        lc.with {
            implicit = true
            defaultVersion = "master"
        }
        return lc
    }

    public static forFolder(Folder folder, ArrayList<PipelineLibrary> libs) {
        ArrayList<LibraryConfiguration> configurations = []
        for (def lib : libs) {
            configurations.add(lib.toConfig())
        }

        folder.addProperty(new FolderLibraries(configurations))
    }


}
