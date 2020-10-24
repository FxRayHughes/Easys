package ray.mintcat.easy

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

object Easy : Plugin() {

    @TInject(value = ["settings.yml"], locale = "LOCALE-PRIORITY")
    lateinit var settings: TConfig
        private set

    fun getTitle(): String {
        return settings.getString("Title", "§7§l[§f§l Easy §7§l] §f")
    }
}