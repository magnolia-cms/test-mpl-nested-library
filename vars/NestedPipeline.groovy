/**
 * Example of using the custom nested pipeline
 */
def call(body) {
  // Init the MPL library
  MPLInit()

  // Executing the pipeline without additional configuration
  pipeline {  // Declarative pipeline
    agent {
      label 'master'
    }
    stages {
      stage( 'Build' ) {
        parallel {        // Parallel build for 2 subprojects
          stage( 'Build Project A' ) {
            steps {
              dir( 'subProjectA' ) {
                MPLModule('Maven Build', [ // Using overriden Maven Build
                  maven: [
                    tool_version: 'Maven 2'
                  ]
                ])
              }
            }
          }
          stage( 'Build Project B' ) {
            steps {
              dir( 'subProjectB' ) {
                // Custom build process (it's better to put it into the project custom module
                sh 'touch file.test'
              }
            }
          }
        }
      }
    }
  }
}
