package ray.mintcat.easy

import io.izzel.taboolib.module.inject.TListener
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.serverct.ersha.jd.AttributeAPI
import org.serverct.ersha.jd.event.AttrAttributeUpdateEvent
import java.util.*


@TListener
class AttrListener: Listener {

    @EventHandler
    fun onAttrAttributeUpdateEvent(event: AttrAttributeUpdateEvent){
        val player = event.entity
        if (player !is Player){
            return
        }
        val attributeList = Easy.settings.getStringList("WorldAttribute.${player.world.name}") ?: return
        AttributeAPI.addAttribute(player, "WorldAttribute", attributeList)
    }
}