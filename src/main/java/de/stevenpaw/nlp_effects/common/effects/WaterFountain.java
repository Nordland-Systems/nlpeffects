package de.stevenpaw.nlp_effects.common.effects;

import de.stevenpaw.nlp_effects.common.database.SQL_Effects;
import de.stevenpaw.nlp_effects.common.utils.EffectTypes;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import de.stevenpaw.nlp_effects.common.utils.Util_Log;
import org.bukkit.Location;
import org.bukkit.Particle;

public class WaterFountain implements IEffect {

    private int id;
    private boolean isRunning;
    private Location loc;
    private float radius = 1.2f;
    private float angle = 0f;

    public WaterFountain (int id, Location loc, boolean isRunning){
        this.id = id;
        this.loc = loc;
        this.isRunning = isRunning;
    }

    @Override
    public void ToggleEffect()
    {
        isRunning = !isRunning;
        SQL_Effects.setValue(this, "isRunning", isRunning);
    }

    @Override
    public EffectTypes GetType() { return EffectTypes.WATERFOUNTAIN; }

    @Override
    public int GetID() { return id; }

    @Override
    public Location GetLocation() { return loc; }

    @Override
    public boolean GetIsRunning() { return isRunning; }

    @Override
    public void ExecuteEffect() {
        if(isRunning){

            Location spawnLoc = new Location(loc.getWorld(), loc.getX()+0.5f, loc.getY(), loc.getZ()+0.5f);
            double x = (radius * Math.sin(angle));
            double z = (radius * Math.cos(angle));
            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, new Location(spawnLoc.getWorld(), spawnLoc.getX()+x, spawnLoc.getY(), spawnLoc.getZ()+z), 3);

            x = (radius * 0.5 * Math.sin(angle));
            z = (radius * 0.5 * Math.cos(angle));
            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, new Location(spawnLoc.getWorld(), spawnLoc.getX()+x, spawnLoc.getY()+1f, spawnLoc.getZ()+z), 3);

            x = (radius * 0.5 * Math.sin(-angle));
            z = (radius * 0.5 * Math.cos(-angle));
            angle += 0.3;

            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, new Location(spawnLoc.getWorld(), spawnLoc.getX()+x, spawnLoc.getY()+1f, spawnLoc.getZ()+z), 3);
            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, spawnLoc, 5);
            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, new Location(spawnLoc.getWorld(), spawnLoc.getX(), spawnLoc.getY()+1f, spawnLoc.getZ()), 5);
        }
    }
}
