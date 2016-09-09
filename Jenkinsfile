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
}

def publishUnitTestResults() {
    step([$class: "JUnitResultArchiver", testResults: "build/**/TEST-*.xml"])
}
