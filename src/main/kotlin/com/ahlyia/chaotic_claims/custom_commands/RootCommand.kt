package com.ahlyia.chaotic_claims.custom_commands

import com.ahlyia.chaotic_claims.ChaoticClaims
import com.ahlyia.chaotic_claims.gui_items.CurrencyDisplayItem
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object RootCommand {
    fun execute(ctx: CommandContext<CommandSourceStack>, plugin: ChaoticClaims): Int {
        val source = ctx.source
        val player = source.executor as? Player

        if(player != null) {
            println("${player.name} sent root command!")


            val guiBuilder = Gui.normal()
                .setStructure(
                    "---------",
                    "-.....S#-",
                    "-.......-",
                    "-.......-",
                    "-.......-",
                    "-.......-",
                    "---------"
                )
                .addIngredient('#', CurrencyDisplayItem(plugin,Material.GRASS_BLOCK,"Claims",player))
                .addIngredient('S', CurrencyDisplayItem(plugin,Material.SKELETON_SKULL,"Points",player))
                .build()

            val window = Window.single()
                .setViewer(player)
                .setTitle("Chaotic Claims: Home")
                .setGui(guiBuilder)
                .build()

            window.open()
        } else {
            println("Server, or CommandBlock sent root command!")
        }
        return 1
    }
}
