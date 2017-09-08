import jenkins.model.JenkinsLocationConfiguration
import org.jenkinsci.core.JenkinsHelper

//TODO: Ensure this class loads properly even without a weird name

println("== Setting Jenkins URL")
JenkinsLocationConfiguration.get().setUrl(JenkinsHelper.url)

println("== Setting up the default security configuration")
// TODO: ideally Jenkins should support a start flag for that
JenkinsHelper.setupDefaultSecurity()
