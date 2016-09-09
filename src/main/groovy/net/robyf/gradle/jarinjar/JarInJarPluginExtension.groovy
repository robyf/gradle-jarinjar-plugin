package net.robyf.gradle.jarinjar

import org.gradle.api.artifacts.Configuration

/**
 * Extension class for configuring the JarInJar plugin.
 *
 * @author Roberto Fasciolo
 */
class JarInJarPluginExtension {

    /**
     * Fully qualified name of the class to be run
     */
    def String mainClass
    
    /**
     * Configuration including all the needed runtime jars. If the java plugin is used by default
     * it uses the project's runtime configuration.
     */
    def Configuration configuration

}
