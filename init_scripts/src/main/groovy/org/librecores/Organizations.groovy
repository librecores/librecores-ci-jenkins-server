package org.librecores

import org.jenkinsci.plugins.pipeline.Organization

/**
 * Organizations to be hosted on LibreCores CI.
 *
 * @author Oleg Nenashev.
 * @since TODO
 */
class Organizations {

    /**
     * Default organization list for demo purposes
     */
    static def DEFAULT = new ArrayList<Organization>(
        [
     //       new Organization("librecores", "oleg-nenashev",
     //           ["olofk", "imphil", "wallento"], "LibreCores")
        ]
    )
}
