package com.ahlyia.chaotic_claims.gui_items

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import xyz.xenondevs.invui.item.impl.SimpleItem

class DisplayItem(val title: String, val loreLines: List<String>) : AbstractItem() {
    override fun getItemProvider(): ItemProvider {
        val stack = ItemStack(Material.PAPER,1)
        val meta = stack.itemMeta
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        stack.itemMeta = meta

        val build = ItemBuilder(stack).setDisplayName(title)
        build.clearLore()

        loreLines.forEach { build.addLoreLines(it) }

        return build
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        // do nothing
    }
}