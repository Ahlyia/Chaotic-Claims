package com.ahlyia.chaotic_claims

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import com.ahlyia.chaotic_claims.ChaoticClaims
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory


class PluginCommands(private val plugin: ChaoticClaims){

    fun rootCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        val player = source.executor as? Player

        if(player != null) {
            println("${player.name} sent root command!")

            val inventory: Inventory = Bukkit.createInventory(player, 9 * 7, Component.text("Chaotic Claims"))



        } else {
            println("Server, or CommandBlock sent root command!")
        }
        return 1
    }
    fun hooktestCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        val player = source?.executor as? Player

        if(plugin.settings.discordWebhook != "") {
            DiscordWebhookClient.sendWebhook(plugin.settings.discordWebhook,"Hook test! Yargh!\nSent by `${player?.name ?: "SERVER"}`")
            return 1
        } else {
            println("No webhook set!")

            player?.sendMessage("No webhook is currently set for this server!")
            return 0
        }
    }
}