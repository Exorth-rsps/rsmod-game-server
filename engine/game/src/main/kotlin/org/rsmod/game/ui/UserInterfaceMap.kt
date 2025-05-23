package org.rsmod.game.ui

import it.unimi.dsi.fastutil.ints.Int2IntMap
import it.unimi.dsi.fastutil.ints.IntArraySet
import org.rsmod.game.type.comp.ComponentType
import org.rsmod.game.type.interf.IfEvent
import org.rsmod.game.type.interf.InterfaceType
import org.rsmod.game.ui.collection.ComponentEventMap
import org.rsmod.game.ui.collection.ComponentTargetMap
import org.rsmod.game.ui.collection.ComponentTranslationMap
import org.rsmod.game.ui.collection.UserInterfaceSet

public data class UserInterfaceMap(
    public val topLevels: UserInterfaceSet = UserInterfaceSet(),
    public val overlays: ComponentTargetMap = ComponentTargetMap(),
    public val modals: ComponentTargetMap = ComponentTargetMap(),
    public val events: ComponentEventMap = ComponentEventMap(),
    public val gameframe: ComponentTranslationMap = ComponentTranslationMap(),
    public val closeQueue: IntArraySet = IntArraySet(),
    public var closeModal: Boolean = false,
) {
    public fun queueClose(target: Component) {
        closeQueue.add(target.packed)
    }

    public fun removeQueuedCloseSub(target: ComponentType) {
        closeQueue.remove(target.packed)
    }

    public operator fun contains(type: InterfaceType): Boolean {
        return containsModal(type) ||
            containsOverlay(type) ||
            containsTopLevel(type) ||
            containsGameframe(type)
    }

    public fun containsTopLevel(topLevel: InterfaceType): Boolean =
        topLevels.backing.contains(topLevel.id)

    public fun containsOverlay(overlay: InterfaceType): Boolean =
        overlays.backing.containsValue(overlay.id)

    public fun containsModal(modal: InterfaceType): Boolean = modals.backing.containsValue(modal.id)

    public fun containsGameframe(type: InterfaceType): Boolean =
        gameframe.backing.containsValue(type.id)

    public fun getOverlay(key: ComponentType): Component = overlays.backing.get(key)

    public fun getOverlayOrNull(key: ComponentType): Component? = getOverlay(key).orNull()

    public fun getModal(key: ComponentType): Component = modals.backing.get(key)

    public fun getModalOrNull(key: ComponentType): Component? = getModal(key).orNull()

    public fun getGameframe(key: ComponentType): Component = gameframe.backing.get(key)

    public fun getGameframeOrNull(key: ComponentType): Component? = getGameframe(key).orNull()

    public fun hasEvent(component: ComponentType, slot: Int, event: IfEvent): Boolean {
        val events = events[component, slot]
        return (events and event.bitmask) != 0
    }

    private fun Component.orNull(): Component? = if (this == Component.NULL) null else this

    private fun Int2IntMap.get(key: ComponentType): Component {
        val packed = getOrDefault(key.packed, null) ?: return Component.NULL
        return Component(packed)
    }
}
