package theworldsteps;

import arc.util.Log;
import mindustry.mod.Mod;

public class TheWorldStepsMod extends Mod {
    public TheWorldStepsMod() {
        Log.info("[TheWorldSteps] Mod constructor called.");
    }

    @Override
    public void init() {
        Log.info("[TheWorldSteps] Initializing...");
        StepsUnits.init();
        Log.info("[TheWorldSteps] Loaded successfully.");
    }

    @Override
    public void loadContent() {
        StepsUnits.loadContent();
    }
}
