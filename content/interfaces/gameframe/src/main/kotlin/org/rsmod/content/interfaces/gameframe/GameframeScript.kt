package org.rsmod.content.interfaces.gameframe

import jakarta.inject.Inject
import org.rsmod.api.script.onPlayerInit
import org.rsmod.content.interfaces.gameframe.impl.SidePanelsResizablePane
import org.rsmod.content.interfaces.gameframe.util.openGameframe
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class GameframeScript
@Inject
constructor(private val sidePanelsResizable: SidePanelsResizablePane) : PluginScript() {
    override fun ScriptContext.startup() {
        onPlayerInit { player.openGameframe(sidePanelsResizable, eventBus) }
    }
}
