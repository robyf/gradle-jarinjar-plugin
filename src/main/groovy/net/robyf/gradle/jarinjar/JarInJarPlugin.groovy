package net.robyf.gradle.jarinjar

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.UnknownConfigurationException
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact
import org.gradle.api.internal.java.JavaLibrary
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin

class JarInJarPlugin implements Plugin<Project> {

    void apply(Project project) {
        //project.getPluginManager().apply(JavaPlugin.class);
    
        def extension = project.extensions.create('executableJar', JarInJarPluginExtension)
        try {
            extension.configuration = project.configurations.runtime
        } catch(UnknownConfigurationException e) {
        } catch(MissingPropertyException e) {
        }
        
        project.afterEvaluate {
            project.jar {
                from project.executableJar.configuration
                
                def pluginJarFile = project.buildscript.configurations.classpath.find { it.getName().startsWith('gradle-jarinjar-plugin') && it.getName().endsWith('jar') }
                from project.zipTree(pluginJarFile.getAbsolutePath()).matching {
                    include 'org/**'
                }
                
                manifest {
                    attributes 'Main-Class': 'org.eclipse.jdt.internal.jarinjarloader.JarRsrcLoader'
                    attributes 'Class-Path': '.'
                    attributes 'Rsrc-Class-Path': './ ' + project.executableJar.configuration.collect { it.getName() }.join(' ')
                    attributes 'Rsrc-Main-Class': project.executableJar.mainClass
                }
            }
        }
    }

}
