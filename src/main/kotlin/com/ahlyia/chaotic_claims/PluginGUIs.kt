package com.ahlyia.chaotic_claims

import org.apache.commons.lang3.ObjectUtils
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

val PluginMenus: List<String> = listOf( // Contains the name of all Inventories made by the Plugin.
    "Chaotic Claims"
)

class GUIListener(private val settings: PluginSettings, plugin: ChaoticClaims) : Listener {
    val plugin = plugin

    @EventHandler(priority = EventPriority.HIGH)
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventoryTitle = event.view.title().toString()
        val isPartOfPlugin = PluginMenus.any { it == inventoryTitle }

        if(isPartOfPlugin){
            val item = event.currentItem
            val id = item?.itemMeta?.persistentDataContainer?.get(NamespacedKey(plugin,"button_function"),
                PersistentDataType.INTEGER)

            id?.let {
                plugin.pluginGUIs.getAction(id)(event) // get and call
            }

            event.isCancelled = true
        }
    }
}

class PluginGUIs (plugin: ChaoticClaims){
    val plugin: ChaoticClaims = plugin
    var GUIClickMaps: MutableMap<Int, (InventoryClickEvent) -> Int> = mutableMapOf()

    private var nextId = 0

    fun registerButton(item: ItemStack, func: (InventoryClickEvent) -> Int) {
        val myId = nextId++

        val itemMeta = item.itemMeta ?: return
        val key = NamespacedKey(plugin,"button_function")

        itemMeta.persistentDataContainer.set(key, PersistentDataType.INTEGER, myId)
        item.itemMeta = itemMeta

        GUIClickMaps[myId] = func
    }

    fun getAction(id: Int): (InventoryClickEvent) -> Int {
        return GUIClickMaps[id] ?: { 0 }
    }

    fun reset() {
        GUIClickMaps.clear()
        nextId = 0
    }
}