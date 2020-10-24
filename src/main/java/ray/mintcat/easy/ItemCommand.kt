package ray.mintcat.easy

import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.util.item.Items
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.permissions.PermissionDefault
import org.serverct.ersha.jd.AttributeAPI
import java.util.*

@BaseCommand(name = "eitem", aliases = ["ei"], permissionDefault = PermissionDefault.OP)
class ItemCommand : BaseMainCommand() {
    val title = Easy.getTitle()

    @SubCommand
    var give: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "给予物品"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("目标"), Argument("物品"), Argument("数量"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            ItemFeed.giveItemStack(args[1],player,args[2].toInt())
        }
    }

    @SubCommand
    var take: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "扣除物品"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("目标"), Argument("物品"), Argument("数量"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            Items.checkItem(player, ItemFeed.getItemStack(player,args[1]), args[2].toInt(), true)
        }
    }
}