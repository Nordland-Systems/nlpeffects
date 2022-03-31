package de.stevenpaw.nlp_effects.common.commands;

import de.stevenpaw.nlp_effects.common.database.SQL_Effects;
import de.stevenpaw.nlp_effects.common.effects.Fireflame;
import de.stevenpaw.nlp_effects.common.effects.SmallWaterFountain;
import de.stevenpaw.nlp_effects.common.effects.WaterFountain;
import de.stevenpaw.nlp_effects.common.utils.EffectTypes;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import de.stevenpaw.nlp_effects.common.utils.Util_Log;
import de.stevenpaw.nlp_effects.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static de.stevenpaw.nlp_effects.main.Main.effectList;

public class CMDeffect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //nlpe <create/edit/remove/toggle> <type>
        if(sender instanceof Player) {
            Player p = (Player)sender;

            if(p.hasPermission("nlpeffect.*")) {
                if (args.length <= 1) {
                    p.sendMessage(Main.prefix + "§fUsage: /nlpe <create/edit/remove/toggle> <type/id>");
                } else if (args.length == 2) {
                    outerswitch:
                    switch (args[0]){
                        case "create":
                            EffectTypes type;
                            try {
                                type = EffectTypes.valueOf(args[1].toUpperCase());
                            } catch (NullPointerException ex){
                                p.sendMessage(Main.prefix + "§fUsage: /nlpe create §c<type>");
                                p.sendMessage(Main.prefix + "§fPossible Effects: " + EffectTypes.values());
                                break outerswitch;
                            }
                            p.sendMessage(Main.prefix + "§fcreating at: " + p.getLocation());
                            Location playerPosition = new Location(p.getWorld(), p.getLocation().getBlockX(),p.getLocation().getBlockY(),p.getLocation().getBlockZ());
                            switch (type){
                                case WATERFOUNTAIN:
                                    IEffect newWaterFountainEffect = new WaterFountain(effectList.size()+1, playerPosition, true);
                                    SQL_Effects.createEffect(newWaterFountainEffect);
                                    effectList.add(newWaterFountainEffect);
                                    break;
                                case FIREFLAME:
                                    IEffect newFireFlameEffect = new Fireflame(effectList.size()+1, playerPosition, true);
                                    SQL_Effects.createEffect(newFireFlameEffect);
                                    effectList.add(newFireFlameEffect);
                                    break;
                                case SMALLWATERFOUNTAIN:
                                    IEffect newSmallWaterFountainEffect = new SmallWaterFountain(effectList.size()+1, playerPosition, true);
                                    SQL_Effects.createEffect(newSmallWaterFountainEffect);
                                    effectList.add(newSmallWaterFountainEffect);
                                    break;
                                default:
                                    p.sendMessage(Main.prefix + "§fUsage: /nlpe create §c<type>");
                                    break outerswitch;
                            }
                            break;
                        case "edit":
                            p.sendMessage(Main.prefix + "§fedit at: " + p.getLocation());
                            break;
                        case "remove":
                            p.sendMessage(Main.prefix + "§fremoving at: " + p.getLocation());
                            break;
                        case "toggle":
                            int idGiven = 0;
                            try {
                                idGiven = Integer.parseInt(args[1]);
                            } catch (NumberFormatException ex){
                                p.sendMessage(Main.prefix + "§fUsage: /nlpe toggle §c<id>");
                                break outerswitch;
                            }
                            for (IEffect fx:effectList) {
                                if(idGiven == fx.GetID()){
                                    fx.ToggleEffect();
                                }
                            }
                            break;
                        default:
                            p.sendMessage(Main.prefix + "§fUsage: /nlpe <create/edit/remove/toggle> <type>");
                            break outerswitch;
                    }
                }
            }
        }

        return false;
    }
}
