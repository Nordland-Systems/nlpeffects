package de.stevenpaw.nlp_effects.common.utils;

import org.bukkit.Location;

public interface IEffect {
    //Methoden
    public void ToggleEffect();
    public EffectTypes GetType();
    public int GetID();
    public Location GetLocation();
    public boolean GetIsRunning();
    public void ExecuteEffect();
}
