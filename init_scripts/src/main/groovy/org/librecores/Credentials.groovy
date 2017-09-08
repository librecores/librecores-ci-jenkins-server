package org.librecores

import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import jenkins.model.Jenkins

/**
 * Provides common tools for credentials management.
 * @author Oleg Nenashev.
 */
class Credentials {

    static final defaultGitHubID = "github-credentials"

    static void load(String id, File credentialsSource) {
        def props = new Properties()
        credentialsSource.withReader { rdr ->
            props.load(rdr)
        }
        def c = new UsernamePasswordCredentialsImpl(
            CredentialsScope.GLOBAL,
            id,
            "Default credentials for accessing GitHub",
            props.getProperty("username"),
            props.getProperty("password")
        )

        CredentialsProvider.lookupStores(Jenkins.instance).each { it ->
            it.addCredentials(Domain.global(), c)
        }

    }

    static void loadIfExists(String id, File directory, String child) {
        File path = new File(directory, child)
        if (path.exists() && path.isFile()) {
            println "Loading credentials ${id} from ${path.absolutePath}"
            load(id, path)
        } else {
            println "Skipping credentials ${id}, ${path.absolutePath} is missing"
        }
    }

}
