package de.stevenpaw.nlp_effects.common.database;

import de.stevenpaw.nlp_effects.common.effects.Fireflame;
import de.stevenpaw.nlp_effects.common.effects.SmallWaterFountain;
import de.stevenpaw.nlp_effects.common.effects.WaterFountain;
import de.stevenpaw.nlp_effects.common.utils.EffectTypes;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import de.stevenpaw.nlp_effects.common.utils.Util_Log;
import de.stevenpaw.nlp_effects.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static de.stevenpaw.nlp_effects.main.Main.effectList;
import static de.stevenpaw.nlp_effects.main.MySQL.con;

public class SQL_Effects {

    /**
     * Erstellt die Tabelle nlp_effects
     */
    public static void createEffectsTable() {
        Util_Log.DebugConsoleLog("§dnlp_effects erstellen...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        if (MySQL.isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS nlp_effects (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, type VARCHAR(50), isrunning BOOLEAN, world VARCHAR(50), x INT, y INT, z INT)");
                Util_Log.DebugConsoleLog("§dnlp_effects","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            }
            catch (SQLException e) {
                e.printStackTrace();
                Util_Log.DebugConsoleLog("§cnlp_effects konnte nicht erstellt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            }
        }
    }

    public static void loadEffects(){
        Util_Log.DebugConsoleLog("§fLoading Effects","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        effectList = new ArrayList<>();
        if (MySQL.isConnected()) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try{
                ps=con.prepareStatement("select * from nlp_effects");
                rs=ps.executeQuery();
                while(rs.next())
                {
                    EffectTypes type = EffectTypes.valueOf(rs.getString("type"));
                    if(type != null){
                        switch (type){
                            default:
                                break;
                            case SMALLWATERFOUNTAIN:
                                effectList.add(new SmallWaterFountain(rs.getInt("id"),new Location(Bukkit.getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")), rs.getBoolean("isrunning")));
                                break;
                            case WATERFOUNTAIN:
                                effectList.add(new WaterFountain(rs.getInt("id"),new Location(Bukkit.getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")), rs.getBoolean("isrunning")));
                                break;
                            case FIREFLAME:
                                effectList.add(new Fireflame(rs.getInt("id"),new Location(Bukkit.getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")), rs.getBoolean("isrunning")));
                                break;
                        }
                    }
                }

            }
            catch(Exception e)
            {
                System.out.println("Error in getData"+e);
            }

        }
    }

    /**
     * Setzt die Zählweise der Ids zurück
     */
    public static void resetIncrement() {
        Util_Log.DebugConsoleLog("§dnlp_effects ID zurücksetzen...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        try {
            MySQL.con.createStatement().executeUpdate("ALTER TABLE nlp_effects AUTO_INCREMENT = 1;");
            Util_Log.DebugConsoleLog("§dnlp_effects ID zurückgesetzt","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        } catch (SQLException e) {
            e.printStackTrace();
            Util_Log.DebugConsoleLog("§dnlp_effects ID konnte nicht zurückgesetzt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
    }

    /**
     * Erstellt den Spieler, wenn er noch nicht existiert
     * @param fx (IEffect) = Der zu erstellende Effekt
     */
    public static void createEffect(final IEffect fx) {
        resetIncrement();
        loadEffects();
        Util_Log.DebugConsoleLog("§dErstelle Effekt §6"+fx.GetType().toString()+" mit der ID "+fx.GetID()+" §din Tabelle...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        String effectID = ""+fx.GetID();
        EffectTypes effectType = fx.GetType();
        String isRunningAsString = fx.GetIsRunning()?"1":"0";
        if (!effectExists(fx)) {
            MySQL.update("INSERT INTO nlp_effects (type, isrunning, world, x, y, z) VALUES ('" +
                    fx.GetType().toString() + "','"+isRunningAsString+"','"+fx.GetLocation().getWorld().getName()+"','"+fx.GetLocation().getX()+"','"+fx.GetLocation().getY()+"','"+fx.GetLocation().getZ()+"')");
            Util_Log.DebugConsoleLog("§dEffekt §6"+fx.GetType().toString()+" mit der ID "+fx.GetID()+" §dwurde erstellt","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        } else {
            Util_Log.DebugConsoleLog("§cKonnte Effekt §6"+fx.GetType().toString()+" mit der ID "+fx.GetID()+" §cnicht erstellen, da er bereits existiert", "SQL_Effects:" + Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
    }

    /**
     * Überprüft, ob der Spieler bereits existiert
     * @param fx (Player) = Der zu überprüfende Spieler
     * @return Boolean = True/False
     */
    public static boolean effectExists(final IEffect fx) {

        Util_Log.DebugConsoleLog("§dPrüfe, ob Effekt §6"+fx.GetID()+"§d existiert...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM nlp_effects WHERE id= '" + fx.GetID() + "'");
            assert res != null;
            if(res.next()) {
                Util_Log.DebugConsoleLog("§dEffekt §6"+fx.GetType()+"§d mit der ID §6"+fx.GetID()+"§d existiert","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
                return true;
            }
            Util_Log.DebugConsoleLog("§cEffekt §6"+fx.GetType()+"§c mit der ID §6"+fx.GetID()+"§c existiert nicht","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            Util_Log.DebugConsoleLog("§cEffekt §6"+fx.GetType()+"§c mit der ID §6"+fx.GetID()+"§c existiert nicht","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            return false;
        }
    }

    public static void setValue(final IEffect fx, final String column, final Object value){
        Util_Log.DebugConsoleLog("§dSetze Wert aus Spalte §6"+column+" §dfür Effekt §6"+fx.GetID()+"§d...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        String usedString = value.toString();
        if (!effectExists(fx)) {
            Util_Log.DebugConsoleLog("§cWert aus Spalte §6"+column+" §cfür Effekt §6"+fx.GetID()+" §ckonnte nicht gesetzt werden. Erstelle Effekt und versuche erneut","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            createEffect(fx);
        }
        if(value instanceof Boolean){
            int i = 0;
            if ((boolean)value) {
                i = 1;
            }
            usedString = ""+i;
        } else if (value == null){
            usedString = "NULL";
        }
        MySQL.update("UPDATE nlp_effects SET " + column + "= '" + usedString + "' WHERE id= '"+fx.GetID()+"';");
        Util_Log.DebugConsoleLog("§dWert aus Spalte §6"+column+" §dfür Spieler §6"+fx.GetID()+" §dgesetzt: §6"+value,"SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
    }

    /**
     * Gibt den Boolean eines Spielers in einer Spalte wieder
     * @param fx (Player) = Der Spieler
     * @param column (String) = Die zu überprüfende Spalte
     * @return Boolean = True/False
     */
    public static boolean getBoolean(final IEffect fx, final String column) {
        Util_Log.DebugConsoleLog("§dFrage Boolean aus Spalte §6"+column+" §dfür Effekt §6"+fx.GetID()+"§d ab...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());

        boolean bool = false;
        if (!effectExists(fx)) {
            Util_Log.DebugConsoleLog("§cBoolean aus Spalte §6"+column+" §cfür Effekt §6"+fx.GetID()+" §ckonnte nicht abgefragt werden. Erstelle Spieler und versuche erneut","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            createEffect(fx);
        }
        try {
            final ResultSet res = MySQL.getResult("SELECT "+column+" FROM nlp_effects WHERE id='"+fx.GetID()+"'");
            assert res != null;
            if (res.next()) {
                bool = res.getBoolean(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Util_Log.DebugConsoleLog("§cBoolean aus Spalte §6"+column+" §cfür Effekt §6"+fx.GetID()+" §ckonnte nicht abgefragt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
        Util_Log.DebugConsoleLog("§dBoolean aus Spalte §6"+column+" §dfür Effekt §6"+fx.GetID()+" §dabgefragt: §6"+bool,"SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        return bool;

    }

    /**
     * Gibt den String eines Spielers in einer Spalte wieder
     * @param fx (Player) = Der Spieler
     * @param column (String) = Die zu überprüfende Spalte
     * @return String = String des Spielers aus der Spalte
     */
    public static String getString(final IEffect fx, final String column) {
        Util_Log.DebugConsoleLog("§dFrage String aus Spalte §6"+column+" §dfür Effekt §6"+fx.GetID()+"§d ab...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());

        if (effectExists(fx)) {
            final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM nlp_effects WHERE id='"+fx.GetID()+"'");
            try {
                assert rs != null;
                if (rs.next()) {
                    Util_Log.DebugConsoleLog("§dString aus Spalte §6"+column+" §dfür Effekt §6"+fx.GetID()+" §dabgefragt: §6"+rs.getString(column),"SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
                    return rs.getString(column);
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
                Util_Log.DebugConsoleLog("§cString aus Spalte §6"+column+" §cfür Effekt §6"+fx.GetID()+" §ckonnte nicht abgefragt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
                return null;
            }
        }
        return null;
    }

    /**
     * Gibt den Integer eines Spielers in einer Spalte wieder
     * @param fx (Player) = Der Spieler
     * @param column (String) = Die zu überprüfende Spalte
     * @return Integer = Integer des Spielers aus der Spalte
     */
    public static Integer getInt(final IEffect fx, final String column) {
        Util_Log.DebugConsoleLog("§dFrage Integer aus Spalte §6"+column+" §dfür Spieler §6"+fx.GetID()+" §dab...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());

        try {
            con.createStatement().executeUpdate("UPDATE nlp_effects SET "+column+" = 0 WHERE "+column+" IS NULL");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM nlp_effects WHERE id='"+fx.GetID()+"'");
        try {
            assert rs != null;
            if (rs.next()) {
                Util_Log.DebugConsoleLog("§dInteger aus Spalte §6"+column+" §dfür Spieler §6"+fx.GetID()+" §dabgefragt: §6"+rs.getInt(column),"SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
                return rs.getInt(column);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Util_Log.DebugConsoleLog("§cInteger aus Spalte §6"+column+" §cfür Spieler §6"+fx.GetID()+" §ckonnte nicht abgefragt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
            return 0;
        }
        return 0;
    }

    /**
     * Gibt den Double eines Spielers in einer Spalte wieder
     * @param fx (Player) = Der Spieler
     * @param column (String) = Die zu überprüfende Spalte
     * @return Double = Double des Spielers aus der Spalte
     */
    public static Double getDouble(final IEffect fx, final String column) {
        Util_Log.DebugConsoleLog("§dFrage Double aus Spalte §6"+column+" §dfür Spieler §6"+fx.GetID()+" §dab...","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());

        try {
            con.createStatement().executeUpdate("UPDATE nlp_effects SET "+column+" = 0 WHERE "+column+" IS NULL");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM nlp_effects WHERE id='"+fx.GetID()+"'");
        try {
            assert rs != null;
            if (rs.next()) {
                Util_Log.DebugConsoleLog("§dDouble aus Spalte §6"+column+" §dfür Spieler §6"+fx.GetID()+" §dabgefragt: §6"+rs.getInt(column),"SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
                return rs.getDouble(column);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            Util_Log.DebugConsoleLog("§cDouble aus Spalte §6"+column+" §cfür Spieler §6"+fx.GetID()+" §ckonnte nicht abgefragt werden","SQL_Effects:"+Thread.currentThread().getStackTrace()[1].getLineNumber());
        }
        return null;
    }
}
