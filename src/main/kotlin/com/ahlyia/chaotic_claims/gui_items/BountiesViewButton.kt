package com.ahlyia.chaotic_claims.gui_items

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class BountiesViewButton() : AbstractItem() {
    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
       player.sendMessage("NOT IMPLEMENTED YET!")
    }

    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(ItemStack(Material.PLAYER_HEAD,1)).setDisplayName("Bounties")
    }
}