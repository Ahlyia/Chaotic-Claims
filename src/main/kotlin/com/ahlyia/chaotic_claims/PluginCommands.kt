package com.ahlyia.chaotic_claims

import com.ahlyia.chaotic_claims.custom_commands.RootCommand
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

class PluginCommands(private val plugin: ChaoticClaims){

    fun rootCommand(ctx: CommandContext<CommandSourceStack>) = RootCommand.execute(ctx,plugin) // an example of my beautiful compartmentalization

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