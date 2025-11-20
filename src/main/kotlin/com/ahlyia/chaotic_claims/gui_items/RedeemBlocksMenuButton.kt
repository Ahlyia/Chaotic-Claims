package com.ahlyia.chaotic_claims.gui_items

import com.ahlyia.chaotic_claims.ChaoticClaims
import io.papermc.paper.datacomponent.DataComponentType
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem
import xyz.xenondevs.invui.window.AnvilWindow
import java.text.NumberFormat

class AnvilListener : Listener {
    val plugin = ChaoticClaims.instance

    val key = NamespacedKey(plugin,"claimValueStack")

    @EventHandler(priority = EventPriority.HIGH)
    fun onPrepare(event: PrepareAnvilEvent) {
        val player = event.view.player as? Player

        val inventory: AnvilInventory = event.inventory

        if(event.view.title() == Component.text("Get Claims")) {
            val input = inventory.firstItem ?: return

            val item = input?.type?.key?.asString()
            if(plugin.settings.itemLandValues.keys.any {it == item}) {
                val singletValue = plugin.settings.itemLandValues[item]
                val stackValue = singletValue?.times((input.amount ?: 1)) ?: 0

                val display: ItemStack = inventory.secondItem ?: return // this should never happen LOL

                val displayMeta = display.itemMeta
                displayMeta.lore = mutableListOf(
                    "Singlet: $singletValue",
                    "Stack: $stackValue"
                )
                display.itemMeta = displayMeta

                val result = inventory.result ?: return
                val resultMeta = result.itemMeta

                resultMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, stackValue)
                result.itemMeta = resultMeta
            }
        }
    }
}

class RedeemBlocksConfirm : AbstractItem() {
    override fun getItemProvider(): ItemProvider {
        return ItemBuilder(ItemStack(Material.LIME_STAINED_GLASS_PANE,1)).setDisplayName("Confirm")
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        // do nothing
    }
}

class RedeemBlocksMenuButton : AbstractItem() {
    override fun getItemProvider(): ItemProvider {
        val stack = ItemStack(Material.HOPPER)
        val meta = stack.itemMeta
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        stack.itemMeta = meta

        return ItemBuilder(stack)
            .setDisplayName("Get Claims")
    }

    override fun handleClick(
        clickType: ClickType,
        player: Player,
        event: InventoryClickEvent
    ) {
        var claimsOffered = 0

        val gui = Gui.normal().setStructure(
            "123"
        )
            .addIngredient('3', RedeemBlocksConfirm())
            .addIngredient('2',DisplayItem("Claims",listOf(NumberFormat.getInstance().format(claimsOffered))))


        val window = AnvilWindow.single()
            .setViewer(player)
            .setGui(gui)
            .setTitle("Get Claims")
            .build()

        window.open()
    }

}