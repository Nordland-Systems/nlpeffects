package de.stevenpaw.nlp_effects.main;

import de.stevenpaw.nlp_effects.common.commands.CMDeffect;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import de.stevenpaw.nlp_effects.common.utils.Util_Log;
import de.stevenpaw.nlp_effects.common.utils.Util_RunnableClass;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.ObjectInput;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public static String prefix = "§e[NLP|Effects] ";

    public static File file;
    public static FileConfiguration cfg;
    public static List<IEffect> effectList;

    public static Main getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getConsoleSender().sendMessage("§b|||   |||   §6|||      §b|||||");
        Bukkit.getConsoleSender().sendMessage("§b||||  |||   §6|||      §b|||  ||");
        Bukkit.getConsoleSender().sendMessage("§b||||| |||   §6|||      §b|||  ||");
        Bukkit.getConsoleSender().sendMessage("§b||| |||||   §6|||      §b|||||");
        Bukkit.getConsoleSender().sendMessage("§b|||  ||||   §6|||      §b|||");
        Bukkit.getConsoleSender().sendMessage("§b|||   |||   §6||||||   §b|||");
        Bukkit.getConsoleSender().sendMessage("§5EFFECTS");
        Bukkit.getConsoleSender().sendMessage("§aloaded");

        registerCommands();

        setupConfig();
        MySQL.setupMySQL();
        setupRunnable();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Erstelle und lade die Config
     */
    private void setupConfig(){
        saveDefaultConfig();
        file = new File("plugins/NLP_Effects", "config.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
        Util_Log.DebugConsoleLog("Config geladen","Main:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
    }

    private void setupRunnable(){
        Bukkit.getScheduler().runTaskTimer(this, Util_RunnableClass::playEffects, 0, 1);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("nlpeffects")).setExecutor(new CMDeffect());
    }
}
