package theworldsteps;

import arc.util.Log;
import mindustry.mod.Mod;

public class TheWorldStepsMod extends Mod {

    public void init() {
        Log.info("[TheWorldSteps] Initializing...");
        theworldsteps.content.StepsUnits.init();
        Log.info("[TheWorldSteps] Loaded successfully.");
    }

    public void loadContent() {
        theworldsteps.content.StepsUnits.loadContent();
    }
}
