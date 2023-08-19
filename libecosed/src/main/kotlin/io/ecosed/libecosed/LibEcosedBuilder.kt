package io.ecosed.libecosed

import io.ecosed.libecosed.plugin.LibEcosedPlugin
import io.ecosed.plugin.LibEcosed

object LibEcosedBuilder : LibEcosedImpl {

    override val mLibEcosed: LibEcosed
        get() = LibEcosedPlugin()
}