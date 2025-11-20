package com.ahlyia.chaotic_claims.gui_items

import com.ahlyia.chaotic_claims.ChaoticClaims
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.PagedGui
import xyz.xenondevs.invui.gui.structure.Structure
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ControlItem
import xyz.xenondevs.invui.window.Window
import javax.swing.plaf.basic.BasicMenuItemUI

class ClaimMenuButton : ControlItem<Gui>() {
    val plugin = ChaoticClaims.instance

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        val claimInfoDisplay = """|Start by clicking the \"Start Claim\" button.
            |This will make the first corner 
            |where you are currently standing
            |
            |After that, `type /chaotic confirm`
            |to place the other corner where you
            |are standing. This will complete
            |the claim.
            |
            |You can review your Claim Settings
            |on this page as well.
        """.trimMargin()

        val structure: Structure = Structure(
            "....I....",
            "S.O.R..TC",
            "........."
        )
            .addIngredient('I', DisplayItem("Claim Info",claimInfoDisplay))
            .addIngredient('C', CurrencyDisplayItem(plugin,Material.GRASS_BLOCK,"Claims",player))
            .addIngredient('R', RedeemBlocksMenuButton())

        gui.applyStructure(structure)
    }

    override fun getItemProvider(p0: Gui?): ItemProvider {
        val stack = ItemStack(Material.IRON_SHOVEL)
        val meta = stack.itemMeta
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        stack.itemMeta = meta

        return ItemBuilder(stack)
            .setDisplayName("Claim Menu")
    }
}