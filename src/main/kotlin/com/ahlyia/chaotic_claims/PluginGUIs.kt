package com.ahlyia.chaotic_claims

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

val PluginMenus: List<String> = listOf(
    "Chaotic Claims"
)

class GUIListener(private val settings: PluginSettings) : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {

    }
}

class PluginGUIs (plugin: ChaoticClaims){

}