import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration
import hudson.tasks.Mailer
import hudson.plugins.locale.PluginImpl

// TODO: Configure Job Restrictions, Script Security, Authorize Project, etc., etc.

println("--- Configuring Quiet Period")
// We do not wait for anything
Jenkins.instance.quietPeriod = 0

println("--- Configuring Email global settings")
JenkinsLocationConfiguration.get().adminAddress = "admin@non.existent.email"
Mailer.descriptor().defaultSuffix = "@non.existent.email"

println("--- Configuring Locale")
//TODO: Create ticket to get better API
PluginImpl localePlugin = (PluginImpl)Jenkins.instance.getPlugin("locale")
localePlugin.systemLocale = "en_US"
localePlugin.@ignoreAcceptLanguage=true
