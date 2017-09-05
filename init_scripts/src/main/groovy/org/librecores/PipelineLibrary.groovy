package org.librecores

import com.cloudbees.hudson.plugins.folder.Folder
import hudson.plugins.filesystem_scm.FSSCM
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.FolderLibraries
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMRetriever
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

    LibraryConfiguration fromGitHub() {
        def pipelineLibrarySource = new GitSCMSource(id, "https://github.com/${organization}/${id}.git", null, null, null, false)
        LibraryConfiguration lc = new LibraryConfiguration(id, new SCMSourceRetriever(pipelineLibrarySource))
        lc.with {
            implicit = true
            defaultVersion = "master"
        }
        return lc
    }

    static LibraryConfiguration fromFilesystem(String libPath, String id = null) {
        return fromFilesystem(new File(libPath), id)
    }

    static LibraryConfiguration fromFilesystem(File libPath, String id = null) {
        def libID = id ?: libPath.name
        def scm = new FSSCM(libPath.absolutePath, false, false, null)
        LibraryConfiguration config = new LibraryConfiguration(libID, new SCMRetriever(scm))
        config.with {
            implicit = true
            defaultVersion = "master"
        }
        return config
    }

    public static forFolder(Folder folder, ArrayList<PipelineLibrary> libs) {
        ArrayList<LibraryConfiguration> configurations = []
        for (def lib : libs) {
            configurations.add(lib.fromGitHub())
        }

        folder.addProperty(new FolderLibraries(configurations))
    }


}
