package net.robyf.gradle.jarinjar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.UnknownConfigurationException
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.java.JavaLibrary
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginAware
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * The JarInJar plugin.
 *
 * @author Roberto Fasciolo
 */
class JarInJarPlugin implements Plugin<PluginAware> {
 
    def void apply(PluginAware pluginAware) {
        if (pluginAware instanceof Project) {
            doApply(pluginAware)
        } else if (pluginAware instanceof Settings) {
            pluginAware.gradle.allprojects { p ->
                p.plugins.apply(JarInJarPlugin)
            }
        } else if (pluginAware instanceof Gradle) {
            pluginAware.allprojects { p ->
                p.plugins.apply(JarInJarPlugin)
            }
        } else {
            throw new IllegalArgumentException("${pluginAware.getClass()} is currently not supported as an apply target, please report if you need it")
        }
    }

    /**
     * Apply the plugin to a project.
     */
    void doApply(Project project) {
        def extension = project.extensions.create('executableJar', JarInJarPluginExtension)

        try {
            project.configurations.getByName("runtime")
        } catch(UnknownConfigurationException e) {
            project.configurations.create("runtime")
        }

        extension.configuration = project.configurations.runtime
        
        project.afterEvaluate {
            project.jar {
                from project.extensions.executableJar.configuration
                
                def pluginJarFile = project.buildscript.configurations.classpath.find { it.getName().startsWith('gradle-jarinjar-plugin') && it.getName().endsWith('jar') }
                from project.zipTree(pluginJarFile.getAbsolutePath()).matching {
                    include 'org/**'
                }
                
                manifest {
                    attributes 'Main-Class': 'org.eclipse.jdt.internal.jarinjarloader.JarRsrcLoader'
                    attributes 'Class-Path': '.'
                    attributes 'Rsrc-Class-Path': './ ' + project.extensions.executableJar.configuration.collect { it.getName() }.join(' ')
                    attributes 'Rsrc-Main-Class': project.extensions.executableJar.mainClass
                }
            }
        }
    }

}
