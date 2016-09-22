node {
    stage("Checkout") {
        checkout scm
    }

    stage("Clean") {
        sh "./gradlew --info clean"
    }

    stage("Build") {
        sh "./gradlew --info build"
    }
    
    stage("Publish reports") {
        publishUnitTestResults()
    }
    
    stage("Upload archives") {
        sh "./gradlew --info uploadArchives"
    }
    
    if (env.BRANCH_NAME == "master") {
        stage("Publish plugin") {
            sh "./gradlew --info bintrayUpload publishPlugins"
        }
    }
}

def publishUnitTestResults() {
    step([$class: "JUnitResultArchiver", testResults: "build/**/TEST-*.xml"])
}
