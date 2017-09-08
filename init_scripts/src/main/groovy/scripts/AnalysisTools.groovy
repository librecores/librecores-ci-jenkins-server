import groovy.io.FileType
import hudson.plugins.logparser.LogParserPublisher
import hudson.plugins.logparser.ParserRuleFile
import jenkins.model.Jenkins
import org.apache.commons.io.FilenameUtils

println("=== Setting up analysis tools")

println("==== Log Parsers")
File parsersDir = new File(Jenkins.instance.rootDir, "userContent/config/logParsers")

List<ParserRuleFile> rules = []
parsersDir.eachFile (FileType.FILES) { parserFile ->
    def name = FilenameUtils.getBaseName(parserFile.name)
    def isDefault = name.equals("default")
    println("Creating parser: ${name} (default=${isDefault})")
    rules.add(new ParserRuleFile(name, parserFile.absolutePath))
}

def descriptor = (LogParserPublisher.DescriptorImpl)Jenkins.instance.getDescriptor(LogParserPublisher.class)
descriptor.@parsingRulesGlobal = rules.toArray(new ParserRuleFile[rules.size()])
