import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

import info.magnolia.jenkins.testing.NestedLibTestBase

class BuildTest extends NestedLibTestBase {
  def script = null

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    helper.registerAllowedMethod('fileExists', [String.class], null)
    // helper.registerAllowedMethod('tool', [String.class], { name -> "${name}_HOME" })
    // helper.registerAllowedMethod('withEnv', [List.class, Closure.class], null)

    script = loadScript('MPLModule.groovy')
  }

  @Test
  void testsProjectType() throws Exception {
    // GIVEN

    // WHEN
    script.call('Build')
    printCallStack()

    // THEN
    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'fileExists' }
      .any { c -> c.argsToString().contains('pom.xml') }
    ).isTrue()
    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'fileExists' }
      .any { c -> c.argsToString().contains('package.json') }
    ).isTrue()
    assertJobStatusSuccess()
  }

  @Test
  void runsMavenBuildWhenPomXmlPresent() throws Exception {
    // GIVEN
    helper.registerAllowedMethod('fileExists', [String.class], { file -> file.equals('pom.xml') ? true : false })

    // WHEN
    script.call('Build')
    printCallStack()

    // THEN
    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'echo' }
      .any { c -> c.argsToString().contains('MavenBuild') }
    ).isTrue()
  }

  @Test
  void runsNodeBuildWhenPackageJsonPresent() throws Exception {
    // GIVEN
    helper.registerAllowedMethod('fileExists', [String.class], { file -> file.equals('package.json') ? true : false })

    // WHEN
    script.call('Build')
    printCallStack()

    // THEN
    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'echo' }
      .any { c -> c.argsToString().contains('Node Build') }
    ).isTrue()
  }
}
