import org.junit.Before
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

import com.company.nestedlib.testing.NestedLibTestBase

class BuildTest extends NestedLibTestBase {
  def script = null

  @Override
  @Before
  void setUp() throws Exception {
    super.setUp()

    helper.registerAllowedMethod('fileExists', [String.class], null)
    helper.registerAllowedMethod('tool', [String.class], { name -> "${name}_HOME" })
    helper.registerAllowedMethod('withEnv', [List.class, Closure.class], null)

    script = loadScript('MPLModule.groovy')
  }

  @Test
  void default_run() throws Exception {
    script.call('Build')
    printCallStack()

    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'echo' }
      .any { c -> c.argsToString().contains('''It's overridden MavenBuild started''') }
    ).isTrue()

    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'sh' }
      .any { c -> c.argsToString().contains('''Let's do something important''') }
    ).isTrue()

    assertJobStatusSuccess()
  }

  @Test
  void change_tool_and_option() throws Exception {
    script.call('Build', [
      maven: [
        tool_version: 'Maven 2',
        additional_option: 'Loooool'
      ]
    ])
    printCallStack()

    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'tool' }
      .any { c -> c.argsToString().contains('Maven 2') }
    ).isTrue()

    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'sh' }
      .any { c -> c.argsToString().contains('''Let's do Loooool''') }
    ).isTrue()

    assertThat(helper.callStack
      .findAll { c -> c.methodName == 'sh' }
      .any { c -> c.argsToString().contains('''Let's do something important''') }
    ).isFalse()

    assertJobStatusSuccess()
  }
}
