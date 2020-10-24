package ray.mintcat.easy

import io.izzel.taboolib.module.inject.THook
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

@THook
class ItemPapi : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return "eitem"
    }

    override fun getAuthor(): String {
        return "Ray_Hughes"
    }

    override fun getVersion(): String {
        return "Last"
    }

    override fun persist(): Boolean {
        return true
    }

    override fun onPlaceholderRequest(player: Player, params: String): String {
        if (!player.isOnline) {
            return "N/A"
        }
        val param = params.split("_".toRegex())
        return when (param[0]) {
            "has" -> ItemFeed.getItemAmount(player, param[1]).toString()
            else -> {
                "N/A"
            }
        }
    }
}