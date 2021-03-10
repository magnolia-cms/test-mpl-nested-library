/**
 * Overridden MavenBuild module that uses original one from MPL
 */

echo "It's overridden MavenBuild started - let's run the original one"

sh """echo "Let's do ${CFG.'maven.additional_option' ?: 'something important'} here before the maven build" > testfile.txt"""
// sh """sed -i 's/1.0.0-SNAPSHOT/1.2.0-SNAPSHOT/' pom.xml"""

// MPLModule('Maven Build', CFG)

echo "Execution of the overridden MavenBuild is done"
