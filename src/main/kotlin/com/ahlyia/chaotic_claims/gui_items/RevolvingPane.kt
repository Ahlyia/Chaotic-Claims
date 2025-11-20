package com.ahlyia.chaotic_claims.gui_items

import com.ahlyia.chaotic_claims.ChaoticClaims
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.AbstractItem

class PanesTracker() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    var panes: MutableMap<Int, RevolvingPane> = mutableMapOf()
    var current: Int = 0

    var isActive: Boolean = true

    fun reset() = panes.clear()

    fun start() = scope.launch {
        while (isActive) {
            delay(100)
            panes.values.forEach {
                current += 1
                Bukkit.getScheduler().runTask(ChaoticClaims.instance, Runnable {
                    panes.values.forEach {
                        it.refresh()
                    }
                })
            }
        }
    }

    fun stop() {
        isActive = false
        job.cancel()
    }
}

class RevolvingPane(val panesTracker: PanesTracker) : AbstractItem() {
    init {
        panesTracker.panes.set(panesTracker.panes.size,this)
    }

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        // do nothing
    }

    fun refresh(){
        notifyWindows()
    }

    override fun getItemProvider(): ItemProvider {
        if(panesTracker.panes[panesTracker.current] == this) {
            return ItemBuilder(Material.GRAY_STAINED_GLASS_PANE,1)
        } else {
            return ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE,1)
        }
    }
}