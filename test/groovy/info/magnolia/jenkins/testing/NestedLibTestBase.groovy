package info.magnolia.jenkins.testing

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.LocalSource.localSource

import com.griddynamics.devops.mpl.testing.MPLTestBase
import com.griddynamics.devops.mpl.MPLManager

abstract class NestedLibTestBase extends MPLTestBase {
  void setUp() throws Exception {
    def shared_libs = this.class.getResource('.').getFile()

    // Connecting the libraries
    helper.registerSharedLibrary(library()
        .name('nestedlib')
        .allowOverride(false)
        .retriever(localSource(shared_libs))
        .targetPath(shared_libs)
        .defaultVersion('snapshot')
        .implicit(true)
        .build()
    )
    helper.registerSharedLibrary(library()
        .name('mpl')
        .allowOverride(false)
        .retriever(localSource(shared_libs))
        .targetPath(shared_libs)
        .defaultVersion('snapshot')
        .implicit(true)
        .build()
    )

    setScriptRoots([ 'vars', shared_libs + '/mpl@snapshot/vars' ] as String[])
    setScriptExtension('groovy')

    super.setUp()

    binding.setVariable('env', [:])

    // Here you can put some common allowed methods for all the tests

    MPLManager.instance.addModulesLoadPath('com/company/nestedlib')
  }
}
