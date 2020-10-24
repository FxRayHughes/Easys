package ray.mintcat.easy

import io.izzel.taboolib.TabooLibAPI
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.util.item.Items
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionDefault
import org.serverct.ersha.jd.AttributeAPI
import org.w3c.dom.Attr
import java.util.*


@BaseCommand(name = "easy", aliases = ["es"], permissionDefault = PermissionDefault.OP)
class EasyCommand : BaseMainCommand() {

    val title = Easy.getTitle()

    @SubCommand
    var attrtime: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "临时属性 [不可叠加]"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            val attributeList: List<String> = Arrays.asList("${args[1]}: ${args[2]}")

            AttributeAPI.addAttribute(player, args[1], attributeList)
            Bukkit.getScheduler().runTaskLater(Easy.plugin, Runnable {
                AttributeAPI.deleteAttribute(player,args[1])
            },args[3].toLong())
        }
    }

    @SubCommand
    var attrtimeb: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "临时属性 [可叠加]"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("目标"), Argument("属性"), Argument("数值"), Argument("持续时间"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player = Bukkit.getPlayerExact(args[0]) ?: return
            val attributeList: List<String> = Arrays.asList("${args[1]}: ${args[2]}")

            val uuid = UUID.randomUUID().toString()
            AttributeAPI.addAttribute(player, uuid, attributeList)
            Bukkit.getScheduler().runTaskLater(Easy.plugin, Runnable {
                AttributeAPI.deleteAttribute(player,uuid)
            },args[3].toLong())
        }
    }

    @SubCommand
    var buy: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "购买物品"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("购买者"), Argument("物品名"), Argument("单价"), Argument("数量"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {
            val player: Player = if (args.size == 0) {
                sender as Player
            } else {
                Bukkit.getPlayerExact(args[0])
            } ?: return


            if (TabooLibAPI.getPluginBridge().economyLook(player) < args[2].toDouble() * args[3].toDouble().toInt()){
                player.sendMessage(getMsg(player, "NoMoney"))
                return
            }
            val ItemStack = ItemFeed.getItemStack(player, args[1]) ?: return
            TabooLibAPI.getPluginBridge().economyTake(player, (args[2].toDouble() * args[3].toDouble().toInt()))
            ItemFeed.giveItemStack(args[1], player, args[3].toDouble().toInt())
            val msg = getMsg(player, "YesBuy")
                    .replace("{Amount}", args[3])
                    .replace("{ItemName}", ItemStack.itemMeta.displayName)
                    .replace("{Money}", (args[2].toDouble() * args[3].toDouble().toInt()).toString())
            player.sendMessage(msg)
        }
    }

    @SubCommand
    var sell: BaseSubCommand = object : BaseSubCommand() {
        override fun getDescription(): String {
            return "出售物品"
        }
        override fun getArguments(): Array<Argument>? {
            return arrayOf(Argument("出售者"), Argument("物品名"), Argument("单价"), Argument("数量"))
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>) {

            val player: Player = if (args.size == 0) {
                sender as Player
            } else {
                Bukkit.getPlayerExact(args[0])
            } ?: return

            val ItemStack = ItemFeed.getItemStack(player, args[1]) ?: return
            if (ItemFeed.getItemAmount(player, args[1]) < args[3].toDouble().toInt()){
                val msg = getMsg(player, "NoItems")
                        .replace("{Amount}", args[3])
                        .replace("{ItemName}", ItemStack.itemMeta.displayName)
                player.sendMessage(msg)
                return
            }

            TabooLibAPI.getPluginBridge().economyGive(player, (args[2].toDouble() * args[3].toDouble().toInt()))
            Items.checkItem(player, ItemStack, args[3].toDouble().toInt(), true)
            val msg = getMsg(player, "YesSell")
                    .replace("{Amount}", args[3])
                    .replace("{ItemName}", ItemStack.itemMeta.displayName)
                    .replace("{Money}", (args[2].toDouble() * args[3].toDouble().toInt()).toString())
            player.sendMessage(msg)
        }
    }
    fun getMsg(player: Player, type: String):String{
        val msg = Easy.settings.getStringColored("Message.${type}") ?: return ""
        return (title + TabooLibAPI.getPluginBridge().setPlaceholders(player, msg))
    }
}