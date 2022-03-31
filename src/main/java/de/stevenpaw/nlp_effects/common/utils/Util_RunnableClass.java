package de.stevenpaw.nlp_effects.common.utils;

import de.stevenpaw.nlp_effects.main.Main;
import org.bukkit.Bukkit;

import java.time.LocalDateTime;

import static de.stevenpaw.nlp_effects.main.Main.effectList;

public class Util_RunnableClass implements Runnable {
    public static void playEffects(){
        for (IEffect fx : effectList) {
            fx.ExecuteEffect();
        }
    }

    @Override
    public void run() {
        if(Bukkit.getServer().getOnlinePlayers().size() > 0) {
            actionAtFullMinute();
        }
    }

    private static void actionAtFullMinute() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            if (LocalDateTime.now().getSecond() <= 0) {

            }
        }
    }
}
