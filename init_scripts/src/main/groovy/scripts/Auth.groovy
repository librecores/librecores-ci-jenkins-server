import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins
import jenkins.security.QueueItemAuthenticatorConfiguration
import hudson.model.*
import org.jenkinsci.plugins.authorizeproject.GlobalQueueItemAuthenticator
import org.jenkinsci.plugins.authorizeproject.strategy.TriggeringUsersAuthorizationStrategy
import org.librecores.Users


boolean createAdmin = Boolean.getBoolean("io.jenkins.dev.security.createAdmin")

println("=== Installing the Security Realm")
def securityRealm = new HudsonPrivateSecurityRealm(false)

for (def user : Users.listUsers()) {
    // Mocks credentials
    User instance = securityRealm.createAccount(user, user)
    instance.setFullName("${user} (test user)")
}

if (createAdmin) {
    User admin = securityRealm.createAccount("admin", "admin")
    admin.setFullName("Admin")
}
Jenkins.instance.setSecurityRealm(securityRealm)

println("=== Configure Authorize Project")
GlobalQueueItemAuthenticator auth = new GlobalQueueItemAuthenticator(
    new TriggeringUsersAuthorizationStrategy()
)
QueueItemAuthenticatorConfiguration.get().authenticators.add(auth)
