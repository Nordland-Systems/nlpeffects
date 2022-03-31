package de.stevenpaw.nlp_effects.common.effects;

import de.stevenpaw.nlp_effects.common.database.SQL_Effects;
import de.stevenpaw.nlp_effects.common.utils.EffectTypes;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import org.bukkit.Location;
import org.bukkit.Particle;

public class SmallWaterFountain implements IEffect {

    private int id;
    private boolean isRunning;
    private Location loc;

    public SmallWaterFountain(int id, Location loc, boolean isRunning){
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
    public EffectTypes GetType() { return EffectTypes.SMALLWATERFOUNTAIN; }

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
            spawnLoc.getWorld().spawnParticle(Particle.WATER_SPLASH, spawnLoc, 5);
        }
    }
}
