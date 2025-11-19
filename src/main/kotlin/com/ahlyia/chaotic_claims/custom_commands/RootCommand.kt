package com.ahlyia.chaotic_claims.custom_commands

import com.ahlyia.chaotic_claims.ChaoticClaims
import com.ahlyia.chaotic_claims.PluginGUIs
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object RootCommand {
    fun execute(ctx: CommandContext<CommandSourceStack>, plugin: ChaoticClaims): Int {
        val source = ctx.source
        val player = source.executor as? Player

        if(player != null) {
            println("${player.name} sent root command!")

            plugin.pluginGUIs.reset()

            val inventory: Inventory = Bukkit.createInventory(player, 9 * 5, Component.text("Chaotic Claims"))

            val pageTestStack: ItemStack = ItemStack(Material.GOLD_NUGGET,1)

            plugin.pluginGUIs.registerButton(pageTestStack) { event ->
                player.sendMessage("You clicked the button!! Hooray.")
                1
            }

            inventory.setItem(8,pageTestStack)

            player.openInventory(inventory)
        } else {
            println("Server, or CommandBlock sent root command!")
        }
        return 1
    }
}
