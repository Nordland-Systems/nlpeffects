package de.stevenpaw.nlp_effects.common.effects;

import de.stevenpaw.nlp_effects.common.database.SQL_Effects;
import de.stevenpaw.nlp_effects.common.utils.EffectTypes;
import de.stevenpaw.nlp_effects.common.utils.IEffect;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;

public class Fireflame implements IEffect {

    private int id;
    private boolean isRunning;
    private Location loc;

    public Fireflame(int id, Location loc, boolean isRunning){
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
    public EffectTypes GetType() { return EffectTypes.FIREFLAME; }

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
            spawnLoc.getWorld().playEffect(spawnLoc, Effect.MOBSPAWNER_FLAMES, 0);
        }
    }
}
