package com.ahlyia.chaotic_claims.gui_items

import com.ahlyia.chaotic_claims.ChaoticClaims
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.text.NumberFormat


open class CurrencyDisplayItem(val plugin: ChaoticClaims, val itemMaterial: Material, val title: String, val tracking: Player) : AbstractItem() {

    val key = NamespacedKey(plugin,title)

    override fun handleClick(clickType: ClickType, player: Player, clickEvent: InventoryClickEvent) {
        var dataRead = player.persistentDataContainer.get(key,PersistentDataType.INTEGER) ?: 0
        dataRead += 1

        player.persistentDataContainer.set(key, PersistentDataType.INTEGER,dataRead)
        notifyWindows()
    }

    override fun getItemProvider(): ItemProvider {
        val dataRead = tracking.persistentDataContainer.get(key, PersistentDataType.INTEGER) ?: 0

        return ItemBuilder(itemMaterial).setDisplayName(title).addLoreLines(NumberFormat.getInstance().format(dataRead))
    }
}