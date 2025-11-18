package com.ahlyia.chaotic_claims

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.context.CommandContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.Dictionary

@Serializable
data class PluginSettings(
    val discordWebhook: String = "",
    val landCostPerBlock: Int = 12,
    val freeClaim: Int = 768, // four chunks
    val bannedItems: List<String> = emptyList(),
    val itemCosts: Map<String, Int> = mapOf( // Item cost to redeem from Bounty Points
        "minecraft:diamond" to 15,
        "minecraft:emerald" to 10,
        "minecraft:netherite_ingot" to 1000
    ),
    val itemLandValues: Map<String, Int> = mapOf(
        "minecraft:diamond" to 12, // 16 diamonds for a chunk, one block per diamond.
        "minecraft:emerald" to 8, // 3/4 block per emerald
        "minecraft:iron_ingot" to 3,
        "minecraft:gold_ingot" to 3,
        "minecraft:copper_ingot" to 1
    )
)

object JsonSettingsManager {
    private val json = Json { prettyPrint = true; encodeDefaults = true }

    fun loadSettings(file: File): PluginSettings {
        return if(file.exists()) {
            try{
                val text = file.readText()
                json.decodeFromString<PluginSettings>(text)
            } catch (e: Exception) {
                println("Failed to read Settings! ;( made Defaults. ${e.message}")
                PluginSettings()
            }
        } else {
            val defaultSettings = PluginSettings()
            saveSettings(file, defaultSettings)
            defaultSettings
        }
    }

    fun saveSettings(file: File, settings: PluginSettings){
        try{
            file.writeText(json.encodeToString(settings))
        } catch (e: Exception) {
            println("Failed write Settings! ;( ${e.message}")
        }
    }
}

@Serializable
data class DiscordWebhookMessage(
    val content: String
)

object DiscordWebhookClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    fun sendWebhook(webhookUrl: String, message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                client.post(webhookUrl) {
                    contentType(ContentType.Application.Json)
                    setBody(DiscordWebhookMessage(message))
                }
            } catch (e: Exception){
                println("Failed to send discord webhook: ${e.message}")
            }
        }
    }
}

class ChaoticClaims : JavaPlugin() {

    lateinit var settings: PluginSettings
    private val settingsFile by lazy { File(dataFolder,"settings.json") }

    private fun rootCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        val sender = source.sender as? Player

        if(sender != null) {
            println("${sender.name} sent root command!")
        } else {
            println("Server, or CommandBlock sent root command!")
        }
        return 1
    }
    private fun hooktestCommand(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        val player = source?.executor as? Player

        if(settings.discordWebhook != "") {
            DiscordWebhookClient.sendWebhook(settings.discordWebhook,"Hook test! Yargh!\nSent by `${player?.name ?: "SERVER"}`")
            return 1
        } else {
            println("No webhook set!")

            player?.sendMessage("No webhook is currently set for this server!")
            return 0
        }
    }

    override fun onEnable() {
        if(!dataFolder.exists()) dataFolder.mkdir()

        settings = JsonSettingsManager.loadSettings(settingsFile)
        println("Discord Webhook ist: ${settings.discordWebhook}")

        this.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {commands ->
            val hooktest = Commands.literal("hooktest")
            val commandRoot = Commands.literal("chaotic")

            commandRoot.then(hooktest).executes{ctx -> hooktestCommand(ctx) }
            commandRoot.executes{ctx -> rootCommand(ctx)}

            val commandRootBuild = commandRoot.build()

            commands.registrar().register(commandRootBuild)
        }

        println("Hello from Chaotic Claims!")
    }

    override fun onDisable() {
        println("GOODBYE!! From Chaotic Claims!")
        JsonSettingsManager.saveSettings(settingsFile,settings)
    }
}
