package com.ahlyia.chaotic_claims.gui_items

import com.ahlyia.chaotic_claims.ChaoticClaims
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import java.text.NumberFormat

class TranslatedCurrencyDisplay(val player: Player) : AbstractItem() {
    val plugin = ChaoticClaims.instance
    val key = NamespacedKey(plugin,"Claims")

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        // Do nothing.
    }

    override fun getItemProvider(): ItemProvider {
        val builder = ItemBuilder(ItemStack(Material.DIRT_PATH,1))
        builder.setDisplayName("Claimable Blocks")
        builder.clearLore()

        val claims = player.persistentDataContainer.get(key, PersistentDataType.INTEGER) ?: 0
        val claimsPerBlock = plugin.settings.landCostPerBlock

        val blocks = claims/claimsPerBlock
        val chunks = blocks/256

        """|Blocks: ${NumberFormat.getInstance().format(blocks)}
           |Chunks: ${NumberFormat.getInstance().format(chunks)}
        """.trimMargin().lines().forEach { builder.addLoreLines(it) }

        return builder
    }
}