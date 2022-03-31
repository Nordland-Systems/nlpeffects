package de.stevenpaw.nlp_effects.common.utils;

import de.stevenpaw.nlp_effects.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

public class Util_Log {

    /**
     * Formatiert die ChatLog-Nachricht und speichert/gibt sie aus
     * @param p (Player) = Der Spieler
     * @param message (String) = Nachricht
     */
    public static void ChatLog(Player p, String message) {
        ChatColor.stripColor(message);
        logToFile("["+Util_Time.getRawTimeWithSeconds()+"] [CHAT] "+p.getName()+" >> "+message);
        Bukkit.getConsoleSender().sendMessage(Main.prefix+Util_Time.getFormattedTimeWithSeconds()+" §a[CHAT] §6"+p.getName()+" §b>> §a"+message);
    }

    /**
     * Formatiert die ChatLog-Nachricht und speichert/gibt sie aus
     * @param p (Player) = Der Spieler
     * @param message (String) = Nachricht
     */
    public static void StaffChatLog(Player p, String message) {
        ChatColor.stripColor(message);
        logToFile("["+Util_Time.getRawTimeWithSeconds()+"] [STAFFCHAT] "+p.getName()+" >> "+message);
        Bukkit.getConsoleSender().sendMessage(Main.prefix+Util_Time.getFormattedTimeWithSeconds()+" §a[STAFFCHAT] §6"+p.getName()+" §b>> §a"+message);
    }

    /**
     * Formatiert die CommandLog-Nachricht und speichert/gibt sie aus
     * @param s (CommandSender) = Der CommandSender
     * @param message (String) = Nachricht
     */
    public static void CommandLog(CommandSender s, String message) {
        ChatColor.stripColor(message);
        logToFile("["+Util_Time.getRawTimeWithSeconds()+"] [CMD] "+s.getName()+" >> "+message);
        Bukkit.getConsoleSender().sendMessage(Main.prefix+Util_Time.getFormattedTimeWithSeconds()+" §a[CMD] §6"+s.getName()+" §b>> §a"+message);
    }

    /**
     * Formatiert die Debug-Nachricht und speichert/gibt sie aus
     * @param message (String) = Nachricht
     * @param line (String) = Class & Zeile
     */
    public static void DebugConsoleLog(String message, String line) {
        if(Main.cfg.getBoolean("SendDebugMessages")) {
            Bukkit.getConsoleSender().sendMessage(Main.prefix+Util_Time.getFormattedTimeWithSeconds()+" §7[§5DEBUG§7] §a"+message+" §7("+line+")");
        }
    }

    /**
     * Schreibt die Nachricht in die Log-Datei
     * @param message (String) = Nachricht
     */
    private static void logToFile(String message) {
        try {
            File dataFolder = Main.getPlugin().getDataFolder();
            File log = new File(dataFolder+File.separator+"Log", Util_Time.getRawDateReverse()+".txt");
            if(!log.exists()) {
                log.getParentFile().mkdirs();
                log.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(log, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
            PrintWriter pw = new PrintWriter(osw);
            pw.println(message);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
