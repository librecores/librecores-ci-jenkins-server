package org.jenkinsci.plugins.pipeline

import com.cloudbees.hudson.plugins.folder.Folder
import com.synopsys.arc.jenkins.plugins.ownership.OwnershipDescription
import jenkins.branch.OrganizationFolder
import jenkins.scm.api.SCMNavigator
import org.jenkinsci.plugins.github_branch_source.GitHubSCMNavigator
import org.jenkinsci.plugins.ownership.model.folders.FolderOwnershipHelper

import javax.annotation.CheckForNull

/**
 * Represents the LibreCores organization.
 * Each organization has its own set of owners and a list of Pipeline libraries to be included.
 *
 * @author Oleg Nenashev.
 * @see OrganizationType
 */
class Organization {

    String id
    String displayName
    String primaryOwner
    Set<String> secondaryOwners
    @CheckForNull String url
    OrganizationType type

    Organization(String id, String primaryOwner, Iterable<String> secondaryOwners = null, String displayName = null,
                 String url = null, OrganizationType type = OrganizationType.GITHUB) {
        this.id = id
        this.primaryOwner = primaryOwner
        this.displayName = displayName ?: id
        this.secondaryOwners = new HashSet<>()
        if (secondaryOwners != null) {
            this.secondaryOwners.addAll(secondaryOwners)
        }
        this.type = type
    }

    OrganizationFolder toOrganizationFolder(Folder parent) {
        def orgFolder = parent.createProject(OrganizationFolder.class, id)
        orgFolder.displayName = this.displayName
        orgFolder.description = "Organization folder for ${displayName}"
        def d = new OwnershipDescription(true, primaryOwner, secondaryOwners)
        FolderOwnershipHelper.setOwnership(orgFolder, d)

        //TODO: Here we call APIs which are restricted in Java. It may fall apart after an update
        SCMNavigator nav = createNavigator()
        orgFolder.navigators.add(nav)

        return orgFolder
        //TODO: Add sidebar link to the org
    }

    private SCMNavigator createNavigator() {
        switch (type) {
            case OrganizationType.GITHUB:
                return new GitHubSCMNavigator(null, id, null, null)
            default:
                throw new IllegalArgumentException("Unsupported SCM type: ${type}")
        }
    }
}
